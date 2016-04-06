package com.example.innova6.cooperativa;

/**
 * Created by innova6 on 06-04-2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Splash extends Activity {
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        progressbar = (ProgressBar) findViewById(R.id.mi_loading_splash);
        new Sincronico(this).execute();
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }
    private class Sincronico extends AsyncTask<Void, Integer, Boolean> {

        public Sincronico(Splash splash) {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i=1; i<=5; i++) {
                tareaLarga();
                publishProgress(i*20);
                if(isCancelled())
                    break;
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            progressbar.setProgress(progreso);
        }
        @Override
        protected void onPreExecute() {
            progressbar.setMax(100);
            progressbar.setProgress(0);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(Splash.this, "Abriendo Menú", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Splash.this, MainActivity.class); //instanciamos el objeto Intent y lo inicializamos desde splash hasta MainActivity
            startActivity(intent);
            finish();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(Splash.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}
