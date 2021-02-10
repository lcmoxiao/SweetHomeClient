package com.banmo.sweethomeclient.client.tool;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpTools {

    private final static String preUrl = "http://192.168.0.107/";
    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url){
        Request request = new Request.Builder()
                .url(preUrl + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String post(String url, String json) throws IOException {

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody stringBody = RequestBody.create(json, mediaType);

        Request request = new Request
                .Builder()
                .url(preUrl + url)
                .post(stringBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String put(String url, String json) {

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody stringBody = RequestBody.create(json, mediaType);

        Request request = new Request
                .Builder()
                .url(preUrl + url)
                .put(stringBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String delete(String url, String json) {

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody stringBody = RequestBody.create(json, mediaType);

        Request request = new Request
                .Builder()
                .url(preUrl + url)
                .delete(stringBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
