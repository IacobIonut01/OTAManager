package splusteam.center.ota.ota_center;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static splusteam.center.ota.ota_center.MainActivity.device;
import static splusteam.center.ota.ota_center.MainActivity.file;
import static splusteam.center.ota.ota_center.MainActivity.id;
import static splusteam.center.ota.ota_center.MainActivity.versiune;

public class jsonpopup extends AppCompatActivity {
    private String myJSONString;
    private JSONArray users = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonpopup);
        TextView tv1 = (TextView) findViewById(R.id.textbuild);
        String str = Build.VERSION.RELEASE;
        tv1.setText(str);

            TextView textjson = (TextView) findViewById(R.id.json);
            Log.e("JSON", "Text");
            //  showJSON(str);
                JSONObject jsonObject=null;

                //Log.e("JSON", str);
                try {
                    jsonObject = new JSONObject(str);
                    users = jsonObject.getJSONArray("success");
                    JSONObject jo = users.getJSONObject(0);
                    id = jo.getString("id");
                    device = jo.getString("device");
                    versiune = jo.getString("versiune");
                    file = jo.getString("file");

                    textjson.setText(device);


                } catch (final JSONException e) {
                    e.printStackTrace();
                    textjson.setText("Nu exista actualizare!");

                }

                //   Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

}

