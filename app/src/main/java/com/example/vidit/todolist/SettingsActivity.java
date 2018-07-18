package com.example.vidit.todolist;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity
{
    CheckBox checkBox;
    ConstraintLayout layout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        layout=findViewById(R.id.layout);
        checkBox = findViewById(R.id.checkBox);
        PackageManager pm=SettingsActivity.this.getPackageManager();
        int hasPerm=pm.checkPermission(android.Manifest.permission.READ_SMS,SettingsActivity.this.getPackageName());
        if(hasPerm==PackageManager.PERMISSION_GRANTED)
        {
            checkBox.setChecked(true);
        }
        else if(hasPerm==PackageManager.PERMISSION_DENIED)
        {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    if (!(ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)) {

                        String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
                        ActivityCompat.requestPermissions(SettingsActivity.this, permissions, 10);
                    }
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==10)
        {
            int smsReadPermission=grantResults[0];
            int smsReceivePermission=grantResults[1];
            if(smsReadPermission==PackageManager.PERMISSION_GRANTED && smsReceivePermission==PackageManager.PERMISSION_GRANTED)
            {
                checkBox.setChecked(true);
                checkBox.setEnabled(false);
                Snackbar.make(layout, "Permissions Granted", Snackbar.LENGTH_LONG).show();
            }
            else
            {
                checkBox.setChecked(false);
                Snackbar.make(layout,"Permission not granted",Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
