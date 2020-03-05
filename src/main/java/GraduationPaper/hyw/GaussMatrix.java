package GraduationPaper.hyw;


import java.util.Arrays;

/**
 * 根据伪代码的意思：
 * M中的每一行代表每个编码符号包含哪些源符号信息；
 * c代表每次都到的一个编码符号，即对源符号的异或结果；
 * e作为系数矩阵.
 */
public class GaussMatrix {

	public static int k = 3;
	public static int matrixM[][] = //new int[k][k];
			{
					{1, 1, 1},
					{1, 1, 0},
					{1, 0, 0},
			};
	public static int vectorY[] = {0,0,0}; // 源符号的集合，这里存放解出的源符号
	// 每次异或的结果e，即系数矩阵, 可能要根据你的自行调整成多维矩阵
	public static int[] vectorE = {1, 1, 1};
	public static int emptyRows = k;

	public static void main(String[] args) {
		preProcess();
		// 最后我们输出看一下源符号的内容
		System.out.println(Arrays.toString(vectorY));
	}

	public static void preProcess() {
		// 这里逆推，给出了一个编码符号的集合
		int[] newSymbols = {1, 1, 0};
		while (emptyRows > 0) {
			for (int i = 0; i < k; i++) {
				// 这里我们模拟每次收到一个新的编码符号c
				getResult(newSymbols[i]);
			}
		}
	}

	// 每次接收一个新的编码符号和
	public static void getResult(int c) {
		for (int i = 0; i < k; i++) {
			if (vectorE[i] == 1 && matrixM[i][i] == 1) {
				for (int j = 0; j < k; j++) {
					vectorE[j] ^= matrixM[i][j];
				}
				c ^= vectorY[i];
			}
		}

		int l;
		for (l = 0; l < k; l++) {
			if (vectorE[l] == 1)
				break;
		}
		if (l < k) {
			matrixM[l] = vectorE;
			vectorY[l] = c;
			emptyRows -= 1;
			for (int i = 0; i <= l - 1; i++) {
				if (matrixM[i][l] == 1) {
					for (int j = 0; j < k; j++) {
						matrixM[i][j] = matrixM[i][j] ^ matrixM[l][j];
					}
					vectorY[i] = vectorY[i] ^ vectorY[l];
				}
			}
		}
	}
}
