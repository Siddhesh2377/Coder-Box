package com.dark.coderbox;

import static com.dark.coderbox.DarkServices.EnvPathVariables.THEMES_FOLDER;
import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.Setup.THEMES.Add_Themes;
import static com.dark.coderbox.libs.Temp.SYSTEM.SYSTEM_FILE;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dark.coderbox.libs.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.S)
public class SetUpActivity extends AppCompatActivity {

    //Permissions
    private final String[] BLE_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,};
    private final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION,};


    public ArrayList<String> Generate_XR_LIST_SYSTEM = new ArrayList<>();

    public void CopyAssets(String path, String name, @NonNull Context context) {
        try {
            OutputStream myOutput = new FileOutputStream(path + name);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = context.getAssets().open(name);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();
        } catch (IOException e) {
            Log.d("Copying System Theme : ", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        try {
            INIT();
        } catch (IOException e) {
            Log.d("Copying System Theme : ", e.toString());
        }
    }

    public void INIT() throws IOException {
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRPermissions"));
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/CBRData/SystemData"));
        FileUtil.makeDir(getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES"));
        SYSTEM_FILE(Generate_XR_LIST_SYSTEM, this);
        CopyAssets(THEMES_FOLDER, "MK_LIGHT.zip", this);
        Add_Themes(THEMES_FOLDER.concat("MK_LIGHT.zip"), this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            askForPermissions();
        }
    }

    public void askForPermissions() {
        //Permissions

        //Read An Write Permission
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000); // your request code
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                                    }
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                    startActivity(intent);
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}