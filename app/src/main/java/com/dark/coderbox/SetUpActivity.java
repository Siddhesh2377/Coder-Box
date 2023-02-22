package com.dark.coderbox;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;
import static com.dark.coderbox.DarkServices.DarkUtils.ShowMessage;
import static com.dark.coderbox.DarkServices.DarkUtils.unzip;
import static com.dark.coderbox.DarkServices.EnvPathVariables.DEFAULT_WALLPAPER;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_DATA_FILE;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_DATA_FOLDER;
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
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.S)
public class SetUpActivity extends AppCompatActivity {

    //ArrayList
    public static ArrayList<String> systemInfo = new ArrayList<>();
    //Permission Components
    public Manifest.permission PM_Holder;
    //Ints
    public int Android11 = Build.VERSION_CODES.R;
    public int Android10 = Build.VERSION_CODES.Q;

    public boolean InitSystem = false;

    AlertDialog custom_progressbar;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        txt = findViewById(R.id.textView);

        //End ...< _ >...
    }

    public void ActivityTransition(final View _view, final String _transitionName, final Intent _intent) {
        _view.setTransitionName(_transitionName);

        ActivityOptions optionsCompat = makeSceneTransitionAnimation(this, _view, _transitionName);
        startActivity(_intent, optionsCompat.toBundle());
    }

    private void downloading() {
        ShowMessage(new StringBuilder("Download Started !"), SetUpActivity.this);
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
                            ShowMessage(new StringBuilder("Completed"), SetUpActivity.this);
                            InitSystem = true;
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            ActivityTransition(txt, "", i);
                        }
                    });
                } catch (Exception e) {
                    ShowMessage(new StringBuilder(e.toString()), SetUpActivity.this);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();

        if (InitSystem){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTransition(txt, "", i);
        }else {
            if (!InitSystem){
                if (Build.VERSION.SDK_INT <= Android10) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    } else {
                        if (!Settings.canDrawOverlays(this)) {
                            AskCanDrawOverLayPermission();
                        } else {
                            if (Environment.isExternalStorageManager()) {
                                INIT_SYSTEM();
                            }
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Android11) {
                        if (!Environment.isExternalStorageManager()) {
                            ManageStoragePermission();
                        } else {
                            if (!Settings.canDrawOverlays(this)) {
                                AskCanDrawOverLayPermission();
                            } else {
                                if (Environment.isExternalStorageManager()) {
                                    INIT_SYSTEM();
                                }
                            }
                        }
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
        if (isExistFile(DEFAULT_WALLPAPER)) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTransition(txt, "", i);
        } else {
            if (!isExistFile(DEFAULT_WALLPAPER)) {
                systemInfo.clear();
                FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRData/SystemData"));
                FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES"));
                FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/SYSTEM/USERS"));

                systemInfo.add("DATA.OPEN : Current.Theme C : Lite : Data.Close");

                FileUtil.writeFile(SYSTEM_DATA_FILE, "");
                FileUtil.writeFile(SYSTEM_DATA_FOLDER.concat("/").concat("system.xr"), new Gson().toJson(systemInfo));

                if (isConnected()) {
                    downloading();
                } else {
                    if (!isConnected()) {
                        ShowMessage(new StringBuilder("Connect To the INTERNET !"), this);
                    }
                }
            }
        }
    }

    public void ManageStoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void AskCanDrawOverLayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
