package com.bobo.iweeker.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {

    final static int HTTP_METHOD_GET = 0;
    final static int HTTP_METHOD_POST = 1;
    final static int HTTP_METHOD_DELETE = 2;

    // 将InputStream流转换成String
    private static String convertStreamReaderToString(
            InputStreamReader inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(inputStream, 1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static String getHttpsResponseStr(int requestMethod, String urlStr,
            Map<String, String> getParams, Map<String, String> postParams)
                    throws Exception {

        String resoposeStr = null;

        String getStr = map2Params(getParams);

        URL myURL;
        HttpsURLConnection httpsConn;
        switch (requestMethod) {
            case HTTP_METHOD_GET:
                myURL = new URL(urlStr + "?" + getStr);
                httpsConn = (HttpsURLConnection) myURL.openConnection();
                if (httpsConn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // 取得该连接的输入流，以读取响应内容
                    InputStreamReader insr = new InputStreamReader(
                            httpsConn.getInputStream());
                    resoposeStr = convertStreamReaderToString(insr);
                }
                break;
            case HTTP_METHOD_POST:
                myURL = new URL(urlStr + "?" + getStr);
                httpsConn = (HttpsURLConnection) myURL.openConnection();
                String postStr = map2Params(postParams);
                httpsConn.setRequestMethod("POST");
                httpsConn.setUseCaches(false);
                httpsConn.setDoOutput(true);
                httpsConn.setDoInput(true);
                httpsConn.setInstanceFollowRedirects(true);
                httpsConn.setRequestProperty("Content-length",
                        String.valueOf(postStr.length()));
                httpsConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpsConn
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31");
                OutputStreamWriter dos = new OutputStreamWriter(
                        httpsConn.getOutputStream());
                // 将请求参数数据向服务器端发送
                dos.write(postStr);
                dos.flush();
                dos.close();
                if (httpsConn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // 取得该连接的输入流，以读取响应内容
                    InputStreamReader insr = new InputStreamReader(
                            httpsConn.getInputStream());
                    resoposeStr = convertStreamReaderToString(insr);
                }
                break;
            case HTTP_METHOD_DELETE:
                HttpClient client = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(urlStr + "?" + getStr);
                HttpResponse response = client.execute(httpDelete);
                if (response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK) {
                    // 取得该连接的输入流，以读取响应内容
                    resoposeStr = EntityUtils.toString(response.getEntity());
                }
                break;
            default:
                break;
        }
        return resoposeStr;
    }

    public static String map2Params(Map<String, String> paramsStr)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsStr.entrySet()) {
            sb.append(entry.getKey()).append("=")
            .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            sb.append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String params) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + params;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, List<NameValuePair> postParams) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            HttpPost post = new HttpPost(url);

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams,
                    HTTP.UTF_8);
            post.setEntity(entity);
            //

            HttpClient hc = new DefaultHttpClient();
            HttpResponse response = hc.execute(post);
            HttpEntity httpEntity = response.getEntity();

            if (httpEntity != null) {
                InputStream is = httpEntity.getContent();

                result = convertStreamToString(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static String convertStreamToString(InputStream is) {
        ByteArrayOutputStream oas = new ByteArrayOutputStream();
        copyStream(is, oas);
        String t = oas.toString();
        try {
            oas.close();
            oas = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    private static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

}
