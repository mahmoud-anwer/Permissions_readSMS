package com.example.root.permissions_readsms;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView myMessages;
    Button loadButton;
    final int reqCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMessages = (TextView) findViewById(R.id.textViewMessages);
        loadButton = (Button) findViewById(R.id.buttonLoad);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buttonLoadMessages(View view) {
        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), Manifest.permission.READ_SMS)) {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS}, reqCode);
                }
            }
        }

        loadMessages();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case reqCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    loadMessages();
                else
                    // permission denied
                    break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void loadMessages() {
        String sms = "";
        try {
            Uri uriSMS = Uri.parse("content://sms/inbox");
            Cursor cur = getContentResolver().query(uriSMS, null, null, null, null);
            cur.moveToPosition(0);
            while (cur.moveToNext()) {
                sms += "From" + cur.getString(cur.getColumnIndex("address")) + cur.getString(cur.getColumnIndex("body")) + "\n";
            }
        } catch (Exception ex) {
        }
        myMessages.setText(sms);
    }
}
