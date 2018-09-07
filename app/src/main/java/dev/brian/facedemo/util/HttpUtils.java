package dev.brian.facedemo.util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/8
 * 描述:
 */

public class HttpUtils {

    public static String get(String url, Map<String, Object> args) throws IOException {

        StringBuilder fullHostUrl = new StringBuilder(url).append("?");


        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();


                if (value instanceof File) {
                    value = new FileInputStream(File.class.cast(value));
                }

                if (value instanceof InputStream) {
                    fullHostUrl.append(key).append("=");
                    InputStream is = InputStream.class.cast(value);
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    fullHostUrl.append(URLEncoder.encode(Base64.encodeToString(data, data.length), "UTF-8"));

                    is.close();
                } else {
                    fullHostUrl.append(key).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
                }

                fullHostUrl.append("&");
            }
        }

        URL host = new URL(fullHostUrl.toString());
        HttpURLConnection connection = HttpURLConnection.class.cast(host.openConnection());

        connection.setRequestMethod("GET");

        connection.setUseCaches(false);

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charsert", "UTF-8");


        int resultCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        if (resultCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
        } else {
            response.append(resultCode);
        }
        return response.toString();
    }


    public static String post(String url, Map<String, Object> args) throws IOException {
        //
        URL host = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(host.openConnection());
        //
        connection.setRequestMethod("POST");
        //
        connection.setDoOutput(true);
        connection.setDoInput(true);
        //
        connection.setUseCaches(false);
        //
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charsert", "UTF-8");
        //
        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

        //
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                //
                if (value instanceof File) {
                    value = new FileInputStream(File.class.cast(value));
                }
                //
                if (value instanceof InputStream) {
                    dos.write((key + "=").getBytes());
                    InputStream is = InputStream.class.cast(value);
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    dos.write(URLEncoder.encode(Base64.encodeToString(data, data.length), "UTF-8").getBytes());
                    //
                    is.close();
                } else { //
                    dos.write((key + "=" + URLEncoder.encode(String.valueOf(value), "UTF-8")).getBytes());
                }
                //
                dos.write("&".getBytes());
            }
        }
        //
        dos.flush();
        dos.close();
        //
        int resultCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        if (resultCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
        } else {
            response.append(resultCode);
        }
        return response.toString();
    }
}
