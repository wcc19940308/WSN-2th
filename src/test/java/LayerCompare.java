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

//		����ǰ��ӡ
        System.out.println("����ǰ");
        for (Map.Entry<Integer, Double> entry : mapList) {
            System.out.println(entry.toString());
        }
        System.out.println();

        Collections.sort(mapList, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> obj1, Map.Entry<Integer, Double> obj2) {
                // ��ʹ�����ñȽϺ���, ������ܻᱨ��, Υ��ʹ��Լ��
                // ����Ҫ���㽻����, ������ֵcompare(x, y)��compare(y, x)Ӧһ��
                return obj1.getValue().compareTo(obj2.getValue()); // �Ƚ�mapֵ
//				return obj1.getKey().compareTo(obj2.getKey()); // �Ƚ�map��
            }
        });

//		������ӡ
        System.out.println("�����");
        for (int id : map.keySet()) {
            System.out.println(id);

        }
    }
}
