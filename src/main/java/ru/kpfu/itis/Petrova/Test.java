package ru.kpfu.itis.Petrova;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws IOException {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        params.put("", "");
        params.put("", "");

        System.out.println(new ClientImpl().get("https://postman-echo.com/get",
                headers, params));
        System.out.println(new ClientImpl().post("https://postman-echo.com/post",
                headers, params));
    }


}
