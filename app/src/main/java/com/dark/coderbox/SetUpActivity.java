package com.dark.coderbox;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;
import static com.dark.coderbox.DarkServices.DarkUtils.ShowMessage;
import static com.dark.coderbox.DarkServices.DarkUtils.unzip;
import static com.dark.coderbox.DarkServices.EnvPathVariables.DEFAULT_WALLPAPER;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_DATA_FILE;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_FOLDER;
import static com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule.SetBackData;
import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.FileUtil.isExistFile;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dark.coderbox.libs.FileUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.S)
public class SetUpActivity extends AppCompatActivity {

    AlertDialog custom_progressbar;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        txt = findViewById(R.id.textView);

        //Logic
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    }
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }

    }

    public void ActivityTransition(final View _view, final String _transitionName, final Intent _intent) {
        _view.setTransitionName(_transitionName);

        ActivityOptions optionsCompat = makeSceneTransitionAnimation(this, _view, _transitionName);
        startActivity(_intent, optionsCompat.toBundle());
    }

    private void downloading() {
        ShowMessage("Download Started !", SetUpActivity.this);
        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        custom_progressbar = new AlertDialog.Builder(SetUpActivity.this).create();
        LayoutInflater custom_lyt = getLayoutInflater();
        View custom_view = custom_lyt.inflate(R.layout.progress_dailog, null);
        custom_progressbar.setView(custom_view);

        final LinearLayout bg_custom_progress = custom_view.findViewById(R.id.bg_custom_progress);
        final ProgressBar download_progress = custom_view.findViewById(R.id.download_progress);


        SetBackData(18, "#FFFFFFFF", 0, "#FFFFFFFF", bg_custom_progress);
        custom_progressbar.setCancelable(false);
        custom_progressbar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_progressbar.show();

        executor.execute(new Runnable() {

            int count;

            @Override
            public void run() {

                //Background work here
                try {
                    // put your url.this is sample url.
                    URL url = new URL("https://github.com/DarkCode462/Coder-Box/raw/master/THEMES.zip");
                    URLConnection conection = url.openConnection();
                    conection.connect();


                    int lenghtOfFile = conection.getContentLength();

                    // download the file
                    InputStream input = conection.getInputStream();

                    //catalogfile is your destenition folder
                    OutputStream output = new FileOutputStream(SYSTEM_FOLDER.concat("/").concat("THEMES.zip"));


                    byte[] data = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        download_progress.setProgress(Integer.parseInt("" + (int) ((total * 100) / lenghtOfFile)));
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //UI Thread work here
                            custom_progressbar.dismiss();
                            unzip(SYSTEM_FOLDER.concat("/THEMES.zip"), SYSTEM_FOLDER);
                            ShowMessage("Completed", SetUpActivity.this);

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            ActivityTransition(txt, "", i);
                        }
                    });
                } catch (Exception e) {
                    ShowMessage(e.toString(), SetUpActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (!Settings.canDrawOverlays(SetUpActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                if (Settings.canDrawOverlays(SetUpActivity.this)) {
                    if (isExistFile(DEFAULT_WALLPAPER)) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        ActivityTransition(txt, "", i);
                    } else {
                        if (!isExistFile(DEFAULT_WALLPAPER)) {
                            INIT_SYSTEM();
                        }
                    }
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (!Settings.canDrawOverlays(SetUpActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            if (Settings.canDrawOverlays(SetUpActivity.this)) {
                if (isExistFile(DEFAULT_WALLPAPER)) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    ActivityTransition(txt, "", i);
                } else {
                    if (!isExistFile(DEFAULT_WALLPAPER)) {
                        INIT_SYSTEM();
                    }
                }
            }
        }
    }

    public boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return false;
    }

    public void INIT_SYSTEM() {
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRData/SystemData"));
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES"));
        FileUtil.writeFile(SYSTEM_DATA_FILE, "");
        if (isConnected()) {
            downloading();
        } else {
            if (!isConnected()) {
                ShowMessage("Connect To the INTERNET !", this);
            }
        }
    }

//    public void askForPermissions() {
//        //Permissions
//
//        //Read An Write Permission
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1000); // your request code
//        } else {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                //Manager Storage Permission
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    if (!Environment.isExternalStorageManager()) {
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                        startActivity(intent);
//                    } else {
//                        if (!Settings.System.canWrite(getApplicationContext())) {
//                            Toast.makeText(this, "Please try again after giving access to Change system settings", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
//                            startActivityForResult(intent, 200);
//                        } else {
//                            //Can Draw Overlays Permission
//                            if (!Settings.canDrawOverlays(SetUpActivity.this)) {
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                                startActivity(intent);
//                            } else {
//                                //UseAccess Permission
//                                try {
//                                    PackageManager packageManager = getPackageManager();
//                                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
//                                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
//                                    int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
//
//                                    if ((mode == AppOpsManager.MODE_ALLOWED)) {
//                                        if (ContextCompat.checkSelfPermission(SetUpActivity.this,
//                                                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
//                                                    ActivityCompat.requestPermissions(this, ANDROID_12_BLE_PERMISSIONS, 1007);
//                                                else
//                                                    ActivityCompat.requestPermissions(this, BLE_PERMISSIONS, 1007);
//                                                return;
//                                            }
//                                        } else {
//                                            if (Build.VERSION.SDK_INT >= 23) {
//                                                if (ContextCompat.checkSelfPermission(SetUpActivity.this,
//                                                        Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//
//                                                    if (ActivityCompat.checkSelfPermission(this,
//                                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                                        //get access to location permission
//                                                        int REQUEST_CODE_ASK_PERMISSIONS = 123;
//                                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
//                                                        return;
//                                                    } else {
//
//                                                    }
//                                                }
//
//                                            }
//                                        }
//                                    } else {
//                                        if (!(mode == AppOpsManager.MODE_ALLOWED)) {
//                                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                                            startActivity(intent);
//                                        }
//                                    }
//
//                                }
//
//

}
