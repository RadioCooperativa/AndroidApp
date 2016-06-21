package com.example.innova6.cooperativa;
/*Desarrollado por Rodrigo A Vargas Sanhueza para Radio Cooperativa - Abril del 2016*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.IOException;


public class MainActivity extends Activity {
    static MediaPlayer mPlayer;
    ImageButton buttonPlay;
    ImageButton buttonPause;


    ProgressBar pgrbarr;
    //public String lk;
    //public static boolean flag = false;

    //*******Declaración de las tareas ejecutadas en segundo plano*****//

    //tarea1-> inicialización del player al presionar play, ademas de trabajar con el progressbar
    private MiTareaAsincrona tarea1;

    //tarea2-> inicialización del player al arrancar app
    private MiTareaAsincrona_2 tarea2;

    private Tracker mTracker;

       String url = "http://tunein.digitalproserver.com/cooperativa.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// evita que se gire la pantalla


        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]


        //Se define para agregar imagen SVG de barra en caso de telefonos con S.O > API 11
        if (android.os.Build.VERSION.SDK_INT>=11) {
            setContentView(R.layout.activity_main);

            buttonPlay = (ImageButton) findViewById(R.id.play);
            buttonPause = (ImageButton) findViewById(R.id.pause);
            pgrbarr=(ProgressBar) findViewById(R.id.progressBar);


            buttonPause.setVisibility(View.INVISIBLE);
            buttonPlay.setVisibility(View.VISIBLE);

            pgrbarr.setVisibility(View.INVISIBLE);

            ImageView imageView = (ImageView) findViewById(R.id.binferior);//imageview de barra inferior
            ImageView imageView_play= (ImageView) findViewById(R.id.play);//imageview de boton play
            ImageView imageView_pause= (ImageView) findViewById(R.id.pause);//imageview de boton pause

            SVG homeSvg = SVGParser.getSVGFromResource(getResources(), R.raw.rep); //Parseo de imagen de barra inferior
            SVG homeSvg_play = SVGParser.getSVGFromResource(getResources(), R.raw.play); //Parseo de imagen de boton play
            SVG homeSvg_pause = SVGParser.getSVGFromResource(getResources(), R.raw.pause); //Parseo de imagen de boton stop

            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);   //activa la aceleracion de hw
            imageView_play.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //activa la aceleracion de hw
            imageView_pause.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //activa la aceleracion de hw

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView_play.setAdjustViewBounds(true);
            imageView.setImageDrawable(homeSvg.createPictureDrawable());

            imageView_play.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView_play.setAdjustViewBounds(true);
            imageView_play.setImageDrawable(homeSvg_play.createPictureDrawable());

            imageView_pause.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView_pause.setAdjustViewBounds(true);
            imageView_pause.setImageDrawable(homeSvg_pause.createPictureDrawable());
        }else{
            setContentView(R.layout.activity_main_bajo);

            buttonPlay = (ImageButton) findViewById(R.id.play);
            buttonPause = (ImageButton) findViewById(R.id.pause);
            pgrbarr=(ProgressBar) findViewById(R.id.progressBar);

            buttonPause.setVisibility(View.INVISIBLE);
            buttonPlay.setVisibility(View.VISIBLE);
            pgrbarr.setVisibility(View.INVISIBLE);
            //par.setVisibility(View.VISIBLE);
        }
        /************** Módulos de muestra de webview validación de conectividad y validación de versión app***************/
        //populateWebView();
        valida_version();
        estaConectado();
        /************** /Módulos de muestra de webview validación de conectividad y validación de versión app***************/

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

       // tarea2 = new MiTareaAsincrona_2();
       // tarea2.execute();

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
    private void valida_version() {

        final WebView myBrowser;
        myBrowser = (WebView)findViewById(R.id.webView);


        //myBrowser.setWebViewClient(new WebViewClient());
        //myBrowser.setWebChromeClient(new WebChromeClient());

        myBrowser.setWebViewClient(new WebViewClientExternal());

        myBrowser.getSettings().setDomStorageEnabled(true);
        myBrowser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        myBrowser.getSettings().setJavaScriptEnabled(true);



      myBrowser.loadUrl("http://m.cooperativa.cl");

          myBrowser.setWebChromeClient(new WebChromeClient() {

        });

      /*  myBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
               // Toast.makeText(WebView, "Oh no! " + description, Toast.LENGTH_SHORT).show();
           Log.i("Error i", " ");

            /*  public void onPageFinished(WebView view, String url) {
              url= "http://coop.janus.cl/cooperativa/stat/portada/portada.html";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                // String msgToSend = "x";
               // Log.i("llego a onpage","si");

               // myBrowser.loadUrl("http://coop.janus.cl/cooperativa/stat/portada/portada.html");
            }


        });*/
    }
    protected void onPause() {
        super.onPause();
    }
    protected void onResume() {
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean estaConectado(){
        if(conectadoWifi()){
            // showAlertDialog(Main.this, "Conexion a Internet",
            //   "Tu Dispositivo tiene Conexion a Wifi.", true);

            Toast.makeText(getApplicationContext(), "Estás conectado a través de Wifi", Toast.LENGTH_LONG).show();
            return true;
        }else{
            if(conectadoRedMovil()){
                //showAlertDialog(Main.this, "Conexion a Internet",
                //      "Tu Dispositivo tiene Conexion Movil.", true);

                Toast.makeText(getApplicationContext(), "Actualmente estás usando tus datos móviles. Te recomendamos utilizar Wifi", Toast.LENGTH_LONG).show();
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "No tienes conectividad a internet. Para usar la aplicación necesitas estar conectado", Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(this, SinConexion.class);
                startActivity(intent);*/
                Intent myIntent = new Intent(MainActivity.this, SinConexion.class);
                startActivityForResult(myIntent, 0);
                return false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
    }

    public class WebViewClientExternal extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
       // boolean url_sitio=Uri.parse(url).getHost().endsWith(view.getResources().getString(R.string.frag_web_root));
           // String url_sitio=Uri.parse(url).getPath().endsWith(view.getResources().getString(R.string.excluye_web_root));

           if ( Uri.parse(url).getHost().endsWith(view.getResources().getString(R.string.excluye_web_root))) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                Log.i("Entra a if ","WebViewClientExternal_1");
                return false;
            } else {
                if (Uri.parse(url).getHost().endsWith(view.getResources().getString(R.string.frag_web_root))) {
                    Log.i("Entra a if ","WebViewClientExternal_2");

                }
                return true;
            }

            }

    }
}