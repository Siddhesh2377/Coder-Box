package com.dark.coderbox.DarkServices;

import static com.dark.coderbox.libs.FileUtil.getExternalStorageDir;
import static com.dark.coderbox.libs.LanguageXR.READ_XR_DATA;
import static com.dark.coderbox.libs.Setup.THEMES.GetThemeInfo;
import static com.dark.coderbox.libs.Setup.THEMES.getCurrentTheme;

import com.dark.coderbox.libs.Keys;

public class EnvPathVariables {
    public static String SYSTEM_DATA_FILE = getExternalStorageDir().concat("/CBRoot/CBRData/SystemData/system.xr");
    public static String SYSTEM_DATA_FOLDER = getExternalStorageDir().concat("/CBRoot/CBRData/SystemData");
    public static String THEME_PATH_FILE = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/Path.json");
    public static String THEMES_FOLDER = getExternalStorageDir().concat("/CBRoot/SYSTEM/THEMES/");
    public static String SYSTEM_FOLDER = getExternalStorageDir().concat("/CBRoot");
    public static String SYSTEM_THEME = READ_XR_DATA(Keys.MODS.SYSTEM_XR, Keys.XRLanguages.ThemeProvider, SYSTEM_DATA_FILE);
    public static String APPLIED_THEME = getCurrentTheme();
    public static String COLOR_ACCENT = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Accent);
    public static String COLOR_BASE = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Base);
    public static String COLOR_DOMINANT = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Dominant);
    public static String COLOR_PRIMARY = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Primary);
    public static String COLOR_SECONDARY = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Secondary);
    public static String COLOR_TERTIARY = GetThemeInfo(SYSTEM_THEME, Keys.MODS.THEME_COLORS, Keys.THEMES.ThemeColors.Tertiary);
}
