package com.example.mypong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float x, y, z;
    private LinkedList<Integer> valueList;
    private TextView middleForceValue, middleAngleValue, lastForceValue, lastAngleValue;
    private boolean saveValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        middleForceValue = findViewById(R.id.txtMiddleForceValue);
        middleAngleValue = findViewById(R.id.txtMiddleAngleValue);
        lastForceValue = findViewById(R.id.txtLastForceValue);
        lastAngleValue = findViewById(R.id.txtLastAngleValue);
        valueList = new LinkedList<>();
        saveValue = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                if (saveValue) {
                    valueList.add((int) z);
                    Log.d("TIME", "" + event.timestamp / 100000000);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            saveValue = true;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            saveValue = false;
            showValues();
        }
        return true;
    }

    private void showValues() {
        middleForceValue.setText(Integer.toString(getValuesAverage()));
        lastForceValue.setText(Integer.toString((int) z));
        valueList = new LinkedList<>();
    }

    private int getValuesAverage() {
        int s = 0;
        for (int i = 0; i < valueList.size(); i++) {
            s += valueList.get(i);
            Log.d("Value[" + i + "]", "" + valueList.get(i));
        }
        return s / valueList.size();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
