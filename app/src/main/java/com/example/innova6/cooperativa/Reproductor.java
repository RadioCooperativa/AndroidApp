package com.example.innova6.cooperativa;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * Created by innova6 on 13-03-2017.
 */

public class Reproductor extends MainActivity {
    public static final String U_R_L = "http://unlimited3-cl.dps.live/cooperativafm/aac/icecast.audio";
    public  MediaPlayer mediaPlayer = new MediaPlayer();
    public  ImageButton buttonPlay;
    ImageButton buttonPause;
    ProgressBar pgrbarr;
    private MediaController mController;
    public Reproductor() {
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleIntent(Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();

        if( action.equalsIgnoreCase( U_R_L ) ) {
           // mController.getTransportControls().play();
            reproducir();
        }
    }

    public void reproducir(){

        buttonPlay = (ImageButton) buttonPlay.findViewById(R.id.play);
        buttonPause = (ImageButton) buttonPause.findViewById(R.id.pause);
        try {

            mediaPlayer.setDataSource(this.U_R_L);
            mediaPlayer.prepareAsync();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //mp3 will be started after completion of preparing...
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer meplayer) {
                Log.i("Entra a onPrepared ","mPlayer.prepareAsync()");
                meplayer.setVolume(0,0);
                buttonPlay.setVisibility(View.VISIBLE);
                buttonPause.setVisibility(View.INVISIBLE);
                pgrbarr.setVisibility(View.INVISIBLE);
                meplayer.start();

            }

        });


    }
}
