package com.example.android.tessrecttask2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private static final int SENSOR_DELAY = 8 * 1000;
    private float[] rotMat = new float[9];
    private float[] vals = new float[3];
    private TextView textView;
    private Button button;
    private IServiceAidlInterface aidlInterfaceService = null;
    private boolean isBound;
   // private InternalHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.txtvw);
        button = findViewById(R.id.btn);
       // button.setOnClickListener(mBindListener);

        // connection.onServiceConnected(,aidlInterfaceService);


        try {
            sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sensorManager.registerListener(this, rotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Log.i("Task2","sensor exception : "+e);
            Toast.makeText(MainActivity.this,"Hardware not compatible",Toast.LENGTH_SHORT).show();

        }


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
            textView.setText(" Yaw: " + vals[0] + "\n Pitch: "
                    + vals[1] + "\n Roll (not used): "
                    + vals[2]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
    /*
    private View.OnClickListener mBindListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Establish a couple connections with the service, binding
            // by interface names.  This allows other applications to be
            // installed that replace the remote service by implementing
            // the same interface.
            Intent intent = new Intent(MainActivity.this, ServiceConnection.class);
            intent.setAction(IServiceAidlInterface.class.getName());
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
            isBound = true;

        }
    };

    private View.OnClickListener unbindListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (isBound) {
                // If we have received the service, and hence registered with
                // it, then now is the time to unregister.
                if (aidlInterfaceService != null) {
                    try {
                     //   aidlInterfaceService.unregisterCallback(mCallback);
                    } catch (Exception e) {
                        // There is nothing special we need to do if the service
                        // has crashed.
                    }
                }

                // Detach our existing connection.
                unbindService(connection);
                isBound = false;
                //callbackText.setText("Unbinding.");
            }
        }
    };

    private static final int BUMP_MSG = 1;

    private static class InternalHandler extends Handler {
        private final WeakReference<TextView> weakTextView;

        InternalHandler(TextView textView) {
            weakTextView = new WeakReference<>(textView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BUMP_MSG:
                    TextView textView = weakTextView.get();
                    if (textView != null) {
                        textView.setText("Received from service: " + msg.arg1);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {
                aidlInterfaceService = IServiceAidlInterface.Stub.asInterface(service);
                float[] data = aidlInterfaceService.getOrientationData();
                Log.i("Task1"," service connected : "+data[0]);
                textView.setText("yaw : "+data[0]);

                Toast.makeText(MainActivity.this,"Service Connected",Toast.LENGTH_SHORT).show();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlInterfaceService = null;
        }
    };
     */
