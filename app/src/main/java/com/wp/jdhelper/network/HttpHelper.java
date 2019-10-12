package com.wp.jdhelper.network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    private static final String USER_AGENT = "Mozilla/5.0";

    // HTTP GET请求
    public static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        String inputLine = transStream2String(con.getInputStream());
        //打印结果
        System.out.println(inputLine);
        return inputLine;
    }


    public static String getRealUrl(String urlString) throws
            IOException {
//        URL url = new URL("https://u.jd.com/jgxB39");
        String url = "https://u.jd.com/jgxB39";
        System.out.println("访问地址:" + url);

        //发送get请求
        URL serverUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        conn.setRequestMethod("GET");
        //必须设置false，否则会自动redirect到重定向后的地址
        conn.setInstanceFollowRedirects(false);
        conn.addRequestProperty("Accept-Charset", "UTF-8;");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
        conn.connect();
        int code = conn.getResponseCode();
        if (code == 302) {
            System.out.println("302");
            System.out.println(conn.getHeaderField("Location"));
        } else {
            System.out.println(code);
            System.out.println(conn.getURL().getPath());
        }
        String result = transStream2String(conn.getInputStream());
        System.out.println(result);
        int startIndex = result.indexOf("var hrl=");
        System.out.println(startIndex);
        int endIndex = result.indexOf(";", startIndex);
        System.out.println(endIndex);
        String realUrl = result.substring(startIndex + 9, endIndex - 1);
        System.out.println(realUrl);

        URL realURL = new URL(realUrl);
        HttpURLConnection conn2 = (HttpURLConnection) realURL.openConnection();
        conn2.setRequestMethod("GET");
        //必须设置false，否则会自动redirect到重定向后的地址
        conn2.setInstanceFollowRedirects(false);
        conn2.addRequestProperty("Accept-Charset", "UTF-8;");
        conn2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
        conn2.connect();
        int code2 = conn2.getResponseCode();
        if (code2 == 302) {
            System.out.println("302");
            String realResult = conn2.getHeaderField("Location");
            System.out.println("realResult: " + realResult);
        } else {
            System.out.println(code2);
            System.out.println(conn2.getURL().getPath());
        }
//        HttpClient client = new HttpClient();
//        HttpMethod method = new GetMethod(url);
//        HttpParams params = client.getParams();
//        params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
//        client.
//        String realUrl = method.getURI().getURI();
//        String result = transStream2String(conn.getInputStream());
        return "123";
    }

    private static String transStream2String(InputStream inputStream) throws
            IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
