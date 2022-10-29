package com.dark.coderbox.libs;

import static com.dark.coderbox.DarkServices.DarkUtils.ShowMessage;
import static com.dark.coderbox.DarkServices.DarkUtils.unzip;
import static com.dark.coderbox.DarkServices.EnvPathVariables.THEMES_FOLDER;
import static com.dark.coderbox.DarkServices.EnvPathVariables.THEME_PATH_FILE;
import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.FileUtil.getFileLength;
import static com.dark.coderbox.libs.FileUtil.isExistFile;
import static com.dark.coderbox.libs.FileUtil.writeFile;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Setup {

    public static ArrayList<String> Generated_Theme_list = new ArrayList<>();
    public static ArrayList<String> Generated_INFO = new ArrayList<>();
    public static ArrayList<String> Generated_icons = new ArrayList<>();
    public static ArrayList<String> Generated_colors = new ArrayList<>();
    public static ArrayList<String> Generated_wallpaper = new ArrayList<>();
    public static ArrayList<String> Current_Theme_Info = new ArrayList<>();


    public static class THEMES {

        public static void Add_Themes(String path, Context context) {
            String str = Uri.parse(path).getLastPathSegment();
            String current_theme = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/".concat(str.substring(0, str.length() - 4)).concat("/Data.json"));
            unzip(path, THEMES_FOLDER);
            Generated_Theme_list.add(current_theme);
            if (isExistFile(THEME_PATH_FILE) && getFileLength(THEME_PATH_FILE) != 0) {
                if (IsThemeExist(current_theme)) {
                    Generated_Theme_list.clear();
                    Generated_Theme_list = new Gson().fromJson(FileUtil.readFile(THEME_PATH_FILE), new TypeToken<ArrayList<String>>() {
                    }.getType());
                    Generated_Theme_list.add(current_theme);
                    writeFile(THEME_PATH_FILE, "");
                    writeFile(THEME_PATH_FILE, new Gson().toJson(Generated_Theme_list));
                } else {
                    ShowMessage("Theme Exist !", context);
                }
            } else {
                writeFile(THEME_PATH_FILE, "");
                writeFile(THEME_PATH_FILE, new Gson().toJson(Generated_Theme_list));
            }
        }

        public static boolean IsThemeExist(String theme) {
            boolean val;
            if (isExistFile(THEME_PATH_FILE)) {
                Generated_Theme_list = new Gson().fromJson(FileUtil.readFile(THEME_PATH_FILE), new TypeToken<ArrayList<String>>() {
                }.getType());
                Generated_Theme_list.clear();
                Generated_Theme_list = new Gson().fromJson(FileUtil.readFile(THEME_PATH_FILE), new TypeToken<ArrayList<String>>() {
                }.getType());
                val = Generated_Theme_list.contains(theme);
            } else {
                val = false;
            }
            return !val;
        }

        public static String GetThemeInfo(String Theme, String mod, String info) {
            String val = "void";
            String path = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/".concat(Theme).concat("/Data.json"));
            String ThemeRoot = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/".concat(Theme));
            if (IsThemeExist(Theme)) {
                Generated_INFO = new Gson().fromJson(FileUtil.readFile(path), new TypeToken<ArrayList<String>>() {
                }.getType());
                if (mod.equals("MK.TH432876.COLORS")) {
                    val = getColors(info, ThemeRoot.concat(Generated_INFO.get(0)));
                } else {
                    if (mod.equals("MK.TH432876.ICONS")) {
                        val = getIcons(info, ThemeRoot.concat(Generated_INFO.get(1)));
                    } else {
                        if (mod.equals("MK.TH432876.WALLPAPERS")) {
                            val = getWallpaper(info, ThemeRoot.concat(Generated_INFO.get(2)));
                        } else {
                            val = "No Data Found";
                        }
                    }
                }
            } else {
                val = Theme + " << Doesn't Exist !";
            }
            return val;
        }

        public static String getIcons(String data, String icon) {
            Generated_icons = new Gson().fromJson(FileUtil.readFile(icon), new TypeToken<ArrayList<String>>() {
            }.getType());
            Generated_icons.clear();
            Generated_icons = new Gson().fromJson(FileUtil.readFile(icon), new TypeToken<ArrayList<String>>() {
            }.getType());
            String val = Generated_icons.get(0);
            switch (data) {
                case "airplay":
                    val = Generated_icons.get(0);
                    break;
                case "app_icon":
                    val = Generated_icons.get(1);
                    break;
                case "archive":
                    val = Generated_icons.get(2);
                    break;
                case "background_services":
                    val = Generated_icons.get(3);
                    break;
                case "battery":
                    val = Generated_icons.get(4);
                    break;
                case "bluetooth":
                    val = Generated_icons.get(5);
                    break;
                case "brightness":
                    val = Generated_icons.get(6);
                    break;
                case "clean":
                    val = Generated_icons.get(7);
                    break;
                case "control_panel":
                    val = Generated_icons.get(8);
                    break;
                case "desktops":
                    val = Generated_icons.get(9);
                    break;
                case "dnd":
                    val = Generated_icons.get(10);
                    break;
                case "files":
                    val = Generated_icons.get(11);
                    break;
                case "folders":
                    val = Generated_icons.get(12);
                    break;
                case "grid":
                    val = Generated_icons.get(13);
                    break;
                case "lock":
                    val = Generated_icons.get(14);
                    break;
                case "notifications":
                    val = Generated_icons.get(15);
                    break;
                case "rotation":
                    val = Generated_icons.get(16);
                    break;
                case "saved_shortcuts":
                    val = Generated_icons.get(17);
                    break;
                case "search":
                    val = Generated_icons.get(18);
                    break;
                case "settings":
                    val = Generated_icons.get(19);
                    break;
                case "system_info":
                    val = Generated_icons.get(20);
                    break;
                case "theme_mod":
                    val = Generated_icons.get(21);
                    break;
                case "user_panel":
                    val = Generated_icons.get(22);
                    break;
                case "volume":
                    val = Generated_icons.get(23);
                    break;
                case "wifi":
                    val = Generated_icons.get(24);
                    break;
            }
            return val;
        }

        public static String getColors(String data, String color) {
            Generated_colors = new Gson().fromJson(FileUtil.readFile(color), new TypeToken<ArrayList<String>>() {
            }.getType());
            Generated_colors.clear();
            Generated_colors = new Gson().fromJson(FileUtil.readFile(color), new TypeToken<ArrayList<String>>() {
            }.getType());
            String val = Generated_colors.get(0);

            switch (data) {
                case "MK_TH_COLOR_A":
                    val = Generated_colors.get(0);
                    break;
                case "MK_TH_COLOR_B":
                    val = Generated_colors.get(1);
                    break;
                case "MK_TH_COLOR_D":
                    val = Generated_colors.get(2);
                    break;
                case "MK_TH_COLOR_P":
                    val = Generated_colors.get(3);
                    break;
                case "MK_TH_COLOR_S":
                    val = Generated_colors.get(4);
                    break;
                case "MK_TH_COLOR_T":
                    val = Generated_colors.get(5);
                    break;
            }

            return val;
        }

        public static String getWallpaper(String data, String wallpaper) {
            Generated_wallpaper = new Gson().fromJson(FileUtil.readFile(wallpaper), new TypeToken<ArrayList<String>>() {
            }.getType());
            Generated_wallpaper.clear();
            Generated_wallpaper = new Gson().fromJson(FileUtil.readFile(wallpaper), new TypeToken<ArrayList<String>>() {
            }.getType());
            String val = Generated_wallpaper.get(0);

            if (data.equals("MK_TH_WALLPAPER_D")) {
                val = Generated_wallpaper.get(0);
            } else {
                val = "void";
            }

            return val;
        }
    }
}