package com.grability.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Edgar C on 27/1/2017.
 * www.Carrera-Brothers.com
 */

public class ProductDetails extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.product_details);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Button button = (Button) findViewById(R.id.closeDetails);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null){
            String image =  intent.getStringExtra("app_image");
            String title = intent.getStringExtra("app_title");
            String desc =  intent.getStringExtra("app_desc");
            String price = intent.getStringExtra("app_price");

            ImageView imageView = (ImageView) findViewById(R.id.app_single_icon);
            TextView textTitle = (TextView) findViewById(R.id.app_single_title);
            TextView textDesc = (TextView) findViewById(R.id.app_single_desc);
            TextView textPrice = (TextView) findViewById(R.id.app_single_price);

            Picasso.with(getApplicationContext()).load(image).into(imageView);
            textTitle.setText(title);
            textDesc.setText(desc);
            textPrice.setText(price);
        }
    }
}
