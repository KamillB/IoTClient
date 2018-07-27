package com.example.kamil.smartrpi.websocket;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.smartrpi.BuildConfig;
import com.example.kamil.smartrpi.R;
import com.example.kamil.smartrpi.models.MessageWS;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import com.example.kamil.smartrpi.models.*;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class WSService extends Service {
    private boolean shouldContunue = true;
    //private final String WS_URL = "wss://echo.websocket.org/";
    private final String WS_URL = BuildConfig.SERVER_URL + "wsClient";
    private static WSService instance = null;
    private OkHttpClient client;
    private Request request;
    private EchoWebSocketListener listener;
    private WebSocket ws;
    private Callbacks activity;
    private final IBinder mBinder = new LocalBinder();
    private Handler handler = new Handler();
    private Gson gson = new Gson();
    private String sessionKey;

    //check if service exists
    public static boolean isInstanceCreated(){
        return instance != null;
    }

    //runnable
    Runnable serviceRunnable = new Runnable(){
        @Override
        public void run() {
            if (shouldContunue) {
                Double n = Math.random();
                activity.changeBtnName(n.toString());
                handler.postDelayed(this, 1000);
            }
        }
    };

    //returns the instance of the service
    public class LocalBinder extends Binder{
        public WSService getServiceInstance(){
            return WSService.this;
        }
    }

    //Here Activity register to the service as Callbacks client
    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onCreate(){
        instance = this;
        request = new Request.Builder().url(WS_URL).build();
        listener = new EchoWebSocketListener();
        client = new OkHttpClient();

        Toast.makeText(this, "Service onCreate" + this.client, Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //websocket connection
        sessionKey = intent.getExtras().get("UUID_KEY").toString();
        System.out.println("session key is " + sessionKey);
        ws = client.newWebSocket(request, listener);

        WsMessage message = new WsMessage(sessionKey, "initialMessage", new Payload());
        ws.send(gson.toJson(message));

        return START_STICKY;
    }


    @Override
    public void onDestroy(){
        shouldContunue = false;
        instance = null;
        ws.close(1001,"");
        Toast.makeText(this, "Service onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    public void sensorPageRefresh() {
        handler.postDelayed(serviceRunnable, 0);

        WsMessage message = new WsMessage(sessionKey, "refresh", new Payload());
        ws.send(gson.toJson(message));

        Toast.makeText(this, "MSG SENT", Toast.LENGTH_LONG).show();
    }


    //Interface for activity
    public interface Callbacks {
        void updateClient(Integer data);
        void changeBtnName(String name);
    }
}
