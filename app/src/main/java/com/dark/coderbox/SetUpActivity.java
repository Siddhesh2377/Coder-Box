package com.dark.coderbox;

import static com.dark.coderbox.DarkServices.DarkUtils.ShowMessage;
import static com.dark.coderbox.DarkServices.DarkUtils.copyAssets;
import static com.dark.coderbox.DarkServices.DarkUtils.unzip;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_FOLDER;
import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.Temp.SYSTEM.SYSTEM_FILE;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dark.coderbox.libs.FileUtil;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.S)
public class SetUpActivity extends AppCompatActivity {

    //Permissions
    private final String[] BLE_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,};
    private final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION,};

    public ArrayList<String> Generate_XR_LIST_SYSTEM = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        //Logic
        askForPermissions();
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRPermissions"));
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRData/SystemData"));
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES"));
        SYSTEM_FILE(Generate_XR_LIST_SYSTEM, this);
//        try {
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Html.fromHtml() + ""));
//            request.setTitle(title);
//            // in order for this if to run, you must use the android 3.2 to compile your app
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            }
//
////                    request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS, "ALSupermarket");
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//            // // get download service and enqueue file
//            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            long index = manager.enqueue(request);
//            context.registerReceiver(receiver_complete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//
////                    manager.openDownloadedFile(index);
////                    uri = manager.getUriForDownloadedFile(index);
////                    Log.d("downloading url = ", uri.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        unzip("THEMES.zip", SYSTEM_FOLDER + "THEMES");
        ShowMessage("Completed", this);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    public void askForPermissions() {
        //Permissions

        //Read An Write Permission
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1000); // your request code
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Manager Storage Permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    } else {
                        if (!Settings.System.canWrite(getApplicationContext())) {
                            Toast.makeText(this, "Please try again after giving access to Change system settings", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 200);
                        } else {
                            //Can Draw Overlays Permission
                            if (!Settings.canDrawOverlays(SetUpActivity.this)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            } else {
                                //UseAccess Permission
                                try {
                                    PackageManager packageManager = getPackageManager();
                                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
                                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                                    int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);

                                    if ((mode == AppOpsManager.MODE_ALLOWED)) {
                                        if (ContextCompat.checkSelfPermission(SetUpActivity.this,
                                                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                                    ActivityCompat.requestPermissions(this, ANDROID_12_BLE_PERMISSIONS, 1007);
                                                else
                                                    ActivityCompat.requestPermissions(this, BLE_PERMISSIONS, 1007);
                                                return;
                                            }
                                        } else {
                                            if (Build.VERSION.SDK_INT >= 23) {
                                                if (ContextCompat.checkSelfPermission(SetUpActivity.this,
                                                        Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

                                                    if (ActivityCompat.checkSelfPermission(this,
                                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                        //get access to location permission
                                                        int REQUEST_CODE_ASK_PERMISSIONS = 123;
                                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
                                                        return;
                                                    } else {

                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        if (!(mode == AppOpsManager.MODE_ALLOWED)) {
                                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                            startActivity(intent);
                                        }
                                    }

                                } catch (PackageManager.NameNotFoundException e) {

                                }
                            }
                        }

                    }
                } else {
                    if (!Settings.System.canWrite(getApplicationContext())) {
                        Toast.makeText(this, "Please try again after giving access to Change system settings", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 200);

                    } else {
                        //Can Draw Overlays Permission
                        if (!Settings.canDrawOverlays(SetUpActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        } else {
                            //UseAccess Permission
                            try {
                                PackageManager packageManager = getPackageManager();
                                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
                                AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                                if ((mode == AppOpsManager.MODE_ALLOWED)) {
                                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        //get access to location permission
                                        int REQUEST_CODE_ASK_PERMISSIONS = 123;
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
                                    } else {
                                        ShowMessage("hi", this);
                                    }
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                    startActivity(intent);
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                ShowMessage(e.toString(), this);
                            }
                        }
                    }
                }
            }
        }
    }
}


