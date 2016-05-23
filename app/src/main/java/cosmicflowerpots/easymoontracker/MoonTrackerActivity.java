package cosmicflowerpots.easymoontracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MoonTrackerActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    public Camera mCamera;
    AsynctaskJSON proof = new AsynctaskJSON();
    String wololo;
    String phase;
    double azimuth;
    double altitude;
    double rangeAmplitude = 1.0;
    ImageView moonImg;
    Vibrator v;
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private TextView magnetoData;
    private TextView letrero;
    private SensorManager mSensorManager;
    private Sensor accelerometer = null;
    private Sensor magnetometer = null;
    private float[] rotationMatrix = new float[9];
    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    private float[] orientation = {0, 0, 0};
    private float[] orientationAux = {0, 0, 0};
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSensors();

        //moonImg.invalidate();

        getProofData();

        setLayout();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        // Camara
        initCamera();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


    }

    private void setLayout() {
        setContentView(R.layout.activity_moon_tracker);
        moonImg = (ImageView) findViewById(R.id.moonpic);

        mVisible = true;
        magnetoData = (TextView) findViewById(R.id.magnetoDataText);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        letrero = (TextView) findViewById(R.id.letrero);
    }

    private void getProofData() {
        proof.execute();
        try {
            wololo = proof.get();
            JSONDataParse(wololo);
            //text.setText(wololo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void initSensors() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if (accelerometer != null && magnetometer != null) {
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


    }

    public void JSONDataParse(String data) {
//        TextView text = (TextView) findViewById(R.id.moonCrap);
//        TextView textTwo = (TextView) findViewById(R.id.moonCrapTwo);
//        TextView textThree = (TextView) findViewById(R.id.moonCrapThree);
        try {
            JSONArray jArray = new JSONArray(data);
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                phase = jObject.getString("phase");
                altitude = jObject.getDouble("altitude");
                altitude = Math.toRadians(altitude + 90);
                azimuth = jObject.getDouble("azimuth");
                azimuth = Math.toRadians(azimuth + 90);

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerValues = event.values;
                break;
        }

        SensorManager.getRotationMatrix(this.rotationMatrix, null, accelerometerValues, magnetometerValues);
        orientationAux = SensorManager.getOrientation(this.rotationMatrix, this.orientation);
        magnetoData = (TextView) findViewById(R.id.magnetoDataText);
        DecimalFormat formatoDecimal = new DecimalFormat("###.##");
        Double oriHorz = Math.toDegrees(orientation[0]);
        Double oriVer = Math.toDegrees(orientation[1]);
        magnetoData.setText("H:" + String.valueOf(formatoDecimal.format(oriHorz))
                + "\n" +
                "V: " + String.valueOf(formatoDecimal.format(oriVer)));

        onSensorChangedRationally();
    }

    public void onSensorChangedRationally() {

        if ((double) orientation[0] > azimuth - rangeAmplitude &&
                (double) orientation[0] < azimuth + rangeAmplitude &&
                (double) orientation[1] > altitude - rangeAmplitude &&
                (double) orientation[1] < altitude + rangeAmplitude) {

            letrero.setText("¡Ahí está!");
            //ImageView moonImg = (ImageView) findViewById(R.id.moonpic);
            moonImg.setImageResource(R.drawable.bigmoon);
            moonImg.bringToFront();
            v.vibrate(500);
        } else {
            letrero.setText("¡Sigue buscando!");
            moonImg.setImageResource(0);
        }

        //letrero = (TextView) findViewById(R.id.letrero);
//        letrero.setText("Acimut:" + String.valueOf(azimuth)
//                + "\n" +
//                "Elevación: " + String.valueOf(altitude));

    }

    private void initCamera() {
        if (Camera.getNumberOfCameras() > 0) {
            mCamera = Camera.open(0);
            try {
                //mCamera.cancelAutoFocus();
                Camera.Parameters params = mCamera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(params);
            } catch (RuntimeException a) {

            }
            try {

                mCamera.setDisplayOrientation(90);
            } catch (NullPointerException a) {
                mCamera.setDisplayOrientation(90);
            }

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error :(")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float[] getOrientation() {
        return orientation;
    }
}
