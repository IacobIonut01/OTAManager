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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import android.support.v4.content.ContextCompat;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import android.content.pm.PackageManager;
import static splusteam.center.ota.ota_center.R.id.textdevice;
import static splusteam.center.ota.ota_center.R.id.textnumber;

import android.Manifest;

import android.webkit.WebView;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebViewClient;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse, View.OnClickListener {
    private static final int REQUEST_INTERNET = 200;
    private WebView htmlWebView;
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
    public static String file_update;
    public static String raspuns_update;
    public static String file_recovery;
    public static String raspuns_recovery;
    public static String changelog;
    public static String r_da="da";
    public static String r_nu="nu";
    public static String r_not="not";
    public static String r_err="err";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);
        }
        //TextView textjson = (TextView) findViewById(json);
        //textjson.setText(" ");
        TextView tv1 = (TextView) findViewById(R.id.textbuild);
        String str = Build.VERSION.RELEASE; //Replace SERIAL with other build strings
        tv1.setText(str);
        TextView tvBuildVer = (TextView) findViewById(R.id.textnumber);
        tvBuildVer.setText(String.format( Utils.getVersion()));
        String tvBuildVers = tvBuildVer.getText().toString();
        TextView density = (TextView) findViewById(R.id.density);
        density.setText(String.format( Utils.getDensity()));
        TextView chip = (TextView) findViewById(R.id.chip);
        chip.setText(String.format( Utils.getChipset()));
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
        button_update = (Button)findViewById(R.id.button_update);
        button_update.setOnClickListener(this);



        if (tvBuildVers.contains("RR-N-")) {
            ImageView img_rr = (ImageView) findViewById(R.id.imageView2);
            img_rr.setImageResource(R.drawable.ic_rr_logo);
            TextView no_rom = (TextView) findViewById(R.id.no_rom_support);
            no_rom.setVisibility(View.GONE);
        }
        else if (!tvBuildVer.getText().equals("")) {
            ImageView img_rr = (ImageView) findViewById(R.id.imageView2);
            img_rr.setImageResource(R.color.cardview_light_background);
            img_rr.setVisibility(View.GONE);
            TextView no_rom = (TextView) findViewById(R.id.no_rom_support);
            no_rom.setVisibility(View.VISIBLE);
        }
        else if (tvBuildVers.contains("LineageOS-14.1-")) {
            ImageView img_rr = (ImageView) findViewById(R.id.imageView2);
            img_rr.setImageResource(R.drawable.ic_lineageos_logo);
            TextView no_rom = (TextView) findViewById(R.id.no_rom_support);
            no_rom.setVisibility(View.GONE);
        }
        else if (tvBuildVers.contains("AICP-N-")) {
            ImageView img_rr = (ImageView) findViewById(R.id.imageView2);
            img_rr.setImageResource(R.drawable.ic_aicp_logo);
            TextView no_rom = (TextView) findViewById(R.id.no_rom_support);
            no_rom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            Log.e("JSON", "Raspuns cerere permisiune: "+requestCode);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("JSON", "Raspuns cerere permisiune: OK");
                //start audio recording or whatever you planned to do
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Log.e("JSON", "Raspuns cerere permisiune: NOK");
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show an explanation to the user *asynchronously*
                    Log.e("JSON", "Creez dialogul");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Save Updates to SDcard");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Grant Permission to write in sdcard");
                    alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
               }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
   /* public void debug_btn_click(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        TextView tvBuildVer = (TextView) findViewById(R.id.textnumber);
        tvBuildVer.setText(String.format(Utils.getVersion()));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Device & OTA App Informations");
        alertDialogBuilder.setMessage("ROM-Status:\n" +);
        alertDialogBuilder.setPositiveButton("Sure",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog.Builder builder = alertDialogBuilder.setNegativeButton("Maybe later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    @Override
    public void processFinish(String str) {
     //   TextView textjson = (TextView) findViewById(R.id.json);
//        Log.e("JSON", "Text"+str);
      //  showJSON(str);

//  Asta ii rezultatul de la baza de date
//        {"rezultat":[{"raspuns_update":"da","file_update":"http:\/\/ota.geolin.ro\/files\/update\/test.zip","raspuns_recovery":"da","file_recovery":"http:\/\/ota.geolin.ro\/files\/recovery\/test.zip"}]}
// raspuns_update si raspuns_recovery pot primi urmatoarele valori:
//    "da" - daca este update / recovery (s-au gasit in baza de date)
//    "nu" - nu exista fisier de update / recovery (s-a gasit in baza de date device-ul, versiunea, dar nu are nici un fisier atasat
//    "not" - cand nu se gaseste device-ul si versiune in baza de date
//    "err" - cand nu se primeste parametrul "mobile"
//   Ar trebui sa vezi cum faci cu aceste valori
// Pe langa raspuns_... vei mai primi
// file_update si file_recovery
// In ele este link-ul de download. Link-ul va exista doar daca se primeste raspuns_... "da"
// Daca raspuns_... este alta valoare decat da, atunci in file_... nu va fi nimic
// Mai primesti si changelog_upd - este changelog-ul...

// Nu stiu cum sa fac mai departe, sa testez raspunsul si sa fac download daca exista. Te las pe tine la partea asta.
// Vezi ca am mai modificat si la parametrii de trimitere.

        if (str != "")  {
            JSONObject jsonObject=null;
//            Log.e("JSON", str);
            try {
                jsonObject = new JSONObject(str);
                users = jsonObject.getJSONArray("rezultat");
                JSONObject jo = users.getJSONObject(0);
                raspuns_update=jo.getString("raspuns_update");
                changelog=jo.getString("changelog_upd");
                Log.e("JSON", "Raspuns: "+raspuns_update);

                /*if (raspuns_recovery.equals(r_da)){
                    Log.e("JSON", "Recovery cu da"+str);
                    file_recovery = jo.getString("file_recovery");
                    AlertDialog.Builder alertRecovery = new AlertDialog.Builder(this);
                    alertRecovery.setCancelable(false);
                    alertRecovery.setTitle("Update your " + "Recovery ");
                    alertRecovery.setMessage("Install latest recovery");
                    alertRecovery.setPositiveButton("Sure",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Uri firmware_uri = Uri.parse(file_recovery);
                                    Firmware_DownloadId = DownloadData(firmware_uri, file_recovery);
                                }
                            });

                    alertRecovery.setNegativeButton("Maybe later",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                }*/
                if (raspuns_update.equals(r_da))
                {
                    Log.e("JSON", "Sunt cu da "+str);
                    file_update = jo.getString("file_update");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    TextView tvBuildVer = (TextView) findViewById(R.id.textnumber);
                    tvBuildVer.setText(String.format(Utils.getVersion()));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setTitle("Update your " + "ROM " + tvBuildVer.getText());
                    alertDialogBuilder.setMessage(changelog);
                    alertDialogBuilder.setPositiveButton("Sure",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //     Toast.makeText(MainActivity.this,file,Toast.LENGTH_LONG).show();
                                    Uri firmware_uri = Uri.parse(file_update);
                                    Firmware_DownloadId = DownloadData(firmware_uri, file_update);
                                }
                            });
                    AlertDialog.Builder builder = alertDialogBuilder.setNegativeButton("Maybe later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
                            final Button cancel_update = (Button) findViewById(R.id.cancel_update);
                            final TextView progres = (TextView) findViewById(R.id.progres);
                            button_update = (Button) findViewById(R.id.button_update);
                            button_update.setVisibility(View.VISIBLE);
                            progres.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            cancel_update.setVisibility(View.INVISIBLE);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if (raspuns_update.equals(r_nu))
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
                else if (raspuns_update.equals(r_not))
                {
                    Log.e("JSON", "Sunt cu not "+str);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Your Device don't support CustomOTA Services!");
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
                if (raspuns_update.equals(r_err))
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
            Log.e("JSON", "Couldn't get json from server. Please check your internet connection");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't connect to internet connection!",
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnSettings_OnClick(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
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

         postData.put("mobile", "android");
         postData.put("ROMID", tvBuildVer.getText());
         postData.put("AndroidV", tv1.getText());
         postData.put("DeviceID", tv3.getText());
         postData.put("RecoveryID", "32");
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute("http://ota.geolin.ro/verificare.php");
    }

    private long DownloadData (Uri uri, String file) {
//        Log.e("JSON", "Creez directorul: "+Environment.getExternalStorageDirectory().toString()+"/OTA");
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/OTA");

        if(folder.exists()){
            //Toast.makeText(getApplicationContext(),"exists",Toast.LENGTH_LONG).show();
//            Log.e("JSON", "Exista");
        }else{
            //Toast.makeText(getApplicationContext(),"Nu exists. Incerc sa creez",Toast.LENGTH_LONG).show();
//            Log.e("JSON", "Can write: " + Environment.getExternalStorageDirectory().canWrite());
            folder.mkdirs();
            boolean result = folder.mkdirs();
//            Log.d("JSON", "mkdirs: " + result);
        }
        DownloadManager.Request request = new DownloadManager.Request(uri);

        String[] path = uri.getPath().split("/");
        String nume_fisier = path[ path.length - 1 ];

        request.setDescription("OTA Manager Firmware");
        request.setTitle("Downloading Update...");
        request.allowScanningByMediaScanner();
        request.setAllowedOverRoaming(false);
        request.setDestinationInExternalPublicDir("/OTA/Updates", nume_fisier);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
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
                            progres.setText((int) dl_progress + "% Downloaded");
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
        super.onResume();

        //        CheckDwnloadStatus();

        IntentFilter intentFilter
                = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
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

    /*public void OnClick_beta(View view) {
        Toast toast = Toast.makeText(MainActivity.this,
                "Android Beta in progress..." + " Please wait new app version.", Toast.LENGTH_LONG);
        toast.show();
        /*Intent intent = new Intent(this, beta_program.class);
        startActivity(intent);
    }*/
}