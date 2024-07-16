package com.zybooks.starsurge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {

    // ImageButton helpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        public void onStartGameClick (View view){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}