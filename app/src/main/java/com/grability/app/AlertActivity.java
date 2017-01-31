package com.grability.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AlertActivity extends Activity {

    Intent intent;
    Button check;
    Button ignore;
    ImageView img;
    static String currentClass;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setFinishOnTouchOutside(false);


        setContentView(R.layout.activity_alert);

        context = getApplicationContext();

        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        intent = getIntent();
        currentClass = intent.getExtras().getString("CurrentClass");

        check = (Button) findViewById(R.id.alert_check);
        ignore = (Button) findViewById(R.id.alert_continue);
        img = (ImageView) findViewById(R.id.alert_img_loading);

        final Animation animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        img.startAnimation(animRotate);

        if (currentClass.equals("MainActivity")) {
            check.setVisibility(View.VISIBLE);
            ignore.setVisibility(View.VISIBLE);
        } else if (currentClass.equals("LoadingDataActivity")) {
            check.setVisibility(View.VISIBLE);
            ignore.setVisibility(View.INVISIBLE);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!alreadyCalled){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {Toast.makeText(context, "Verificando acceso a Internet.", Toast.LENGTH_SHORT);}
                    });
                    img.setVisibility(View.VISIBLE);
                    GoRunnable();
                }
            }
        });

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { Toast.makeText(context, "Comprobaremos tu conexión en unos minutos.", Toast.LENGTH_SHORT);}
                });
                MainActivity.RunHandler(300000);
                AlertActivity.this.finish();
            }
        });
    }
    boolean alreadyCalled = false;
    Handler h = new Handler();
    Runnable runnable;
    private void GoRunnable() {
        if(!alreadyCalled) {
            alreadyCalled = true;
            final int checkTime = 3000;

            h.postDelayed(new Runnable() {

                public void run() {
                    runnable = this;
                    h.postDelayed(this, checkTime);
                    if (ConnectivityReceiver.isConnected(context)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() { Toast.makeText(context, "Acceso Internet completado!", Toast.LENGTH_SHORT);}
                        });

                        if (currentClass == "MainActivity") {
                            MainActivity.RunHandler(5000);
                            AlertActivity.this.finish();
                        } else if (currentClass.equals("LoadingDataActivity")) {
                            Intent intent = new Intent(context, LoadingDataActivity.class);
                            startActivity(intent);
                            AlertActivity.this.finish();
                        }
                    } else {
                        img.setVisibility(View.INVISIBLE);
                        if (currentClass.equals("MainActivity"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { Toast.makeText(context, "No hay acceso a Internet.", Toast.LENGTH_SHORT);}
                            });
                        else if (currentClass.equals("LoadingDataActivity"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { Toast.makeText(context, "Necesitas una conexión a Internet para continuar.", Toast.LENGTH_SHORT);}
                            });
                    }
                    h.removeCallbacks(runnable);
                    alreadyCalled = false;
                }
            }, checkTime);
        }
    }
}