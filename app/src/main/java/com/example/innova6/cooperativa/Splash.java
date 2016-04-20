package com.example.innova6.cooperativa;

/**
 * Created by innova6 on 06-04-2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;


public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        if (android.os.Build.VERSION.SDK_INT>=11) {

            ImageView imageView = (ImageView) findViewById(R.id.imageView_splash);//imageview de splash
            SVG homeSvg = SVGParser.getSVGFromResource(getResources(), R.raw.cooperativacllogo);

            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
           // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageView.setImageDrawable(homeSvg.createPictureDrawable());

        }else {
            setContentView(R.layout.splash);
        }
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
            for(int i=1; i<=3; i++) {
                tareaLarga();

                if(isCancelled())
                    break;
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Boolean result) {
            //instanciamos el objeto Intent y lo inicializamos desde splash hasta MainActivity
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(Splash.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

}
