package com.example.kamil.smartrpi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import com.example.kamil.smartrpi.MainActivity;
import com.example.kamil.smartrpi.R;
import com.example.kamil.smartrpi.models.messages.TemperatureModel;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TemperatureGraphDialog extends Dialog {
    public Activity c;
    public Dialog d;
    private List<TemperatureModel> temperatures;

    public TemperatureGraphDialog(Activity a, List<TemperatureModel> temperatures){
        super(a);
        this.c = a;
        this.temperatures = temperatures;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.graph_dialog);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (TemperatureModel t : temperatures){
            series.appendData(new DataPoint(
                    new Date(t.getMilis()),
                    t.getTemp()
            ),true, 50);
        }
        graph.addSeries(series);
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) value);
                    return (dateFormat.format(d));
                } else {
                    return "" + (int) value;
                }
            }
        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        graph.getViewport().setMinY(0);
        graph.getViewport().setMinX(temperatures.get(0).getMilis());
        graph.getViewport().setMaxX(temperatures.get(temperatures.size()-1).getMilis());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");


    }
}
