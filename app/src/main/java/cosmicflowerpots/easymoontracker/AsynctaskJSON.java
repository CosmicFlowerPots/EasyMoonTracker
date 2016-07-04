package cosmicflowerpots.easymoontracker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsynctaskJSON extends AsyncTask<Void, Void, String> {
    public static String moonCrap;

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection c = null;
        try {
            URL u = new URL("http://blacklo.rbel.co/moonTrack/moon_data_JSON.json");
            c = (HttpURLConnection) u.openConnection();
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    String newString = sb.toString();
                    return newString;
            }

        } catch (Exception ex) {
            return ex.toString();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //disconnect error
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
