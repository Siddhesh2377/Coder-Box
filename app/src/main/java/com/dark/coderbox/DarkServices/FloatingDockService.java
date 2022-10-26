package com.dark.coderbox.DarkServices;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.dark.coderbox.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FloatingDockService extends TileService {


    @Override
    public void onClick() {
        Intent i = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            i = new Intent(getApplicationContext(), MainActivity.class);
        }
        i.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        super.onClick();
    }
}
