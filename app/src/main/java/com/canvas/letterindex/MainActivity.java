package com.canvas.letterindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView letterText;
    private LetterIndex letterIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        letterText = findViewById(R.id.select_position);
        letterIndex = findViewById(R.id.letter);
        letterIndex.setListener(new LetterIndex.ShowSelectLetter() {
            @Override
            public void setText(String text,boolean isShow) {
                if (isShow) {
                    letterText.setVisibility(View.VISIBLE);
                    letterText.setText(text);
                }else{
                    letterText.setVisibility(View.GONE);
                }
            }
        });
    }
}
