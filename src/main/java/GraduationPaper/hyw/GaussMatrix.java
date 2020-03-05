package GraduationPaper.hyw;


import java.util.Arrays;

/**
 * ����α�������˼��
 * M�е�ÿһ�д���ÿ��������Ű�����ЩԴ������Ϣ��
 * c����ÿ�ζ�����һ��������ţ�����Դ���ŵ��������
 * e��Ϊϵ������.
 */
public class GaussMatrix {

	public static int k = 3;
	public static int matrixM[][] = //new int[k][k];
			{
					{1, 1, 1},
					{1, 1, 0},
					{1, 0, 0},
			};
	public static int vectorY[] = {0,0,0}; // Դ���ŵļ��ϣ������Ž����Դ����
	// ÿ�����Ľ��e����ϵ������, ����Ҫ����������е����ɶ�ά����
	public static int[] vectorE = {1, 1, 1};
	public static int emptyRows = k;

	public static void main(String[] args) {
		preProcess();
		// ������������һ��Դ���ŵ�����
		System.out.println(Arrays.toString(vectorY));
	}

	public static void preProcess() {
		// �������ƣ�������һ��������ŵļ���
		int[] newSymbols = {1, 1, 0};
		while (emptyRows > 0) {
			for (int i = 0; i < k; i++) {
				// ��������ģ��ÿ���յ�һ���µı������c
				getResult(newSymbols[i]);
			}
		}
	}

	// ÿ�ν���һ���µı�����ź�
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
