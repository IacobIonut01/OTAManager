package splusteam.center.ota.ota_center;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kosalgeek.asynctask.AsyncResponse;

public class DownloadUpdates extends AppCompatActivity {

    Button startDownload = (Button) findViewById(R.id.btnDownload);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_updates);
        startDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String servicestring = Context.DOWNLOAD_SERVICE;
                DownloadManager downloadmanager;
                downloadmanager = (DownloadManager) getSystemService(servicestring);
                Uri uri = Uri
                        .parse("https://sites.google.com/site/compiletimeerrorcom/android-programming/oct-2013/DownloadManagerAndroid1.zip");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                Long reference = downloadmanager.enqueue(request);
            }
        });
    }

}
