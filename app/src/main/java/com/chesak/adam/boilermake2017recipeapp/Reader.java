package com.chesak.adam.boilermake2017recipeapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Reader {

    public Context appContext;
    public TextToSpeech speak;
    public final Locale language = Locale.US;

    public Reader(Context appContext){
        this.appContext = appContext;
        speak = new TextToSpeech(this.appContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speak.setLanguage(language);
                }
            }
        });
    }

    public void speak(String string){
        speak.speak(string, TextToSpeech.QUEUE_FLUSH, null);
    }

}
