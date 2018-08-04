package com.example.kamil.smartrpi;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.kamil.smartrpi.adapter.SensorAdapter;
import com.example.kamil.smartrpi.models.*;
import com.example.kamil.smartrpi.websocket.WSService;

import java.util.ArrayList;
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
            //TODO popup window with informations about app
            return true;
        }
        if (id == R.id.action_log_out){
            //TODO logout user
            return true;
        }
        if (id == R.id.action_add_device){
            addDevice();
            //TODO add device to user
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    public void run(){
        //Execute some commands
        wsService.sensorPageRefresh();
    }

    @Override
    public void updateClient(Payload payload){
    }

    @Override
    public void changeBtnName(String name){
    }

    @Override
    public void updateRecyclerView(final List<Sensor> sensory){
//        sensors = new ArrayList<>();
//        for (Sensor s : sensory){
//            sensors.add(s);
//            sensorAdapter.notifyItemInserted(sensors.size()-1);
//
//            if (sensor instanceof TemperatureSensor) {
//                System.out.println("yea");
//            } else {
//                System.out.println("nope");
//            }



        handler.post(new Runnable() {
            @Override
            public void run() {
                sensors = new ArrayList<>();
                sensorAdapter = new SensorAdapter(sensors, recyclerView);
                recyclerView.setAdapter(sensorAdapter);
                sensorAdapter.notifyDataSetChanged();
                for (Sensor s : sensory){
                    sensors.add(s);
                    System.out.println("item count " +sensorAdapter.getItemCount());
                    System.out.println("kafelek " + s.getName() + s.getOwnerDevice() + ((TemperatureSensor)s).getTemp() );
                    sensorAdapter.notifyItemInserted(sensors.size()-1);
                }
            }
        });

    }

    public void addToRecyclerView(Device device){
        if (!device.getSensorList().isEmpty()){
            sensors.addAll(device.getSensorList());
            recyclerView.setAdapter(new SensorAdapter(sensors, recyclerView));
        }
    }

    public void addDevice(){
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

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        stopService(serviceIntent);
        System.out.println("main activity on destroy called");
        super.onDestroy();
        getDelegate().onDestroy();
    }

}