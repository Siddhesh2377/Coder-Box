package com.dark.coderbox;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;
import static com.dark.coderbox.DarkServices.DarkRootServices.GetBackGroundServicesList;
import static com.dark.coderbox.DarkServices.DarkUtils.ShowMessage;
import static com.dark.coderbox.DarkServices.EnvPathVariables.DEFAULT_WALLPAPER;
import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_DATA_FILE;
import static com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule.ANIM_FADEOUT;
import static com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule.ANIM_FADE_IN;
import static com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule.ColourAnim;
import static com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule.SetBackData;
import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.Keys.MODS.SYSTEM_XR;
import static com.dark.coderbox.libs.Keys.XRLanguages.GET_VOLUME;
import static com.dark.coderbox.libs.LanguageXR.WriteSystemInfo;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.dark.coderbox.DarkServices.Connections.WifiState;
import com.dark.coderbox.DarkServices.Connections.WifiStateListener;
import com.dark.coderbox.DarkServices.Connections.WifiStateReceiver;
import com.dark.coderbox.DarkServices.DarkUtils;
import com.dark.coderbox.libs.FileUtil;
import com.dark.coderbox.libs.LanguageXR;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

@RequiresApi(api = Build.VERSION_CODES.S)
@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends AppCompatActivity implements WifiStateListener {
    public static String COLOR_ACCENT;
    public static String COLOR_BASE = "FFFFFF";
    public static String COLOR_DOMINANT = "2E2E2E";
    public static String COLOR_PRIMARY;
    public static String COLOR_SECONDARY;
    public static String COLOR_TERTIARY;
    //Arrays
    public final ArrayList<String> list = new ArrayList<>();
    public final ArrayList<String> folderList = new ArrayList<>();
    public final ArrayList<String> fileList = new ArrayList<>();
    public final ArrayList<HashMap<String, Object>> GetBackgroundList = new ArrayList<>();
    public final ArrayList<HashMap<String, Object>> Files = new ArrayList<>();
    public final ArrayList<Drawable> Generate_APPS_Icons_LIST = new ArrayList<>();
    public final ArrayList<String> Generate_APPS_Name_LIST = new ArrayList<>();
    public final ArrayList<HashMap<String, Object>> Generate_APPS_LIST = new ArrayList<>();
    //Strings
    public String base_color = "#FFFFFFFF";
    //Booleans
    public boolean Show_DOCK = false;
    public boolean show_wifi_Notification = false;
    //Components
    public AlertDialog settings_dialog;
    public Timer timer_ = new Timer();
    public TimerTask T_;
    public TimerTask systemNotification_timer;
    public Timer system_notification_T = new Timer();
    public Timer timer_close_floating_dock = new Timer();
    public TimerTask T_close_floating_dock;
    public Timer timer_close_floating_dock2 = new Timer();
    public TimerTask T_close_floating_dock2;
    public WindowManager windowManager2;
    public WindowManager.LayoutParams layoutParams2;
    public View displayView2;
    public WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;
    public View displayView;
    //Numerical
    public double index = 0;
    public double y1 = 0;
    public double x1 = 0;
    public double y2 = 0;
    public double x2 = 0;
    //Int
    public int count_F_D_M = 0;
    public int v_floating_dock_shortcuts = View.VISIBLE;
    public int H = 0;
    public LinearLayout controls_border;
    WifiStateReceiver wifiStateReceiver;
    private LinearLayout bg_main;
    private LinearLayout test_body;
    private ImageView img;

    public boolean isHomeApp() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        return res.activityInfo != null && getPackageName()
                .equals(res.activityInfo.packageName);
    }

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        img = findViewById(R.id.bg_Home_REF);
        // test_body = findViewById(R.id.test_body);
        bg_main = findViewById(R.id.bg_main);
        controls_border = findViewById(R.id.controle_border);


        //Logic
        SetupLogic();

        //        if (All_Permissions_OK) {
