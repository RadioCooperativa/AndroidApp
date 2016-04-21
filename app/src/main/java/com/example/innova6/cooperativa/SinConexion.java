package com.example.innova6.cooperativa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * Created by innova6 on 21-04-2016.
 */
public class SinConexion extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinconexion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// evita que se gire la pantalla

        if (android.os.Build.VERSION.SDK_INT >= 11) { //valida versión de API

            ImageView imageView = (ImageView) findViewById(R.id.imageView_sin_conexion);//imageview de sinconexion
            SVG homeSvg = SVGParser.getSVGFromResource(getResources(), R.raw.nosignal);

            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageView.setAdjustViewBounds(true);
            imageView.setImageDrawable(homeSvg.createPictureDrawable());

        } else {
            setContentView(R.layout.sinconexion);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
    }
}




