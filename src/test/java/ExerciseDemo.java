public class ExerciseDemo {
    public static void main(String[] args) {
        int[] ints = {1, 2, 3, 4, 5};
        print(ints);
    }

    public static void print(int... str) {
        for (int i=0; i<str.length; i++) {
            System.out.println(str[i]);
        }
    }
}
