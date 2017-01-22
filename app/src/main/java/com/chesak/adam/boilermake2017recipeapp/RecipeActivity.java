package com.chesak.adam.boilermake2017recipeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    public Context appContext;
    public Reader reader;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private Button btnNext;
    private Button btnLast;
    private Button btnListen;
    private String[] textArray;
    public int currentLine = -1;

    TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnLast = (Button) findViewById(R.id.btnLast);
        btnListen = (Button) findViewById(R.id.btnListen);

        appContext = getApplicationContext();
        reader = new Reader(appContext);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        String text = (String) intent.getSerializableExtra("text");

        setTitle(recipe.getTitle());
        TextView titleText = (TextView)findViewById(R.id.recipe_title);
        titleText.setText(recipe.getTitle());

        this.textArray = breakTextIntoArray(text);

        int size = textArray.length;

        textViews = new TextView[size];
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        for (int i = 0; i < size; i++){
            textViews[i] = new TextView(this);
            textViews[i].setText(textArray[i]);
            textViews[i].setTextColor(0xFFBCBCBC);
            mainLayout.addView(textViews[i]);
        }

        //when an <ACTION> occures, read a line
        //an action will be a button for now, but should ultimately be the voice command "Next"
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLine < textArray.length - 1) {
                    currentLine++;
                    textViews[currentLine].setTextColor(0xFF000000);
                    textViews[currentLine].setTypeface(null, Typeface.BOLD);
                    if (currentLine >= 1){
                        textViews[currentLine - 1].setTextColor(0xFFBCBCBC);
                        textViews[currentLine - 1].setTypeface(null, Typeface.NORMAL);
                    }
                    reader.speak(textArray[currentLine]);
                } else {
                    reader.speak("You have finished cooking ");
                }
            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLine > 0) {
                    currentLine--;
                    textViews[currentLine].setTextColor(0xFF000000);
                    textViews[currentLine].setTypeface(null, Typeface.BOLD);
                    if (currentLine < textArray.length - 1){
                        textViews[currentLine + 1].setTextColor(0xFFBCBCBC);
                        textViews[currentLine + 1].setTypeface(null, Typeface.NORMAL);
                    }
                    reader.speak(textArray[currentLine]);
                } else if (currentLine == 0) {
                    reader.speak(textArray[0]);

                } else if (currentLine < 0) {
                    reader.speak("You went back too far!");
                }
            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });
    }

    public String[] breakTextIntoArray(String text) {
        return text.split("\\r?\\n+");
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.contains("next")||matches.contains("go on")) {
                if (currentLine < textArray.length - 1) {
                    currentLine++;
                    textViews[currentLine].setTextColor(0xFF000000);
                    textViews[currentLine].setTypeface(null, Typeface.BOLD);
                    if (currentLine >= 1){
                        textViews[currentLine - 1].setTextColor(0xFFBCBCBC);
                        textViews[currentLine - 1].setTypeface(null, Typeface.NORMAL);
                    }
                    reader.speak(textArray[currentLine]);
                } else {
                    reader.speak("You have finished cooking ");
                }
            }else if (matches.contains("last")||matches.contains("go back")){
                if (currentLine > 0) {
                    currentLine--;
                    textViews[currentLine].setTextColor(0xFF000000);
                    textViews[currentLine].setTypeface(null, Typeface.BOLD);
                    if (currentLine < textArray.length - 1){
                        textViews[currentLine + 1].setTextColor(0xFFBCBCBC);
                        textViews[currentLine + 1].setTypeface(null, Typeface.NORMAL);
                    }
                    reader.speak(textArray[currentLine]);
                } else if (currentLine == 0) {
                    reader.speak(textArray[0]);

                } else if (currentLine < 0) {
                    reader.speak("You went back too far!");
                }
            }
        }
    }
}