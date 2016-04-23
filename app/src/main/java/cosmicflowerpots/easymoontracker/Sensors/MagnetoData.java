package cosmicflowerpots.easymoontracker.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yonseca on 23/04/16.
 */
public class MagnetoData extends AppCompatActivity {

    private SensorManager mSensorManager;

    public MagnetoData(Context context) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            //SensorManager.getOrientation()
        }
        else {
            // Failure! No magnetometer.
        }


    }
}
