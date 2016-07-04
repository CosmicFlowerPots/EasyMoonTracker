package cosmicflowerpots.easymoontracker;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Position extends AppCompatActivity {
    static AsynctaskJSON proof = new AsynctaskJSON();
    static String rawData;

    public static void requestData(String phase, double azimuth, double altitude)
    {
        proof.execute();
        try {
            rawData = proof.get();
            JSONDataParse(rawData, phase, azimuth, altitude);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void JSONDataParse(String rawData, String phase, double azimuth, double altitude) {

        try {
            JSONArray jsonArray = new JSONArray(rawData);

            for(int i=0; i < jsonArray.length(); i++) {

                JSONObject jObject = jsonArray.getJSONObject(i);

                phase = jObject.getString("phase");
                altitude = jObject.getDouble("altitude");
                azimuth = jObject.getDouble("azimuth");

            } // End Loop

        } catch (JSONException e) {

            Log.e("JSONException", "Error: " + e.toString());
        }

    }
}