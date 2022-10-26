package com.dark.coderbox.DarkServices;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;


public class NearbyDevices {

    private static final ArrayList<String> HotSpotInfo = new ArrayList<>();
    public static boolean IsHotSpotOn = false;
    private static WifiManager.LocalOnlyHotspotReservation mReservation;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void ActivateConnection(Context context, ImageView img) {

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                Log.d(TAG, "Wifi Hotspot is on now");
                mReservation = reservation;
                HotSpotInfo.add(reservation.getWifiConfiguration().SSID);
                HotSpotInfo.add(reservation.getWifiConfiguration().preSharedKey);
                Toast.makeText(context, reservation.getWifiConfiguration().SSID + " Activated", Toast.LENGTH_LONG).show();

                GenerateCode(img, new Gson().toJson(HotSpotInfo), context);
                IsHotSpotOn = true;
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Log.d(TAG, "onStopped: ");
                Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show();
                final WifiManager wifiMan = context.getSystemService(WifiManager.class);
                wifiMan.setWifiEnabled(true);
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Log.d(TAG, "onFailed: ");
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }, new Handler());
    }

    public static void GenerateCode(ImageView img, String txt, Context context) {
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(txt, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            img.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void DestroyConnection() {
        if (mReservation != null) {
            mReservation.close();
        }
    }

    public static void ConnectNetwork(String ssid, String networkPass, Context context) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        WifiManager wifiManager = context.getSystemService(WifiManager.class);
        wifiManager.addNetwork(conf);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }


    }
}
