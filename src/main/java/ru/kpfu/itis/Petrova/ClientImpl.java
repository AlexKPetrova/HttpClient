package ru.kpfu.itis.Petrova;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClientImpl implements HttpClient {

    @Override
    public String get(String url, Map<String, String> headers, Map<String, String> params) {

        try {
            URL urlMain = new URL(createURL(url, params));
            HttpURLConnection connection = (HttpURLConnection) urlMain.openConnection();
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            connection.setRequestMethod("GET");

            System.out.println(connection.getResponseCode());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())
            )) {
                StringBuilder content = new StringBuilder();
                String input;
                while ((input = reader.readLine()) != null) {
                    content.append(input);
                }
                connection.disconnect();
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createURL(String url, Map<String, String> params) {
        int count = 0;
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");

        for(String key: params.keySet()){
            if (count > 0) {
                urlBuilder.append("&");
            }
            urlBuilder.append(key).append("=").append(params.get(key));
            count +=1;
        }
        return urlBuilder.toString();
    }

    @Override
    public String post(String url, Map<String, String> headers, Map<String, String> params) {

        try {
            URL postUrl = new URL(url);
            HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
            postConnection.setRequestMethod("POST");
            for(String key: headers.keySet()){
                postConnection.setRequestProperty(key, headers.get(key));
            }
            postConnection.setDoOutput(true);

            String jsonInputString = createJSON(params);

            try(OutputStream outputStream = postConnection.getOutputStream()){
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input,0,input.length);

            }

            System.out.println(postConnection.getResponseCode());
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(postConnection.getInputStream(),StandardCharsets.UTF_8))) {
                String input;
                while ((input = reader.readLine()) != null) {
                    content.append(input.trim());
                }
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createJSON(Map<String, String> params) {
        int counter = 0;
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for(String key: params.keySet()){
            if(counter > 0){
                jsonBuilder.append(",");
            }
            jsonBuilder.append(key).append(":").append(params.get(key));
            counter +=1;
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
