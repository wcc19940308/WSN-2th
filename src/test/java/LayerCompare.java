import java.util.*;

public class LayerCompare {
    public static void main(String[] args) {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(5, Math.random() * 100);
        map.put(3, Math.random() * 100);
        map.put(1, Math.random() * 100);
        map.put(2, Math.random() * 100);
        map.put(4, Math.random() * 100);

        List<Map.Entry<Integer, Double>> mapList =
                new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());

//		排序前打印
        System.out.println("排序前");
        for (Map.Entry<Integer, Double> entry : mapList) {
            System.out.println(entry.toString());
        }
        System.out.println();

        Collections.sort(mapList, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> obj1, Map.Entry<Integer, Double> obj2) {
                // 请使用内置比较函数, 否则可能会报错, 违反使用约定
                // 具体要满足交换律, 即返回值compare(x, y)与compare(y, x)应一致
                return obj1.getValue().compareTo(obj2.getValue()); // 比较map值
//				return obj1.getKey().compareTo(obj2.getKey()); // 比较map键
            }
        });

//		排序后打印
        System.out.println("排序后");
        for (int id : map.keySet()) {
            System.out.println(id);

        }
    }
}
