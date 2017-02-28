package splusteam.center.ota.ota_center;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OfficialCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_check);

                TextView sdk = (TextView) findViewById(R.id.textsdk);
                sdk.setText(String.format( Utils.getSDK()));

                TextView xposed = (TextView) findViewById(R.id.textxposed);
                xposed.setText(String.format( Utils.getXposedSupport()));

                TextView status = (TextView) findViewById(R.id.textofficial);
                status.setText(String.format( Utils.getOfficialState()));


    }
}
