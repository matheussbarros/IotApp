package br.com.iotapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SensorAdapter extends ArrayAdapter <Sensor> {



    public SensorAdapter(Context context, List<Sensor> medicoes){
        super(context,-1,medicoes);

    }
}
