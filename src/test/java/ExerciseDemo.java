import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDemo {
    public static void main(String[] args) {
        double c = 0.1;
        double K = 10000;
        double M = 3;
        double �� = 0.02;
        List<Double> list = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            list.add(c * Math.sqrt(M * K) * Math.log(K / (M * ��)) * Math.log(K / (M * ��))
                    - c * Math.sqrt(K) * Math.log(K / ��) * Math.log(K / ��));
            M++;
        }
        System.out.println(list.toString());
    }
}
