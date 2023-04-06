package com.example.lostandfind.fcm;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.TextLinks;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 참고 https://youngest-programming.tistory.com/76
// TODO: sendGson() 채팅 액티비티에 생성
public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String regToken, String title, String messsage){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", messsage);
                    dataJson.put("title", title);
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            // Key값 절대 유출 금지
                            .header("Authorization", "key=" + SecretKey.getFcmKey())
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.d(TAG, "Send Token: "+regToken);
                    Log.d(TAG, "Notification: "+dataJson);
                    Log.d(TAG,"Success Send");
                }catch (Exception e){
                    Log.d("error", e+"");
                }
                return  null;
            }
        }.execute();
    }
}
