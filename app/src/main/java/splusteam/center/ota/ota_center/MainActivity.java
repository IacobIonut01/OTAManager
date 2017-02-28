package splusteam.center.ota.ota_center;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static splusteam.center.ota.ota_center.R.id.json;
import static splusteam.center.ota.ota_center.R.id.textdevice;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse, View.OnClickListener {

    final Context context = this;
    private DownloadManager downloadManager;
    private long downloadReference;
    PostResponseAsyncTask task = new PostResponseAsyncTask(this);
    private String myJSONString;
    private JSONArray users = null;
    String ab;
    Button button;
    Button button_update;
  //  private DownloadManager downloadManager;

    private long Firmware_DownloadId;

    public static String id;
    public static String device;
    public static String versiune;
    public static String file;
    public static String raspuns;
    public static String r_da="da";
    public static String r_nu="nu";
    public static String r_err="err";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TextView textjson = (TextView) findViewById(json);
        //textjson.setText(" ");

        TextView tv1 = (TextView) findViewById(R.id.textbuild);
        String str = Build.VERSION.RELEASE; //Replace SERIAL with other build strings
        tv1.setText(str);

        TextView tvBuildVer = (TextView) findViewById(R.id.textnumber);
        tvBuildVer.setText(String.format( Utils.getVersion()));

        TextView tv3 = (TextView) findViewById(textdevice);
        String str3 = Build.DEVICE;
        tv3.setText(str3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MainFragment mainfragment = new MainFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace
                (R.id.mainLayout, mainfragment).commit();

        button_update = (Button)findViewById(R.id.button_update);
        button_update.setOnClickListener(this);
    }


    /*public void startdown(View view) {
        Intent intent = new Intent(this, DownloadUpdates.class);
        startActivity(intent);
    }*/

    @Override
    public void processFinish(String str) {
     //   TextView textjson = (TextView) findViewById(R.id.json);
//        Log.e("JSON", "Text"+str);
      //  showJSON(str);

        if (str != "")  {
            JSONObject jsonObject=null;
//            Log.e("JSON", str);
            try {
                jsonObject = new JSONObject(str);
                users = jsonObject.getJSONArray("raspuns");
                JSONObject jo = users.getJSONObject(0);
                raspuns=jo.getString("raspuns");
                Log.e("JSON", "Raspuns: "+raspuns);
                if (raspuns.equals(r_da))
                {
//                    Log.e("JSON", "Sunt cu da "+str);
                    file = jo.getString("file");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Exista o noua actualizare. \nPornesti download-ul?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //     Toast.makeText(MainActivity.this,file,Toast.LENGTH_LONG).show();
                                    Uri firmware_uri = Uri.parse(file);
                                    Firmware_DownloadId = DownloadData(firmware_uri, file);
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
//                            Toast.makeText(MainActivity.this,"You clicked NO button",Toast.LENGTH_LONG).show();
                            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
                            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
                            final TextView progres = (TextView) findViewById(R.id.progres);
                            button_update = (Button)findViewById(R.id.button_update);
                            button_update.setVisibility(View.VISIBLE);
                            progres.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            cancel_update.setVisibility(View.INVISIBLE);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if (raspuns.equals(r_nu))
                {
                    Log.e("JSON", "Sunt cu nu "+str);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("No update found.");
                    alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            //Toast.makeText(MainActivity.this,"You clicked NO button",Toast.LENGTH_LONG).show();
                            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
                            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
                            final TextView progres = (TextView) findViewById(R.id.progres);
                            button_update = (Button)findViewById(R.id.button_update);
                            button_update.setVisibility(View.VISIBLE);
                            progres.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            cancel_update.setVisibility(View.INVISIBLE);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                if (raspuns.equals(r_err))
                {
                    Log.e("JSON", "Sunt cu err "+str);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Error! Ask Developer to fix issues!");
                    alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            //Toast.makeText(MainActivity.this,"You clicked NO button",Toast.LENGTH_LONG).show();
                            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
                            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
                            final TextView progres = (TextView) findViewById(R.id.progres);
                            button_update = (Button)findViewById(R.id.button_update);
                            button_update.setVisibility(View.VISIBLE);
                            progres.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            cancel_update.setVisibility(View.INVISIBLE);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("JSON", "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });
            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
            final TextView progres = (TextView) findViewById(R.id.progres);
            button_update = (Button)findViewById(R.id.button_update);
            button_update.setVisibility(View.VISIBLE);
            progres.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            cancel_update.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
    }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_view) {

        } else if (id == R.id.nav_manage) {
            MainFragment mainfragment = new MainFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace
                    (R.id.mainLayout, mainfragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnSettings_OnClick(MenuItem item) {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        button_update = (Button)findViewById(R.id.button_update);
        button_update.setVisibility(View.INVISIBLE);

        TextView tvBuildVer = (TextView) findViewById(R.id.textnumber);
        tvBuildVer.setText(String.format(Utils.getVersion()));
        TextView tv1 = (TextView) findViewById(R.id.textbuild);
        String str = Build.VERSION.RELEASE;
        tv1.setText(str);
        TextView tv3 = (TextView) findViewById(textdevice);
        String str3 = Build.DEVICE;
        tv3.setText(str3);
        HashMap postData = new HashMap();
        postData.put("btnLogin", "Login");
        postData.put("mobile", "android");
        postData.put("ROMID", tvBuildVer.getText());
        postData.put("AndroidV", tv1.getText());
        postData.put("DevuceID", tv3.getText());
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute("http://ota.geolin.ro/verificare.php");
    }

    private long DownloadData (Uri uri, String file) {

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDescription("OTA Manager Firmware");
        request.setTitle("Downloading Update...");
        request.allowScanningByMediaScanner();

//        request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,file);
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        Log.e("JSON", "Download ID "+downloadId);

        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        final Button cancel_update = (Button) findViewById(R.id.cancel_update);
        final TextView progres = (TextView) findViewById(R.id.progres);
        mProgressBar.setVisibility(View.VISIBLE);
        cancel_update.setVisibility(View.VISIBLE);
        progres.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterByStatus(
                            DownloadManager.STATUS_PAUSED|
                                    DownloadManager.STATUS_PENDING|
                                    DownloadManager.STATUS_RUNNING|
                                    DownloadManager.STATUS_SUCCESSFUL
                    );
                    q.setFilterById(downloadId);
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                   // final double dl_progress = (bytes_downloaded / bytes_total) * 100;
                 //   final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    final int dl_progress = (int) ((double)bytes_downloaded / (double)bytes_total * 100f);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mProgressBar.setProgress((int) dl_progress);
                            progres.setText((int) dl_progress + "%");
                        }
                    });

                    Log.e("Log error:", statusMessage(cursor)+" "+dl_progress);
                    cursor.close();
                }
            }
        }).start();
        return downloadReference;
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        //        CheckDwnloadStatus();

        IntentFilter intentFilter
                = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }


    private String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Toast toast = Toast.makeText(MainActivity.this,
                        "Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
            final TextView progres = (TextView) findViewById(R.id.progres);
            button_update = (Button)findViewById(R.id.button_update);
            button_update.setVisibility(View.VISIBLE);
            progres.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            cancel_update.setVisibility(View.INVISIBLE);


        }
    };



}



