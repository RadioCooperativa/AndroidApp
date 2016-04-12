package com.example.innova6.cooperativa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;


public class MainActivity extends Activity  {
    static MediaPlayer mPlayer;
    ImageView buttonPlay;
    ImageView buttonPause;
    LinearLayout lnly_barra_player;
    ProgressBar pgrbarr;
    public String lk;
    public static boolean flag = false;

    //Declaración de las tareas ejecutadas en segundo plano
    //tarea1-> inicialización del player al cargar el activity
    private MiTareaAsincrona tarea1;

    //tarea2-> mostrar loading al cargar url del mplayer
    private MiTareaAsincrona_2 tarea2;

    String url = "http://tunein.digitalproserver.com/cooperativa.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        buttonPlay = (ImageView) findViewById(R.id.play);
        buttonPause = (ImageView) findViewById(R.id.pause);
        pgrbarr=(ProgressBar) findViewById(R.id.progressBar);

        buttonPause.setVisibility(View.INVISIBLE);
        buttonPlay.setVisibility(View.INVISIBLE);
        pgrbarr.setVisibility(View.VISIBLE);


        //Se define para agregar imagen SVG de barra en caso de telefonos con S.O > API 11
        lnly_barra_player=(LinearLayout) findViewById(R.id.lnly_barra_player);


        populateWebView();

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        tarea2=new MiTareaAsincrona_2();
        tarea2.execute();

        //Bloque de codigo para el streaming al presionar play
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgrbarr.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.INVISIBLE);
                tarea1 = new MiTareaAsincrona();
                tarea1.execute();

            }
        });

        //Bloque de codigo para el streaming al presionar pause
        buttonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonPause.setVisibility(View.INVISIBLE);
                buttonPlay.setVisibility(View.VISIBLE);
                if (mPlayer != null && mPlayer.isPlaying()) {

                    mPlayer.pause();

                }
            }
        });
    }

    public class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pgrbarr.setVisibility(View.INVISIBLE);
            buttonPause.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                mPlayer.reset();
                mPlayer.setDataSource(url);
                mPlayer.prepare();
                mPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    private class MiTareaAsincrona_2 extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            buttonPlay.setVisibility(View.INVISIBLE);
            buttonPause.setVisibility(View.VISIBLE);
            pgrbarr.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(url);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    private void populateWebView() {
        lk= "http://m.cooperativa.cl";
        //Log.i("La url ", "es: " + lk);
        final WebView myWebView;

        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(lk);

        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.contains("#") && flag == false) {
                    myWebView.loadUrl(url);
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }
    protected void onPause() {
        super.onPause();
    }
    protected void onResume() {
        super.onResume();
    }
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        final WebView webView;
        webView = (WebView)findViewById(R.id.webView);

        if (webView.canGoBack()) {
            webView.goBack();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Estás seguro que deseas salir de Cooperativa?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
