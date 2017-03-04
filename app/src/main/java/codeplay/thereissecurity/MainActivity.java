package codeplay.thereissecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiUtil;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;


public class MainActivity extends ActionBarActivity {

    @Nullable
    private static ClarifaiClient client;

    @NonNull
    public static ClarifaiClient getClient(){
        //final ClarifaiClient client = client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client=new ClarifaiBuilder(getString(R.string.client_id),getString(R.string.client_secret))
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger(){
                            @Override
                            public void log(String message) {
                                Timber.e(message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .buildSync();

        Timber.plant(new Timber.DebugTree());

        Button bt= (Button) findViewById(R.id.start_survey);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SurveillenceActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
