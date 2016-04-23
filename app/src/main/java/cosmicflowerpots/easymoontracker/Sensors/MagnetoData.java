/*
package cosmicflowerpots.easymoontracker.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yonseca on 23/04/16.
 */ /*
public class MagnetoData extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor accelerometer = null;
    private Sensor magnetometer = null;
    private float[] rotationMatrix;
    private float[] accelerometerValues;
    private float[] magnetometerValues;
    private static float[] orientation;

    public MagnetoData(Context context) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null && magnetometer != null){
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerValues = event.values;
                break;
        }
        SensorManager.getRotationMatrix(this.rotationMatrix, null, accelerometerValues, magnetometerValues);
        SensorManager.getOrientation(this.rotationMatrix, this.orientation);
    }

    public float[] getOrientation(){
        return orientation;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
*/