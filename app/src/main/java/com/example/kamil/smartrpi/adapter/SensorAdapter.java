package com.example.kamil.smartrpi.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kamil.smartrpi.R;
import com.example.kamil.smartrpi.models.data.ImageSensor;
import com.example.kamil.smartrpi.models.data.PeripherySensor;
import com.example.kamil.smartrpi.models.data.Sensor;
import com.example.kamil.smartrpi.models.data.TemperatureSensor;
import com.example.kamil.smartrpi.websocket.WSService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SensorAdapter extends RecyclerView.Adapter {
    private ArrayList<Sensor> mSensors;
    private RecyclerView mRecyclerView;
    private WSService mWsService;
    private Activity activity;
    private Integer THUMBNAIL_WIDTH = 150;
    private Integer THUMBNAIL_HEIGHT = 150;

    public SensorAdapter(){
    }

    public SensorAdapter(ArrayList<Sensor> pSensors, RecyclerView pRecyclerView, WSService mWsService, Activity activity){
        this.mSensors = pSensors;
        this.mRecyclerView = pRecyclerView;
        this.mWsService = mWsService;
        this.activity = activity;
    }

    public void setmSensors(ArrayList<Sensor> mSensors) {
        this.mSensors = mSensors;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setWsService(WSService mWsService) { this.mWsService = mWsService; }

    public void setActivity(Activity activity) { this.activity = activity; }

    private class SensorViewHolder extends RecyclerView.ViewHolder{
        public ImageView mSensorImage;
        public TextView mTitle;
        public TextView mDate;
        public TextView mContentText;
        public ImageView mContentImage;


        public SensorViewHolder(View view){
            super(view);
            mSensorImage = (ImageView) view.findViewById(R.id.sensor_image) ;
            mTitle = (TextView) view.findViewById(R.id.sensor_title);
            mDate = (TextView) view.findViewById(R.id.sensor_date);
            mContentText = (TextView) view.findViewById(R.id.sensor_content_text);
            mContentImage = (ImageView) view.findViewById(R.id.sensor_content_image);
        }
    }

    public void updateList(ArrayList<Sensor> data) {
        mSensors = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_element, parent, false);

        final ViewGroup pt = parent;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                Sensor sensor = mSensors.get(position);
                if (sensor instanceof TemperatureSensor){
                    mWsService.plotTemperatureGraph(sensor);
                    // do something with temperature
                }
                else if (sensor instanceof  ImageSensor){
                    // do something with image
                    showImageDialog(sensor);
                }
                else if (sensor instanceof PeripherySensor){
                    mWsService.switchPeripheryStatus(sensor);
                }
            }
        });

        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        Sensor sensor = mSensors.get(i);
        if (sensor instanceof TemperatureSensor){
            Date date = new Date(((TemperatureSensor)sensor).getMilis());
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
            Resources resources = activity.getResources();
            Integer imageIdentifier = resources.getIdentifier("thermometer", "drawable", activity.getPackageName());

            ((SensorViewHolder) viewHolder).mSensorImage.setImageDrawable(resources.getDrawable(imageIdentifier));
            ((SensorViewHolder) viewHolder).mTitle.setText(sensor.getName());
            ((SensorViewHolder) viewHolder).mDate.setText(dateFormat.format(date.getTime()));

            ((SensorViewHolder) viewHolder).mContentText.setText(((TemperatureSensor) sensor).getTemp().toString() + "°C");
            ((SensorViewHolder) viewHolder).mContentImage.setVisibility(View.GONE);
        }
        else if (sensor instanceof ImageSensor){
            Date date = new Date(((ImageSensor)sensor).getMilis());
            Bitmap thumbnailBitmap = BitmapFactory.decodeByteArray(((ImageSensor) sensor).getImage(),
                    0, ((ImageSensor) sensor).getImage().length);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
            Resources resources = activity.getResources();
            Integer imageIdentifier = resources.getIdentifier("camera", "drawable", activity.getPackageName());

            ((SensorViewHolder) viewHolder).mSensorImage.setImageDrawable(resources.getDrawable(imageIdentifier));
            ((SensorViewHolder) viewHolder).mTitle.setText(sensor.getName());
            ((SensorViewHolder) viewHolder).mDate.setText(dateFormat.format(date.getTime()));

            ((SensorViewHolder) viewHolder).mContentText.setVisibility(View.GONE);
            ((SensorViewHolder) viewHolder).mContentImage.setImageBitmap(Bitmap.createScaledBitmap(thumbnailBitmap,
                    THUMBNAIL_WIDTH,THUMBNAIL_HEIGHT,false));
        }
        else if (sensor instanceof PeripherySensor){
            Date date = new Date(((PeripherySensor) sensor).getMilis());
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
            Resources resources = activity.getResources();
            Integer imageIdentifier = resources.getIdentifier("device", "drawable", activity.getPackageName());

            ((SensorViewHolder) viewHolder).mSensorImage.setImageDrawable(resources.getDrawable(imageIdentifier));
            ((SensorViewHolder) viewHolder).mTitle.setText(sensor.getName());
            ((SensorViewHolder) viewHolder).mDate.setText(dateFormat.format(date.getTime()));

            ((SensorViewHolder) viewHolder).mContentImage.setVisibility(View.GONE);
            if (((PeripherySensor) sensor).getStatus() == 1){
                ((SensorViewHolder) viewHolder).mContentText.setText("On");
            }
            else if (((PeripherySensor) sensor).getStatus() == 0){
                ((SensorViewHolder) viewHolder).mContentText.setText("Off");
            }
            else {
                ((SensorViewHolder) viewHolder).mContentText.setText("Unknown");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mSensors.size();
    }

    public void showImageDialog(Sensor sensor){
        final Dialog nagDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.preview_image);
        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);

        byte[] byteImage = ((ImageSensor) sensor).getImage();
        Drawable image = new BitmapDrawable(activity.getResources(),
                BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));

        ivPreview.setBackground(image);
        nagDialog.show();
    }
}
