package cosmicflowerpots.easymoontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.lalotone.moon.asynctask.AsynctaskJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Position extends AppCompatActivity {
    AsynctaskJSON proof = new AsynctaskJSON();
    String rawData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proof.execute();
        try {
            rawData = proof.get();
            JSONDataParse(rawData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void JSONDataParse(String data) {
        TextView text = (TextView) findViewById(R.id.moonCrap);
        TextView textTwo = (TextView) findViewById(R.id.moonCrapTwo);
        TextView textThree = (TextView) findViewById(R.id.moonCrapThree);
        try {
            JSONArray jArray = new JSONArray(data);

            for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String phase = jObject.getString("phase");
                text.setText(phase);
                String altitude = jObject.getString("altitude");
                textTwo.setText(altitude);
                String azimuth = jObject.getString("azimuth");
                textThree.setText(azimuth);

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
    }
    /*try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String name = jObject.getString("phase");
                String tab1_text = jObject.getString("altitude");
                int active = jObject.getInt("azimuth");

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }*/
}