//            //Setting Wallpaper
////            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////                final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
////                @SuppressLint("MissingPermission") final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
////                img.setImageDrawable(wallpaperDrawable);
////            } else {
////                img.setImageResource(R.drawable.defult_wallpaper);
////            }
//
//            //Initializing Syste
//
//        }

        bg_main.setOnTouchListener((view, p2) -> {

            switch (p2.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    y1 = p2.getY();
                    x1 = p2.getX();

                    break;

                case MotionEvent.ACTION_UP:
                    y2 = p2.getY();
                    x2 = p2.getX();


                    //DOWN
                    if (((y1 - y2) < -250)) {
                        ShowUserPanel();
                    }

                    //UP
                    if (((y2 - y1) < -250)) {

                        if (!Show_DOCK) {
                            Show_System_Dock();
                            Show_DOCK = true;
                        }

                    }

                    //RIGHT
                    if (((x1 - x2) < -250)) {

                        H = bg_main.getHeight() / 3;
                        ShowMessage(String.valueOf(H), this);
                        // ShowSideBar(controls_border);
                        Show_Notification();
                    }

                    //LEFT
                    if (((x2 - x1) < -250)) {
                        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                        WriteSystemInfo(SYSTEM_DATA_FILE, SYSTEM_XR, GET_VOLUME, "DATA.OPEN : Current.Volume X: " + am.getStreamVolume(AudioManager.STREAM_MUSIC) + " DATA.CLOSE", this);
                        Toast.makeText(MainActivity.this, LanguageXR.READ_XR_DATA(SYSTEM_XR, GET_VOLUME, SYSTEM_DATA_FILE), Toast.LENGTH_SHORT).show();
                    }

                    break;
            }

            return true;
        });

    }

    public int GetColor(int CASE) {
        Bitmap icon = BitmapFactory.decodeFile(DEFAULT_WALLPAPER);
        Palette p = Palette.from(icon).generate();
        Palette.Swatch vibrantSwatch = p.getDarkMutedSwatch();
        ShowMessage(String.valueOf(vibrantSwatch.getRgb()), this);
        return p.getLightVibrantColor(getResources().getColor(android.R.color.black));
    }

    public void onStart() {
        super.onStart();
        Close_System_Dock();
        Show_System_Dock();
        if (wifiStateReceiver != null) {
            return;
        }
        wifiStateReceiver = WifiStateReceiver.createAndRegister(this, this);
        // Get current wifi state
        wifiStateReceiver.onReceive(this, null);
    }

    public void onResume() {
        super.onResume();
        Close_System_Dock();
        Show_System_Dock();
        Show_DOCK = true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void onPause() {

        super.onPause();
    }

    public void onDestroy() {
        if (wifiStateReceiver != null) {
            unregisterReceiver(wifiStateReceiver);
            wifiStateReceiver = null;
        }
        super.onDestroy();
    }

    @SuppressLint("SuspiciousIndentation")
    public void SetupLogic() {
        FullScreen_call();
        Bitmap myBitmap = BitmapFactory.decodeFile(DEFAULT_WALLPAPER);
        img.setImageBitmap(myBitmap);
        LoadApps();
        GetBackGroundServicesList(GetBackgroundList, this);
    }

    public void LoadApps() {
        Generate_APPS_LIST.clear();
        Generate_APPS_Icons_LIST.clear();
        Generate_APPS_Name_LIST.clear();

        String packageName;

        final Intent startupIntent = new Intent(Intent.ACTION_MAIN, null);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        for (ResolveInfo resolveInfo : activities) {
            packageName = resolveInfo.activityInfo.packageName;
            // activity name as com.example/com.example.MainActivity
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("pack", packageName);
                Generate_APPS_LIST.add(_item);
            }

        }
    }

    public void Show_Notification() {
        View noti_view = getLayoutInflater().inflate(R.layout.shownotifications, null);
        final PopupWindow system_notification = new PopupWindow(noti_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final LinearLayout bg_system_notification = noti_view.findViewById(R.id.bg_system_notification);
        final LinearLayout indicator_system_notification = noti_view.findViewById(R.id.indicator_system_notification);
        final TextView txt_system_notification = noti_view.findViewById(R.id.txt_system_notification);

        SetBackData(16, "#FFFFFFFF", 0, "#FF222222", bg_system_notification);
        SetBackData(25, "#FF81D2FF", 0, "#FF222222", indicator_system_notification);

        txt_system_notification.setText("System Is Connected To Wifi");
        system_notification.setAnimationStyle(android.R.style.Animation_Translucent);
        system_notification.showAsDropDown(controls_border, 0, 0, Gravity.END);
        system_notification.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        systemNotification_timer = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        system_notification.dismiss();
                    }
                });
            }
        };
        system_notification_T.schedule(systemNotification_timer, 2500);
    }

    public void Show_System_Dock() {
        if (!Settings.canDrawOverlays(this)) {
            //Can Draw Overlays Permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = 180;
            layoutParams.x = 0;
            layoutParams.y = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                layoutParams.preferMinimalPostProcessing = true;
            }
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.system_dock, null);
            displayView.setOnTouchListener(null);

            ImageView lock_ = displayView.findViewById(R.id.lock_);
            ImageView Speed_box = displayView.findViewById(R.id.Speed_Box_);
            ImageView ico = displayView.findViewById(R.id.ico);
            ImageView resent_services = displayView.findViewById(R.id.resent_services);

            LinearLayout headerBlur = displayView.findViewById(R.id.head_blur);
            LinearLayout nearbyServices = displayView.findViewById(R.id.nearbyServices);

            TextView Time_View = displayView.findViewById(R.id.time_view);

            RecyclerView Floating_dock_app = displayView.findViewById(R.id.fd_a);

            SetFilter(lock_, "#FF" + COLOR_BASE, 1);
            SetFilter(Speed_box, "#FF" + COLOR_BASE, 1);
            SetFilter(ico, "#FF" + COLOR_BASE, 0);
            SetFilter(resent_services, "#FF" + COLOR_BASE, 1);


            headerBlur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80));

            final AlphaAnimation fadeIn = new AlphaAnimation(1.0f, 0.0f);
            final AlphaAnimation fadeOut = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1000);
            fadeIn.setFillAfter(true);
            fadeOut.setDuration(1000);
            fadeOut.setFillAfter(true);
            fadeOut.setStartOffset(20);

            headerBlur.setAnimation(fadeOut);
            Speed_box.setVisibility(View.GONE);
            Floating_dock_app.setVisibility(View.GONE);


            T_ = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GetTime(Time_View);
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo info = wifiManager.getConnectionInfo();
                            String ssid = info.getSSID();
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        }
                    });
                }
            };
            timer_.scheduleAtFixedRate(T_, 500, 500);


            //#DF060815
            GetBackGroundServicesList(GetBackgroundList, this);

            Floating_dock_app.setAdapter(new FloatingDock_Apps(Generate_APPS_LIST));
            Floating_dock_app.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            // Green -->  #FF24FF00
            // Red   -->  #FFFF5B5B

            SetBackData(60, "#BF" + COLOR_DOMINANT, 1, "#FFF4D1", headerBlur);

            ico.setOnClickListener(view -> {
                if (v_floating_dock_shortcuts == View.VISIBLE) {
                    v_floating_dock_shortcuts = View.GONE;
                    resent_services.setVisibility(View.GONE);
                    Floating_dock_app.setVisibility(View.VISIBLE);
                } else {
                    if (v_floating_dock_shortcuts == View.GONE) {
                        v_floating_dock_shortcuts = View.VISIBLE;
                        resent_services.setVisibility(View.VISIBLE);
                        Floating_dock_app.setVisibility(View.GONE);
                    }
                }
            });

            headerBlur.setOnTouchListener((view, p2) -> {
                switch (p2.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = p2.getY();
                        x1 = p2.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        y2 = p2.getY();
                        x2 = p2.getX();

                        if (((x1 - x2) < -250)) {
                            //Right
                            ANIM_FADEOUT(headerBlur);
                            T_close_floating_dock = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> Close_System_Dock());
                                }
                            };
                            timer_close_floating_dock.schedule(T_close_floating_dock, 1000);
                        }

                        if (((x2 - x1) < -250)) {
                            //left
                            Show_Notification();
                        }
                        break;
                }
                return true;
            });

            resent_services.setOnClickListener(view -> {
                if (count_F_D_M == 0) {
                    count_F_D_M++;
                    ShowUserPanel();
                } else {
                    if (count_F_D_M == 1) {
                        count_F_D_M--;
                        ANIM_FADEOUT(displayView2.findViewById(R.id.floating_dock_bg));
                        T_close_floating_dock2 = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> Close_UserPanel());
                            }
                        };
                        timer_close_floating_dock2.schedule(T_close_floating_dock2, 1000);
                    }
                }
            });

            lock_.setOnClickListener(view -> {
                headerBlur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80));
                lock_.setVisibility(View.GONE);
                Speed_box.setVisibility(View.VISIBLE);
                resent_services.setVisibility(resent_services.getVisibility());
                displayView.findViewById(R.id.ico).setVisibility(View.VISIBLE);
                displayView.findViewById(R.id.time_view).setVisibility(View.VISIBLE);
            });

            Speed_box.setOnClickListener(view_ -> {
                resent_services.setVisibility(resent_services.getVisibility());

                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                CodeScanner mCodeScanner;
                final ArrayList<String>[] GeneratedQrData = new ArrayList[]{new ArrayList<>()};

                MaterialAlertDialogBuilder dlg = new MaterialAlertDialogBuilder(this);

                dlg.setTitle("Select Device");
                dlg.setView(view);
                dlg.setMessage("You can select one or Multi-pal Devices");

                final CodeScannerView usr = view.findViewById(R.id.scanner_vie);

                mCodeScanner = new CodeScanner(this, usr);

                mCodeScanner.setDecodeCallback(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull final Result result) {
                        GeneratedQrData[0] = new Gson().fromJson(result.getText(), new TypeToken<ArrayList<String>>() {
                        }.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });

                usr.setOnClickListener(view1 -> mCodeScanner.startPreview());

                dlg.setPositiveButton("Connect", (dialog, which) -> {

                });

                dlg.setNegativeButton("Cancel", (dialog, which) -> dlg.create().cancel());

                dlg.create().show();

                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });

            });

            nearbyServices.setOnClickListener(v -> {
                Show_Notification();
            });

            windowManager.addView(displayView, layoutParams);
        }
    }

    public void SetState(int CASE) {
        View v = displayView.findViewById(R.id.nearbyServices);

        switch (CASE) {
            case 1:
                //Off
                SetBackData(60, "#FF5B5B", 0, "#FFF4D1", v);
                break;
            case 2:
                //On
                SetBackData(60, "#81D2FF", 0, "#FFF4D1", v);
                break;
            case 3:
                //Connected
                SetBackData(60, "#00A3FF", 0, "#FFF4D1", v);
                break;


        }

    }

    public void SetFilter(ImageView imageView, String base_color, int Case) {
        if (Case == 0) {
            imageView.setColorFilter(Color.parseColor(base_color), PorterDuff.Mode.MULTIPLY);
        } else {
            if (Case == 1) {
                imageView.setColorFilter(Color.parseColor(base_color), PorterDuff.Mode.SRC_IN);
            }
        }

    }

    public void ShowUserPanel() {
        windowManager2 = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams2 = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams2.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams2.format = PixelFormat.RGBA_8888;
        layoutParams2.gravity = Gravity.TOP;
        layoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams2.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams2.height = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams2.x = 0;
        layoutParams2.y = 0;

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView2 = layoutInflater.inflate(R.layout.floting_dock_max, null);
        displayView2.setOnTouchListener(null);

        BlurView floating_dock_bg = displayView2.findViewById(R.id.floating_dock_bg);

        LinearLayout btn_box = displayView2.findViewById(R.id.btn_box);
        LinearLayout clean_back_bg = displayView2.findViewById(R.id.clean_back_bg);
        LinearLayout slide_diss_bg = displayView2.findViewById(R.id.slide_diss_bg);
        LinearLayout bg_cc_base = displayView2.findViewById(R.id.bg_cc_base);
        LinearLayout Wifi_btn = displayView2.findViewById(R.id.wifi_btn);
        LinearLayout Bluetooth_btn = displayView2.findViewById(R.id.bluetooth_btn);
        LinearLayout Bottom_bar = displayView2.findViewById(R.id.bottom_bar);

        ImageView resents_icon = displayView2.findViewById(R.id.resents_icon);
        ImageView app_list_ic = displayView2.findViewById(R.id.apps_ic);
        ImageView resent_app_list_ic = displayView2.findViewById(R.id.resent_apps_ic);
        ImageView shortcut_ic = displayView2.findViewById(R.id.shaortcut_ic);
        ImageView cc_ic = displayView2.findViewById(R.id.Cc_ic);
        ImageView cell_data = displayView2.findViewById(R.id.cell_data_btn);

        TextView window_title = displayView2.findViewById(R.id.window_title);
        TextView battery_idi_txt = displayView2.findViewById(R.id.battery_idi_txt);

        GridView Task_list = displayView2.findViewById(R.id.task_grid);

        ProgressBar battery_idi = displayView2.findViewById(R.id.battery_idi);

        bg_cc_base.setVisibility(View.GONE);
        Task_list.setVisibility(View.VISIBLE);


        Task_list.setOnItemClickListener((adapterView, v, i, l) -> {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Objects.requireNonNull(Generate_APPS_LIST.get(i).get("pack")).toString());

            ActivityTransition(img, "", launchIntent);
            Close_UserPanel();
        });

        AsyncTask.execute(() -> {
            LoadApps();
            BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onReceive(Context ctxt, Intent intent) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    float batteryPct = level * 100 / (float) scale;
                    battery_idi_txt.setText(batteryPct + "%");
                    battery_idi.setProgress((int) batteryPct);
                }
            };
            registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            GetBackGroundServicesList(GetBackgroundList, MainActivity.this);
            window_title.setText("Device Apps" + " [ " + Generate_APPS_LIST.size() + " ]");
            ANIM_FADE_IN(floating_dock_bg);
            Task_list.setAdapter(new Task_Adapter(Generate_APPS_LIST));
            Task_list.setNumColumns(3);
        });

        slide_diss_bg.setOnTouchListener((v, p2) -> {
            switch (p2.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y1 = p2.getY();
                    x1 = p2.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    y2 = p2.getY();
                    x2 = p2.getX();
                    if (((y2 - y1) < -250)) {
                        //up
                        DarkUtils.DarkAnimations.Slide_Up(floating_dock_bg, 1, MainActivity.this);
                        ANIM_FADEOUT(floating_dock_bg);
                        T_close_floating_dock = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> Close_UserPanel());
                            }
                        };
                        timer_close_floating_dock.schedule(T_close_floating_dock, 1000);
                    }
                    break;
            }
            return true;
        });

        Bottom_bar.setOnClickListener(v -> {
            ScaleAnimation animation = new ScaleAnimation(1f, -0f, 1f, -0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);

            animation.setFillAfter(true);
            animation.setDuration(1000);
            floating_dock_bg.startAnimation(animation);
            Close_UserPanel();
        });

        SetBackData(8, "#FFFFFFFF", 3, "#FF00A3FF", btn_box);
        SetBackData(18, "#FF8B81FF", 0, "#FF00A3FF", clean_back_bg);
        SetBackData(14, "#C4FFFFFF", 2, "#FFFFFFFF", floating_dock_bg);
        BlurTheLayout(floating_dock_bg, 10);

        cell_data.setOnClickListener(v -> {

        });

        app_list_ic.setOnClickListener(v -> {
            ColourAnim(base_color, "#FF00A3FF", btn_box);
            base_color = "#FF00A3FF";
            window_title.setText("Device Apps" + " [ " + Generate_APPS_LIST.size() + " ]");
            Task_list.setAdapter(new Task_Adapter(Generate_APPS_LIST));
            LoadApps();
            Task_list.setVisibility(View.VISIBLE);
            bg_cc_base.setVisibility(View.GONE);
        });

        resent_app_list_ic.setOnClickListener(v -> {
            ColourAnim(base_color, "#FFFF8181", btn_box);
            base_color = "#FFFF8181";
            window_title.setText("Background Services" + " [ " + GetBackgroundList.size() + " ]");
            Task_list.setAdapter(new Task_Adapter(GetBackgroundList));
            Task_list.setVisibility(View.VISIBLE);
            bg_cc_base.setVisibility(View.GONE);
        });

        shortcut_ic.setOnClickListener(v -> {
            ColourAnim(base_color, "#FFFFAD5C", btn_box);
            base_color = "#FFFFAD5C";
            window_title.setText("Saved Shortcuts");
            Task_list.setVisibility(View.VISIBLE);
            bg_cc_base.setVisibility(View.GONE);
            GetFiles(getExternalStorageDir());
            Task_list.setAdapter(new User_Panel(Files));
        });

        cc_ic.setOnClickListener(v -> {
            window_title.setText("Control Center");
            base_color = "#FF909AFF";
            ColourAnim(base_color, "#FF909AFF", btn_box);
            Task_list.setVisibility(View.GONE);
            bg_cc_base.setVisibility(View.VISIBLE);
        });

        Wifi_btn.setOnClickListener(v -> {
//            Close_UserPanel();
//            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
//            startActivityForResult(panelIntent, 1)
            final WifiManager wifiMan = getSystemService(WifiManager.class);
            wifiMan.setWifiEnabled(!wifiMan.isWifiEnabled());
        });

        Bluetooth_btn.setOnClickListener(v -> {
            Intent panelIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivityForResult(panelIntent, 1);
        });

        clean_back_bg.setOnClickListener(v -> {
            GetBackGroundServicesList(GetBackgroundList, MainActivity.this);
            ColourAnim(base_color, "#FFFF8181", btn_box);
            base_color = "#FFFF8181";
            window_title.setText("Background Services" + " [ " + GetBackgroundList.size() + " ]");
            Task_list.setAdapter(new Task_Adapter(GetBackgroundList));
            Task_list.setVisibility(View.VISIBLE);
            bg_cc_base.setVisibility(View.GONE);
        });

        windowManager2.addView(displayView2, layoutParams2);
    }

    public void Close_System_Dock() {
        try {
            windowManager.removeView(displayView);
            Show_DOCK = false;

        } catch (Exception ignored) {
        }
    }

    public void ActivityTransition(final View _view, final String _transitionName, final Intent _intent) {
        _view.setTransitionName(_transitionName);

        ActivityOptions optionsCompat = makeSceneTransitionAnimation(this, _view, _transitionName);
        startActivity(_intent, optionsCompat.toBundle());
    }

    public void Close_UserPanel() {
        try {
            windowManager2.removeView(displayView2);

        } catch (Exception ignored) {
        }
    }

    public void GetFiles(final String _path) {
        list.clear();
        Files.clear();
        folderList.clear();
        fileList.clear();
        FileUtil.listDir(_path, list);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        index = 0;
        for (int _repeat13 = 0; _repeat13 < list.size(); _repeat13++) {

            if (FileUtil.isDirectory(list.get((int) (index)))) {
                folderList.add(list.get((int) (index)));
            } else {
                if (FileUtil.isFile(list.get((int) (index)))) {
                    fileList.add(list.get((int) (index)));
                }
            }
            index++;
        }
        index = 0;
        for (int _repeat37 = 0; _repeat37 < folderList.size(); _repeat37++) {
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("path", folderList.get((int) (index)));
                Files.add(_item);
            }
            index++;
        }
        index = 0;
        for (int _repeat54 = 0; _repeat54 < fileList.size(); _repeat54++) {
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("path", fileList.get((int) (index)));
                Files.add(_item);
            }
            index++;
        }
    }

    public void ShowSettingDialog() {
        settings_dialog = new AlertDialog.Builder(MainActivity.this).create();
        LayoutInflater settings_dialog_layout = getLayoutInflater();
        View settings_dialog_view = settings_dialog_layout.inflate(R.layout.settings_dailog, null);
        settings_dialog.setView(settings_dialog_view);
        final BlurView settings_dialog_bg = settings_dialog_view.findViewById(R.id.settings_dialog_bg);
        final RoundedImageView setting_theme_light = settings_dialog_view.findViewById(R.id.setting_theme_light);
        final RoundedImageView setting_theme_dark = settings_dialog_view.findViewById(R.id.setting_theme_dark);
        final ImageView img_acc = settings_dialog_view.findViewById(R.id.img_acc);
        final ImageView img_app = settings_dialog_view.findViewById(R.id.img_app);

        img_acc.getDrawable().setColorFilter(Color.parseColor("#FF686868"), PorterDuff.Mode.MULTIPLY);
        img_app.getDrawable().setColorFilter(Color.parseColor("#FF686868"), PorterDuff.Mode.MULTIPLY);
        SetBackData(14, "#BFF0F8FF", 10, "#FFFFFFFF", settings_dialog_bg);

        settings_dialog.setCancelable(true);
        settings_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        settings_dialog.show();
        BlurTheLayout(settings_dialog_bg, 5);
    }

    public void ShowSideBar(View Show) {
        View SideBarView = getLayoutInflater().inflate(R.layout.sidebar, null);
        final PopupWindow SideBar_pop = new PopupWindow(SideBarView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final BlurView bg_sidebar = SideBarView.findViewById(R.id.bg_sidebar);
        final ImageView btn_setting_sidebar = SideBarView.findViewById(R.id.btn_setting_sidebar);

        btn_setting_sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSettingDialog();
            }
        });

        BlurTheLayout(bg_sidebar, 10);
        SideBar_pop.setAnimationStyle(android.R.style.Animation_Dialog);
        SideBar_pop.showAsDropDown(Show, 50, H);
        SideBar_pop.setBackgroundDrawable(new BitmapDrawable());
    }

    public void FullScreen_call() {
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void BlurTheLayout(BlurView blurView, int blur) {
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        blurView.setupWith(rootView)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(blur)
                .setBlurAutoUpdate(false);


    }

    public void GetTime(TextView txt) {
        Calendar calander = null;
        calander = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh : mm a");
        txt.setText(simpleDateFormat.format(calander.getTime()));
    }

    @Override
    public void onWifiStateChanged(WifiState state, String ssid) {

        switch (state) {
            case DISABLED:
                SetState(1);
                break;
            case ENABLED:
                SetState(2);
                break;
            case CONNECTED:
                SetState(3);
                break;
        }

    }

    public class Task_Adapter extends BaseAdapter {

        final ArrayList<HashMap<String, Object>> _data;

        public Task_Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View listitemView = _v;
            if (listitemView == null) {
                listitemView = _inflater.inflate(R.layout.taks_list, null);
            }
            final LinearLayout tasks_lis_bg = listitemView.findViewById(R.id.tasks_lis_bg);
            final RoundedImageView icon_task = listitemView.findViewById(R.id.icon_task);
            final TextView txt_task = listitemView.findViewById(R.id.txt_task);

            final PackageManager pm = getPackageManager();
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(_data.get(_position).get("pack").toString(), 0);
                txt_task.setText(ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            try {
                Drawable icon = getPackageManager().getApplicationIcon(_data.get(_position).get("pack").toString());
                icon_task.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return listitemView;
        }
    }

    public class User_Panel extends BaseAdapter {

        final ArrayList<HashMap<String, Object>> _data;

        public User_Panel(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View listitemView = _v;
            if (listitemView == null) {
                listitemView = _inflater.inflate(R.layout.taks_list, null);
            }
            final LinearLayout tasks_lis_bg = listitemView.findViewById(R.id.tasks_lis_bg);
            final RoundedImageView icon_task = listitemView.findViewById(R.id.icon_task);
            final TextView txt_task = listitemView.findViewById(R.id.txt_task);


            icon_task.setImageResource(R.drawable.folders);

            txt_task.setText(Uri.parse(_data.get(_position).get("path").toString()).getLastPathSegment());
            return listitemView;
        }
    }

    public class FloatingDock_Apps extends RecyclerView.Adapter<FloatingDock_Apps.ViewHolder> {

        final ArrayList<HashMap<String, Object>> _data;

        public FloatingDock_Apps(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = getLayoutInflater();
            View _v = _inflater.inflate(R.layout.docks_list_icon, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;

            final ImageView icon_apps_f = _view.findViewById(R.id.app_icons_floatingdock);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _view.setLayoutParams(_lp);

            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(_data.get(_position).get("pack").toString());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            icon_apps_f.setImageDrawable(icon);

        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }
    }
}