package com.example.android.playmusic;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.d(this.getClass().getName(),"DispACH");
            super.hide();
            Context c = getContext();
            ((Activity) c).finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
