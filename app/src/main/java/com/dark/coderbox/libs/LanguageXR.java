package com.dark.coderbox.libs;

import static com.dark.coderbox.DarkServices.EnvPathVariables.SYSTEM_DATA_FOLDER;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class LanguageXR {

    public static ArrayList<String> Generated_Lines_List = new ArrayList<>();
    public static ArrayList<String> Generated_Data_Lines_List = new ArrayList<>();


    public static String READ_XR_DATA(String mod, String keys, String str) {
        String s = "";
        if (FileUtil.isExistFile(str)) {
            Generated_Lines_List.clear();
            Generated_Lines_List = new Gson().fromJson(FileUtil.readFile(str), new TypeToken<ArrayList<String>>() {
            }.getType());

            if (Generated_Lines_List.size() == 0) {
                s = "No Data Found";
            } else {
                if (mod.equals("MK.SYS576845.XR")) {
                    //Mod --> SYSTEM_XR
                    if (keys.equals("MK_SYSTEM_TP.10.06.(){}")) {
                        s = Generated_Lines_List.get(0);
                    } else {
                        if (keys.equals("MK_SYSTEM_GTC.66.09.(){}")) {
                            s = Generated_Lines_List.get(1);
                        } else {
                            if (keys.equals("MK_SYSTEM_BRIGHT.96.79.(){}")) {
                                s = Generated_Lines_List.get(2);
                            } else {
                                if (keys.equals("MK_SYSTEM_VOLUME.34.65.(){}")) {
                                    s = Generated_Lines_List.get(3);
                                }
                            }
                        }
                    }

                    s = s.substring(s.indexOf("C: ") + 3);
                    s = s.substring(0, s.indexOf(" DATA.CLOSE"));
                } else {
                    if (mod.equals("MK.TH432876.XR")) {
                        //Mod --> THEMES_XR
                        if (keys.equals("MK_DATA_BBR.02.27.(){}")) {
                            s = Generated_Lines_List.get(0);
                        } else {
                            if (keys.equals("MK_SYSTEM_TP.10.06.(){}")) {
                                s = Generated_Lines_List.get(1);
                            } else {
                                if (keys.equals("MK_DATA_TC.80.86.(){}")) {
                                    s = Generated_Lines_List.get(2);
                                } else {
                                    if (keys.equals("MK_DATA_DM.10.06.(){}")) {
                                        s = Generated_Lines_List.get(3);
                                    } else {
                                        if (keys.equals("MK_DATA_FM.03.27.(){}")) {
                                            s = Generated_Lines_List.get(4);
                                        } else {
                                            if (keys.equals("MK_DATA_GC.67.06.(){}")) {
                                                s = Generated_Lines_List.get(5);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        s = s.substring(s.indexOf("C: ") + 1);
                        s = s.substring(0, s.indexOf(" DATA.CLOSE"));
                    }
                }
            }
        } else {
            s = "";
        }

        return s;
    }

    public static void WriteSystemInfo(String path, String mod, String keys, String Data, Context context) {
        if (FileUtil.getFileLength(path) == 0) {
            Toast.makeText(context, "Error : No Related Data Found", Toast.LENGTH_LONG).show();
        } else {
            Generated_Data_Lines_List.clear();
            Generated_Data_Lines_List = new Gson().fromJson(FileUtil.readFile(path), new TypeToken<ArrayList<String>>() {
            }.getType());

            if (mod.equals("MK.SYS576845.XR")) {
                //Mod --> SYSTEM_XR
                if (keys.equals("MK_SYSTEM_TP.10.06.(){}")) {
                    ReplaceItems(Generated_Data_Lines_List, 0, Data);
                } else {
                    if (keys.equals("MK_SYSTEM_GTC.66.09.(){}")) {
                        ReplaceItems(Generated_Data_Lines_List, 1, Data);
                    } else {
                        if (keys.equals("MK_SYSTEM_BRIGHT.96.79.(){}")) {
                            ReplaceItems(Generated_Data_Lines_List, 2, Data);
                        } else {
                            if (keys.equals("MK_SYSTEM_VOLUME.34.65.(){}")) {
                                ReplaceItems(Generated_Data_Lines_List, 3, Data);
                            }
                        }
                    }
                }
            }
            FileUtil.writeFile(SYSTEM_DATA_FOLDER.concat("/").concat("system.xr"), "");
            FileUtil.writeFile(SYSTEM_DATA_FOLDER.concat("/").concat("system.xr"), new Gson().toJson(Generated_Data_Lines_List));
        }
    }

    public static void ReplaceItems(ArrayList<String> data, int pos, String str) {
        data.remove(pos);
        data.add(pos, str);
    }

}
