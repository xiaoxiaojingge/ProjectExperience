package com.itjing.community;

import java.io.IOException;

/**
 * @author: lijing
 * @Date: 2021年08月03日 11:19
 * @Description: 将网页生成 pdf 或者 长图
 */
public class WkHtmlToPdfTests {
    public static void main(String[] args) {
        String cmd = "d:/work/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com d:/work/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
