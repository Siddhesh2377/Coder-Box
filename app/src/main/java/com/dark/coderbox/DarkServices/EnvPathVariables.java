package com.dark.coderbox.DarkServices;

import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;

public class EnvPathVariables {
    public static String SYSTEM_DATA_FILE = getExternalStorageDir().concat("/CBRoot/CBRData/SystemData/system.xr");
    public static String SYSTEM_DATA_FOLDER = getExternalStorageDir().concat("/CBRoot/CBRData/SystemData");
    public static String THEME_PATH_FILE = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/Path.json");
    public static String THEMES_FOLDER = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES");
    public static String SYSTEM_FOLDER = getExternalStorageDir().concat("/CBRoot/SYSTEM");
    public static String DEFAULT_WALLPAPER = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/Wallpapers/default_wallpaper.jpg");
}
