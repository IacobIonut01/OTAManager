package splusteam.center.ota.ota_center;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    public void openInformations(View view) {
        Uri uri = Uri.parse("https://www.facebook.com/iacobionut2003");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



}
