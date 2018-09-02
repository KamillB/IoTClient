package com.example.kamil.smartrpi.websocket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.kamil.smartrpi.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import com.example.kamil.smartrpi.models.messages.ImageModel;
import com.example.kamil.smartrpi.models.data.ImageSensor;
import com.example.kamil.smartrpi.models.data.Sensor;
import com.example.kamil.smartrpi.models.messages.TemperatureModel;
import com.example.kamil.smartrpi.models.data.TemperatureSensor;
import com.example.kamil.smartrpi.models.messages.Payload;
import com.example.kamil.smartrpi.models.messages.WsMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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


    public static boolean isInstanceCreated(){
        return instance != null;
    }

    //runnable
    Runnable serviceRunnable = new Runnable(){
        @Override
        public void run() {
            if (shouldContunue) {
                System.out.println("this is running");
                Double n = Math.random();
                activity.changeBtnName(n.toString());
                handler.postDelayed(this, 1000);
            }
        }
    };

    // Handle incomming websocket data
    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private WebSocket websocket;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("WebSocket onOpen");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            final WsMessage msg = gson.fromJson(text,WsMessage.class);
            System.out.println("TYPE " + msg.getType());
            System.out.println("WS RECEIVED " + text);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch(msg.getType()){
                        case "sensorList" :
                            updateSensorList(msg.getPayload());
                            break;
                        case "getTemperatureGraphData" :
                            break;
                    }
                }
            });
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println(bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("WS ONCLOSING CODE " + code + " REASON " + reason);
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("WS ON FAILURE " + t.getMessage() + " RESPONSE " + response);
        }
    }


    public void updateSensorList(Payload payload){
        final List<ImageModel> iModel = payload.getImageModel();
        final List<TemperatureModel> tModel = payload.getTemperatureModel();
        final List<Sensor> sensors = new ArrayList<>();
        handler.post(new Runnable() {
            @Override
            public void run() {
            List<ImageSensor> iSensor = new ArrayList<>();
            for (ImageModel i : iModel){
                iSensor.add(new ImageSensor(
                        i.getName(),
                        i.getOwner(),
                        android.util.Base64.decode(i.getImage(), android.util.Base64.DEFAULT),
                        i.getMilis()
                ));
            }
            List<TemperatureSensor> tSensor = new ArrayList<>();
            for (TemperatureModel t : tModel){
                tSensor.add(new TemperatureSensor(
                        t.getName(),
                        t.getOwnerSerialNumber(),
                        t.getTemp(),
                        t.getMilis()
                ));
            }
            sensors.addAll(iSensor);
            sensors.addAll(tSensor);
            }
        });
            activity.updateRecyclerView(sensors);
    }



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
        ws.close(1000,"closeService");
        Toast.makeText(this, "Service onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    public void getAllSensors() {
        WsMessage message = new WsMessage(sessionKey, "refresh", new Payload());
        ws.send(gson.toJson(message));
    }

    public void addNewDevice(String deviceKey){
        Payload payload = new Payload();
        payload.setDeviceKey(deviceKey);
        WsMessage message = new WsMessage(sessionKey, "addDevice", payload);
        ws.send(gson.toJson(message));
    }

    public void getSensorData(){
        Payload payload = new Payload();
        WsMessage message = new WsMessage(sessionKey, "i want i all and i want it now!", payload);
        ws.send(gson.toJson(message));
    }





    ///////////////////////////// JUST FOR TEST ///////////////////
    public void sensorPageRefresh() {
        handler.postDelayed(serviceRunnable, 0);

        WsMessage message = new WsMessage(sessionKey, "refresh", new Payload());
        ws.send(gson.toJson(message));

        Toast.makeText(this, "MSG SENT", Toast.LENGTH_LONG).show();
    }
    ///////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    //Interface for activity
    public interface Callbacks {
        void updateClient(Payload payload);
        void changeBtnName(String name);
        void updateRecyclerView(List<Sensor> sensory);
    }
}
