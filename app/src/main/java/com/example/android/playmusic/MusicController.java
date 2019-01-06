package com.example.android.playmusic;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c){
        super(c);
    }

    public void hide(){}
    @Override
    public void show(int timeout) {
        super.show(0);
    }
}
