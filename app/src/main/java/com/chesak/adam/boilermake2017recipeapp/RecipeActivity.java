package com.chesak.adam.boilermake2017recipeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecipeActivity extends AppCompatActivity {

    public EditText ingredientsEditText;
    public EditText stepsEditText;
    public Context appContext;
    public Reader reader;

    private Button btnNext;
    private Button btnPrevious;

    public int currentLine = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);

        appContext = getApplicationContext();
        reader = new Reader(appContext);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        String text = (String) intent.getSerializableExtra("text");

        setTitle(recipe.getTitle());

        TextView titleText = (TextView) findViewById(R.id.recipe_title);
        TextView ingredientsText = (TextView) findViewById(R.id.recipe_ingredients);
        TextView dataText = (TextView) findViewById(R.id.recipe_data);

        titleText.setText(recipe.getTitle());

        //set the following control to the text from the bitmap, not the hardcoded values

//        ingredientsText.setText(recipe.getIngredientsString());
//        dataText.setText(recipe.getStepsString());
        ingredientsText.setText("Figure out someway to differentiate the ingredients from the steps. <RecipeActivity.java> \n");
        dataText.setText(text);

        final String[] textArray = breakTextIntoArray(text);

        //when an <ACTION> occures, read a line
        //an action will be a button for now, but should ultimately be the voice command "Next"
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLine++;
                readLine(textArray);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLine >-1){
                    currentLine--;
                    readLine(textArray);
                }
            }
        });

    }

    public String[] breakTextIntoArray(String text){
        return text.split("\\r?\\n+");
    }

    public void readLine(String[] text){
        if (currentLine < text.length){
            reader.speak(text[currentLine]);
        }
    }
}
