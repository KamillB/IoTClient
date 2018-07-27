package com.example.kamil.smartrpi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kamil.smartrpi.adapter.MyAdapter;
import com.example.kamil.smartrpi.models.Article;
import com.example.kamil.smartrpi.websocket.WSService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WSService.Callbacks {
    private String device_key;
    private Toolbar toolbar;
    private Intent serviceIntent;
    private WSService wsService;
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

        device_key = getIntent().getStringExtra("UUID_KEY");
        System.out.println(device_key);

        //SERVICE
        serviceIntent = new Intent(MainActivity.this, WSService.class);
        serviceIntent.putExtra("UUID_KEY", device_key);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        // RECYCLER VIEW
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            articles.add(new Article());
        }

        recyclerView.setAdapter(new MyAdapter(articles, recyclerView));

        //TOP BAR MENU
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
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
            wsService.sensorPageRefresh();
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
    public void updateClient(Integer data){
    }

    @Override
    public void changeBtnName(String name){
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