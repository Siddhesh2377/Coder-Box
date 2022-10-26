package com.dark.coderbox.DarkServices;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DarkRootServices {

    public static String topPackageName;

    public static void GetBackGroundServicesList(ArrayList<HashMap<String, Object>> GetBackgroundList, Context context) {
        GetBackgroundList.clear();
//        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        long time = System.currentTimeMillis();
//        // We get usage stats for the last 10 seconds
//        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
//        // Sort the stats by the last time used
//        if (stats != null) {
//            for (UsageStats usageStats : stats) {
//
//            }
//        }

        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningTaskInfo = manager.getRunningServices(1);

        for (ActivityManager.RunningServiceInfo info : runningTaskInfo) {
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("pack", runningTaskInfo.get(0));
                GetBackgroundList.add(_item);
            }
        }
    }

    private static boolean isSYSTEM(ApplicationInfo pkgInfo) {
        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
