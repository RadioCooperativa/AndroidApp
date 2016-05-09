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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.winsontan520.wversionmanager.library.OnReceiveListener;
import com.winsontan520.wversionmanager.library.WVersionManager;

import java.io.IOException;


public class MainActivity extends Activity  {
    static MediaPlayer mPlayer;
    ImageView buttonPlay;
    ImageView buttonPause;

    ProgressBar pgrbarr;
    public String lk;
    public static boolean flag = false;

    //*******Declaración de las tareas ejecutadas en segundo plano*****//

    //tarea1-> inicialización del player al cargar el activity
    private MiTareaAsincrona tarea1;

    //tarea2-> mostrar loading al cargar url del mplayer
    private MiTareaAsincrona_2 tarea2;

    String url = "http://unlimited1-us.digitalproserver.com/cooperativafm/mp3/icecast.audio";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// evita que se gire la pantalla


        //*******Aplicación de Libreria WVersionManager para comprobación de actualizaciones e informar al usuario*****//
        WVersionManager versionManager = new WVersionManager(this);
        versionManager.setVersionContentUrl("http://especiales2.cooperativa.cl/2016/pruebas/rvargas_test/actualiza_app.json");//archivo en servidor de radio, en donde se encuentran ls datos de la varios
        versionManager.checkVersion();

       /* Toast.makeText(getApplicationContext(), "Versión 3.4.3 con url de beta en setUpdateUrl", Toast.LENGTH_LONG).show();*/


        versionManager.setUpdateNowLabel("Si, Actualizar Ahora");
        versionManager.setRemindMeLaterLabel("No, Recuérdame más Tarde");
        versionManager.setIgnoreThisVersionLabel("No en este momento");
        versionManager.setUpdateUrl("https://play.google.com/apps/testing/com.ejemplo.cooperativa"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
        versionManager.setReminderTimer(1); // this mean checkVersion() will not take effect within 10 minutes

        versionManager.setOnReceiveListener(new OnReceiveListener() {
            @Override
            public boolean onReceive(int status, String result) {
                // implement your own compare logic here
                return false; // return true if you want to use library's default logic & dialog
            }
        });


        //******* /Aplicación de Libreria WVersionManager para comprobación de actualizaciones e informar al usuario*****//


        setContentView(R.layout.activity_main);

        buttonPlay = (ImageView) findViewById(R.id.play);
        buttonPause = (ImageView) findViewById(R.id.pause);
        pgrbarr=(ProgressBar) findViewById(R.id.progressBar);

        buttonPause.setVisibility(View.INVISIBLE);
        buttonPlay.setVisibility(View.INVISIBLE);
        pgrbarr.setVisibility(View.VISIBLE);


        //Se define para agregar imagen SVG de barra en caso de telefonos con S.O > API 11
               if (android.os.Build.VERSION.SDK_INT>=11) {

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
        }
        /************** Módulos de muestra de webview y validación de conectividad ***************/
        populateWebView();
        estaConectado();
        /************** /Módulos de muestra de webview y validación de conectividad ***************/

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
        // lk= "http://m.cooperativa.cl";
       lk="http://especiales2.cooperativa.cl/2016/pruebas/rvargas_test/prueba.php";
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
}
