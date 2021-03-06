package com.example.kamil.smartrpi;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kamil.smartrpi.adapter.SensorAdapter;
import com.example.kamil.smartrpi.dialog.TemperatureGraphDialog;
import com.example.kamil.smartrpi.models.data.ImageSensor;
import com.example.kamil.smartrpi.models.data.PeripherySensor;
import com.example.kamil.smartrpi.models.data.Sensor;
import com.example.kamil.smartrpi.models.data.TemperatureSensor;
import com.example.kamil.smartrpi.models.messages.ImageModel;
import com.example.kamil.smartrpi.models.messages.Payload;
import com.example.kamil.smartrpi.models.messages.PeripheryModel;
import com.example.kamil.smartrpi.models.messages.TemperatureModel;
import com.example.kamil.smartrpi.websocket.WSService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements WSService.Callbacks {
    private String loggingSessionKey;
    private Toolbar toolbar;
    private Intent serviceIntent;
    private WSService wsService;
    private ArrayList<Sensor> sensors = new ArrayList<>();
    private SensorAdapter sensorAdapter = new SensorAdapter();
    @Bind(R.id.recycler) RecyclerView recyclerView;
    Handler handler = new Handler();

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            WSService.LocalBinder binder = (WSService.LocalBinder) service;
            wsService = binder.getServiceInstance();
            wsService.registerClient(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        loggingSessionKey = getIntent().getStringExtra("UUID_KEY");
        System.out.println(loggingSessionKey);

        //TOP BAR MENU
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //SERVICE
        serviceIntent = new Intent(MainActivity.this, WSService.class);
        serviceIntent.putExtra("UUID_KEY", loggingSessionKey);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        // RECYCLER VIEW
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sensorAdapter.setmSensors(sensors);
        sensorAdapter.setmRecyclerView(recyclerView);
        recyclerView.setAdapter(sensorAdapter);

    }

    public void startWsService(){
        //Start and bind to service
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopWsService(){
        //Unbind from service and destroy
        unbindService(mConnection);
        stopService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            stopWsService();
            startWsService();
            wsService.getAllSensors();
            return true;
        }
        if (id == R.id.action_about){
            menuAbout();
            return true;
        }
        if (id == R.id.action_log_out){
            menuLogout();
            return true;
        }
        if (id == R.id.action_add_device){
            menuAddDevice();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void run(){
        //Execute some commands
        wsService.sensorPageRefresh();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        stopService(serviceIntent);
        System.out.println("main activity on destroy called");
        super.onDestroy();
        getDelegate().onDestroy();
    }

    @Override
    public void updateClient(Payload payload) {

        //| | !payload.getTemperatureModel().isEmpty()
        if (payload.getTemperatureModel() != null){
            for (Integer i=0; i<sensors.size(); i++){
                if (sensors.get(i).getName().equals(payload.getTemperatureModel().get(0).getName())){
                    ((TemperatureSensor) sensors.get(i)).setMilis(payload.getTemperatureModel().get(0).getMilis());
                    ((TemperatureSensor) sensors.get(i)).setTemp(payload.getTemperatureModel().get(0).getTemp());
                    sensorAdapter.notifyDataSetChanged();
                }
            }
        }
        // || !payload.getImageModel().isEmpty()
        else if (payload.getImageModel() != null){
            for (Integer i=0; i<sensors.size(); i++){
                if (sensors.get(i).getName().equals(payload.getImageModel().get(0).getName())){
                    ((ImageSensor) sensors.get(i)).setMilis(payload.getImageModel().get(0).getMilis());
                    ((ImageSensor) sensors.get(i)).setImage(
                            android.util.Base64.decode(payload.getImageModel().get(0).getImage(), android.util.Base64.DEFAULT)
                    );
                    sensorAdapter.notifyDataSetChanged();
                }
            }
        }
        // || !payload.getPeripheryModels().isEmpty()
        else if (payload.getPeripheryModels() != null){
            for (Integer i=0; i<sensors.size(); i++){
                if (sensors.get(i).getName().equals(payload.getPeripheryModels().get(0).getName())){
                    ((PeripherySensor) sensors.get(i)).setMilis(payload.getPeripheryModels().get(0).getMilis());
                    ((PeripherySensor) sensors.get(i)).setStatus(payload.getPeripheryModels().get(0).getStatus());
                    sensorAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void changeBtnName(String name){
    }

    @Override
    public void plotTemperatureGraph(List<TemperatureModel> temperatures){
        if (!temperatures.isEmpty()) {
            if (temperatures.size() > 50) {
                temperatures = temperatures.subList(Math.max(temperatures.size() - 50, 0), temperatures.size());
            }
            Collections.sort(temperatures, new Comparator<TemperatureModel>() {
                @Override
                public int compare(TemperatureModel o1, TemperatureModel o2) {
                    return o1.getMilis().compareTo(o2.getMilis());
                }
            });
            TemperatureGraphDialog tgdialog = new TemperatureGraphDialog(MainActivity.this, temperatures);
            tgdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            tgdialog.show();
        }
    }

    @Override
    public void updateRecyclerView(final List<Sensor> sensory){
        handler.post(new Runnable() {
            @Override
            public void run() {
                sensors = new ArrayList<>();
                sensorAdapter = new SensorAdapter(sensors, recyclerView, wsService, MainActivity.this);
                recyclerView.setAdapter(sensorAdapter);
                sensorAdapter.notifyDataSetChanged();
                for (Sensor s : sensory){
                    sensors.add(s);
                    sensorAdapter.notifyItemInserted(sensors.size()-1);
                }
            }
        });
    }

    public void menuAddDevice(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        alert.setTitle("Add device");
        alert.setMessage("Type in the access key of your device.");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                wsService.addNewDevice(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Back to the main view
            }
        });

        alert.show();
    }

    public void menuAbout(){
        String msg = "Smart home application created by Kamil Bajdo. More informations can be found at \n" +
                " www.github.com/KamillB \n" +
                "\n " +
                " Icons taken from website www.shareicon.net";
        final SpannableString s = new SpannableString(msg);
        Linkify.addLinks(s, Linkify.ALL);

        final AlertDialog alert = new AlertDialog.Builder(this)
                .setPositiveButton("OK", null)
                .setTitle("About")
                .setMessage(s)
                .create();

        alert.show();

        ((TextView) alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void menuLogout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}