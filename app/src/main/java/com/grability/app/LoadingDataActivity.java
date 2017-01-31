package com.grability.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoadingDataActivity extends AppCompatActivity {

    private String TAG = LoadingDataActivity.class.getSimpleName();
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private JsonToFeed fetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_data);

        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        getSupportActionBar().hide();

        ImageView rotateImage = (ImageView) findViewById(R.id.loadingCircle);
        final Animation animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        rotateImage.startAnimation(animRotate);

        mSettings = getSharedPreferences("ProductsDB", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

        if (ConnectivityReceiver.isConnected(getApplicationContext())) {
            fetcher = new JsonToFeed();
            fetcher.execute();
        } else {
            String jsonStr = GetDB();
            Log.e(TAG, "Response from device");
            if (jsonStr.equals(null) || jsonStr.equals("") || jsonStr.equals("ProductsDB"))
                endActivity();
            else
                onNextActivity();
        }
    }

    private class JsonToFeed extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
            SetDB(jsonStr);
            Log.e(TAG, "Response from url");

            onNextActivity();
            return null;
        }
    }

    private void onNextActivity() {
        Intent intent = new Intent(LoadingDataActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        LoadingDataActivity.this.finish();
    }

    private void endActivity() {
        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
        intent.putExtra("CurrentClass", this.getLocalClassName());
        startActivity(intent);
        LoadingDataActivity.this.finish();
    }

    private void SetDB(String data) {
        mEditor = mSettings.edit();
        mEditor.putString("ProductsKey", data);
        mEditor.commit();
    }

    private String GetDB() {
        return mSettings.getString("ProductsKey", "ProductsDB");
    }
}