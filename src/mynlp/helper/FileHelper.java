package mynlp.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileHelper {

    // 读取文本返回内容，按行分填入字符串数组
    public static ArrayList<String> readFile(String filePath) {
        ArrayList<String> content = new ArrayList<>();
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));//构造一个BufferedReader类来读取文件
            String s;
            while((s = br.readLine())!= null){//使用readLine方法，一次读一行
                content.add(s);
            }
            br.close();
        } catch(Exception e){
            e.printStackTrace();
            return content;
        }
        return content;
    }

    // 输出文本内容到指定文件目录，不存在则创建，若存在则覆盖
    public static void writeFile(ArrayList<String> content, String filePath) {
        File file = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (String str : content) {
                fos.write(str.getBytes());
                fos.write("\n".getBytes());
            }
            fos.close();
            System.out.println("File "+ filePath +" has been updated..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
