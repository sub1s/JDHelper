package com.wp.jdhelper.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JDReq {


    public static void getQRCode() {
        URL url = null;
        try {
            url = new URL("https://passport.jd.com/new/login.aspx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.addRequestProperty("ContentType", "text/html; charset=utf-8");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch");

            conn.connect();

            int code = conn.getResponseCode();
            if (code != 200) {
                System.out.println("error, code:" + code);
            } else {
                System.out.println(200);
            }

            InputStream inputStream = conn.getInputStream();
            
            transStreamToFile(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transStreamToFile(InputStream inputStream) throws
            IOException {
        File file = new File("/sdcard/code.png");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
//        byte[] buffer = new byte[1024];
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            bufferedWriter.write(temp);
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        bufferedReader.close();
    }
}
