package com.leandrodss.stationtank.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
    }
}
