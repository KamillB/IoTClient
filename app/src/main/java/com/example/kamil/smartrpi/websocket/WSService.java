package com.example.kamil.smartrpi.websocket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;


public class WSService extends Service {
    private boolean shouldContunue = true;
    private final String WS_URL = "ws://echo.websocket.org";
    private static WSService instance = null;
    private OkHttpClient client;
    Request request;
    EchoWebSocketListener listener;
    WebSocket ws;
    Callbacks activity;
    private final IBinder mBinder = new LocalBinder();
    Handler handler = new Handler();

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
            System.out.println("bindservice");
            return WSService.this;
        }
    }

    //Here Activity register to the service as Callbacks client
    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate(){
        instance = this;
        request = new Request.Builder().url(WS_URL).build();
        listener = new EchoWebSocketListener();
        client = new OkHttpClient();
        Toast.makeText(this, "Service onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //websocket connection
        ws = client.newWebSocket(request, listener);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        shouldContunue = false;
        instance = null;
        Toast.makeText(this, "Service onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    public void doSomethingFirst() {
        handler.postDelayed(serviceRunnable, 0);
        System.out.println("DOING SOMETHING FIRST LALALALALALALALALAL");
        Double n = Math.random();
        ws.send(n.toString());
    }


    //Interface for activity
    public interface Callbacks {
        void updateClient(Integer data);
        void changeBtnName(String name);
    }
}
