package com.zybooks.starsurge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        // Bringing the score to this screen
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);


        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText(String.valueOf(score));
    }

    public void onReplayClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences("score_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("score", 0);
        editor.apply();

    }

    public void onMenuClick(View view) {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences("score_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("score", 0);
        editor.apply();
    }
}