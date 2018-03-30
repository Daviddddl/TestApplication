package com.example.didonglin.testapplication.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;


/**
 * 描述:
 * 对文件操作的一些操作工具
 *
 * @outhor didonglin
 * @create 2018-01-05 下午6:21
 */


public class FileUtil {

    private String defaultFilePath = "";

    /**
     * 读取指定路径下的文件
     * @param filepath 文件路径
     * @return String类型的文件内容
     * @throws IOException
     */
    public static String readFile(String filepath) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String s;
        while ((s = br.readLine()) != null)
            result.append(System.lineSeparator() + s);
        br.close();
        return result.toString();

    }

    /**
     * 清除字符串中的空格、空行等
     * @param str  需要处理的字符串
     * @return  String类型，清除空格、空行后的字符串
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 清除空行
     * @param str 需要处理的字符串
     * @return  清除空行后的字符串
     */
    public static String replaceBlankLine(String str) {
        String result = str.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        ;
        for (int i = 0; i < 10; i++) {
            result = result.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        }
        return result;
    }


    /**
     * 将字符串写入文件
     * @param args  待写入的数据
     * @param path  写入的文件路径
     * @return  操作成功返回true， 否则返回false
     */
    public static boolean writeFile(String args, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(args);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }


    /**
     * 清除文件
     * @param filepath  文件路径
     * @return  操作成功返回true，否则返回false
     */
    public static boolean clearFile(String filepath) {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 获取引号内容
     * @param str
     * @return
     */
    public static ArrayList getQuoCon(String str){
        HashMap<Integer, Integer> quoindex = new HashMap<>();
        for(int i = 0, j = 0; i< str.length();i++)
            if(str.charAt(i) == '\"')
                quoindex.put(j++,i);
        if(quoindex.size() % 2 != 0)
            return null;
        ArrayList<String> res = new ArrayList<>();
        for(int i = 0; i < quoindex.size(); i++){
            int startindex = quoindex.get(i)+1;
            int endindex = quoindex.get(++i);
            res.add(str.substring(startindex,endindex));
        }
        return res;
    }

    //输入图片文件路径，返回图片base64编码字符串
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            //e.printStackTrace();
            return "";
        }
        return new String(Base64.encodeBase64(data));
    }

    //输入图片base64编码字符串和保存路径，将字符串转为二进制图片存入保存路径
    public static boolean generateImage(String imgStr,String imgFilePath) {

        if (imgStr == null) //图像数据为空
            return false;

        try {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }

                //生成jpeg图片

                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();
                return true;
            }
        }catch(Exception e1){
            e1.printStackTrace();
            return false;
        }
        return false;
    }
}