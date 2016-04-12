package com.example.innova6.cooperativa;

/**
 * Created by innova6 on 06-04-2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;


public class Splash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Create a new ImageView
        ImageView imageView = new ImageView(this);

        // Set the background color to white
        imageView.setBackgroundColor(Color.WHITE);

        // Parse the SVG file from the resource
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.cooperativacllogo);
         // Get a drawable from the parsed SVG and set it as the drawable for the ImageView

        imageView.setImageDrawable(svg.createPictureDrawable());

        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Set the ImageView as the content view for the Activity
        setContentView(imageView);


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
            //int progreso = values[0].intValue();
          //  progressbar.setProgress(progreso);
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Boolean result) {
           // Toast.makeText(Splash.this, "Abriendo Menú", Toast.LENGTH_SHORT).show();
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
