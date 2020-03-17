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

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private float accelerometerZAxis, gyroscopeZAxis;
    private LinkedList<Integer> accelerometerValueList, gyroscopeValueList;
    private TextView ForceValue, AngleValue, x, y, z;
    private boolean isPressing;

    private final int accelerometerType = Sensor.TYPE_ACCELEROMETER;
    private final int angleType = Sensor.TYPE_ROTATION_VECTOR;
    //fastest:0.01 game:0.02 normal:0.20 ui:~0.65
    private final int accelerometerSensorSpeed = SensorManager.SENSOR_DELAY_GAME;
    private final int gyroscopeSensorSpeed = SensorManager.SENSOR_DELAY_GAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = senSensorManager.getDefaultSensor(accelerometerType);
        Sensor gyroscopeSensor = senSensorManager.getDefaultSensor(angleType);
        if (accelerometerSensor != null) {
            senSensorManager.registerListener(accelerometerSensorListener, accelerometerSensor, accelerometerSensorSpeed);
            Log.d("OK", "Accelerometer listening");
        } else {
            Log.d("ERROR", "Accelerometer not listening");
        }
        if (gyroscopeSensor != null) {
            senSensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, gyroscopeSensorSpeed);
            Log.d("OK", "Gyroscope listening");
        } else {
            Log.d("ERROR", "Gyroscope not listening");
        }
        ForceValue = findViewById(R.id.txtMiddleForceValue);
        AngleValue = findViewById(R.id.txtMiddleAngleValue);
        x = findViewById(R.id.txtX);
        y = findViewById(R.id.txtY);
        z = findViewById(R.id.txtZ);
        accelerometerValueList = new LinkedList<>();
        gyroscopeValueList = new LinkedList<>();
        isPressing = false;
    }

    private SensorEventListener accelerometerSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == accelerometerType) {
                accelerometerZAxis = event.values[2];
                if (isPressing) {
                    accelerometerValueList.add((int) accelerometerZAxis);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == angleType) {
                gyroscopeZAxis = event.values[2];
                x.setText("x:" + String.format("%.2f", event.values[0]));
                y.setText("y:" + String.format("%.2f", event.values[1]));
                z.setText("z:" + String.format("%.2f", event.values[2]));
                if (isPressing) {
                    gyroscopeValueList.add((int) (gyroscopeZAxis * 100));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            isPressing = true;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            isPressing = false;
            showValues();
        }
        return true;
    }

    private void showValues() {
        ForceValue.setText(Integer.toString(getAccelerometerValuesAverage()));
        AngleValue.setText(Integer.toString(getDifferenceOfTheLastAndFirstGyroscopeValues())+"°");
        accelerometerValueList = new LinkedList<>();
        gyroscopeValueList = new LinkedList<>();
    }

    private int getAccelerometerValuesAverage() {
        int s = 0;
        for (int i = 0; i < accelerometerValueList.size(); i++) {
            s += accelerometerValueList.get(i);
            Log.d("AccelerometerValue[" + i + "]", "" + accelerometerValueList.get(i));
        }
        return accelerometerValueList.size() > 0 ? s / accelerometerValueList.size() : 0;
    }

    private int getGyroscopeValuesAverage() {
        int s = 0;
        for (int i = 0; i < gyroscopeValueList.size(); i++) {
            s += gyroscopeValueList.get(i);
            Log.d("GyroscopeValue[" + i + "]", "" + gyroscopeValueList.get(i));
        }
        return gyroscopeValueList.size() > 0 ? s / gyroscopeValueList.size() : 0;
    }

    private int getDifferenceOfTheLastAndFirstGyroscopeValues() {
        return gyroscopeValueList.get(0) - gyroscopeValueList.get(gyroscopeValueList.size() - 1);
    }
}
