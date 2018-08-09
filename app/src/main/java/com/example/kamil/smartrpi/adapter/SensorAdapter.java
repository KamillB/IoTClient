package com.example.kamil.smartrpi.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.smartrpi.R;
import com.example.kamil.smartrpi.models.ImageSensor;
import com.example.kamil.smartrpi.models.Sensor;
import com.example.kamil.smartrpi.models.TemperatureSensor;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class SensorAdapter extends RecyclerView.Adapter {
    private ArrayList<Sensor> mSensors;
    private RecyclerView mRecyclerView;
    private Integer THUMBNAIL_WIDTH = 120;
    private Integer THUMBNAIL_HEIGHT = 120;

    public SensorAdapter(){
    }

    public SensorAdapter(ArrayList<Sensor> pSensors, RecyclerView pRecyclerView){
        this.mSensors = pSensors;
        this.mRecyclerView = pRecyclerView;
    }

    public void setmSensors(ArrayList<Sensor> mSensors) {
        this.mSensors = mSensors;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    private class SensorViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mContent;
        public ImageView mImage;

        public SensorViewHolder(View view){
            super(view);
            mTitle = (TextView) view.findViewById(R.id.sensor_title);
            mContent = (TextView) view.findViewById(R.id.sensor_content);
            mImage = (ImageView) view.findViewById(R.id.sensor_image);
        }
    }

    public void updateList(ArrayList<Sensor> data) {
        mSensors = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_element, parent, false);

        final ViewGroup pt = parent;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                mSensors.get(position).getName();
                Toast.makeText(pt.getContext(), mSensors.get(position).getName(), Toast.LENGTH_LONG).show();

            }
        });

        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        Sensor sensor = mSensors.get(i);
        if (sensor instanceof TemperatureSensor){
            Date date = new Date(((TemperatureSensor)sensor).getMilis());

            ((SensorViewHolder) viewHolder).mTitle.setText("temperature " + sensor.getName());
            ((SensorViewHolder) viewHolder).mContent.setText(date.toString());
        }
        if (sensor instanceof ImageSensor){
            Date date = new Date(((ImageSensor)sensor).getMilis());
            Bitmap thumbnailBitmal = BitmapFactory.decodeByteArray(((ImageSensor) sensor).getImage(), 0, ((ImageSensor) sensor).getImage().length);

            ((SensorViewHolder) viewHolder).mTitle.setText("photo " + sensor.getName());
            ((SensorViewHolder) viewHolder).mContent.setText(date.toString());
            ((SensorViewHolder) viewHolder).mImage.setImageBitmap(Bitmap.createScaledBitmap(thumbnailBitmal, THUMBNAIL_WIDTH,THUMBNAIL_HEIGHT,false));
        }

    }

    @Override
    public int getItemCount() {
        return mSensors.size();
    }
}
