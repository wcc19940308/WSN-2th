package ExperimentCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 工具类
public class Utils {
    // 产生从min到max的不重复的n个随机数，用于模拟随机取节点上的数据
    public  static int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt( max - min + 1 ) + min;
    }

    // 用于随机从min到max不重复的数中产生count个，返回count个数组成的数组
    public  static int[] getRandoms(int min, int max, int count){
        int[] randoms = new int[count];
        List<Integer> listRandom = new ArrayList<Integer>();

        if( count > ( max - min + 1 )){
            return null;
        }
        // 将所有的可能出现的数字放进候选list
        for(int i = min; i <= max; i++){
            listRandom.add(i);
        }
        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for(int i = 0; i < count; i++){
            int index = getRandom(0, listRandom.size()-1);
            randoms[i] = listRandom.get(index);
            listRandom.remove(index);
        }
        return randoms;
    }

    public static boolean mkDirector(String path) {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                return file.mkdir();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
