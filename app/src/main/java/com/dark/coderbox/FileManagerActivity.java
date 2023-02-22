package com.dark.coderbox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.dark.coderbox.DarkServices.DarkUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FileManagerActivity extends AppCompatActivity {

    public ChipGroup filter;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filemanager_main);

        filter = findViewById(R.id.filter);

        filter.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                    Chip chip = filter.findViewById(filter.getCheckedChipId());
                    DarkUtils.ShowMessage(new StringBuilder(chip.getText().toString()), FileManagerActivity.this);
            }
        });
    }
}