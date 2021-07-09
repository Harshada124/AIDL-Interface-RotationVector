package com.example.android.tessrecttask2;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

public class OrientationService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private static final int SENSOR_DELAY = 8 * 1000;
    private float[] rotMat = new float[9];
    private float[] vals = new float[3];
    private IServiceAidlInterface iServiceAidlInterface;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == rotationSensor.getType()){
            SensorManager.getRotationMatrixFromVector(rotMat,
                    event.values);
            SensorManager
                    .remapCoordinateSystem(rotMat,
                            SensorManager.AXIS_X, SensorManager.AXIS_Y,
                            rotMat);
            SensorManager.getOrientation(rotMat, vals);

            vals[0] = (float) Math.toDegrees(vals[0]);
            vals[1] = (float) Math.toDegrees(vals[1]);
            vals[2] = (float) Math.toDegrees(vals[2]);
            System.out.println(" Yaw: " + vals[0] + "\n Pitch: "
                    + vals[1] + "\n Roll (not used): "
                    + vals[2]);

            iServiceAidlInterface = IServiceAidlInterface.Stub.asInterface(binder);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private final IServiceAidlInterface.Stub binder = new IServiceAidlInterface.Stub() {

        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }

        @Override
        public float[] getOrientationData() throws RemoteException {
            float[] data = new float[3];
            data[0] = vals[0];
            data[1] = vals[1];
            data[2] = vals[2];
            return data;
        }
    };

}
