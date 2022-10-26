package com.dark.coderbox.libs;

import static android.content.Context.AUDIO_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Temp {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static class SYSTEM {
        public static String DEFAULT_LIGHT_THEMES_DATA_PATHS = FileUtil.getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/MK_LIGHT/Data.json");
        public static String SYSTEM_DATA_FOLDER = FileUtil.getExternalStorageDir().concat("/CBRoot/CBRData/SystemData");

        public static void SYSTEM_FILE(ArrayList<String> Generate_XR_LIST_SYSTEM, Context context) {
            Generate_XR_LIST_SYSTEM.add("DATA.OPEN : Current.Theme C: " + DEFAULT_LIGHT_THEMES_DATA_PATHS + " DATA.CLOSE");
            Generate_XR_LIST_SYSTEM.clear();
            AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            Generate_XR_LIST_SYSTEM.add("DATA.OPEN : Current.Theme C: " + DEFAULT_LIGHT_THEMES_DATA_PATHS + " DATA.CLOSE");
            Generate_XR_LIST_SYSTEM.add("DATA.OPEN : Build.Time X: " + GetTime() + " DATA.CLOSE");
            Generate_XR_LIST_SYSTEM.add("DATA.OPEN : Current.Volume X: " + am.getStreamVolume(AudioManager.STREAM_MUSIC) + " DATA.CLOSE");
            FileUtil.writeFile(SYSTEM_DATA_FOLDER.concat("/").concat("system.xr"), new Gson().toJson(Generate_XR_LIST_SYSTEM));
        }

        @SuppressLint("SimpleDateFormat")
        public static String GetTime() {
            Calendar calendar;
            SimpleDateFormat simpleDateFormat;
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("hh : mm a");

            return simpleDateFormat.format(calendar.getTime());
        }
    }

}
