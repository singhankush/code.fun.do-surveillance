package codeplay.thereissecurity;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;


public class SurveillenceActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST=9999;
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    boolean interrupt=false;
    public static int count=0;
    final int delay=5000;
    Camera camera;
    SurfaceView surfaceView;
    Camera.PictureCallback callback;
    SurfaceHolder holder;
    ClarifaiClient client;
    ConceptModel model;
    int attributesCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveillence);
        handler=new Handler();
        client=MainActivity.getClient();
        model=client.getDefaultModels().generalModel();
        imageView= (ImageView) findViewById(R.id.captured_image);
        surfaceView= (SurfaceView) findViewById(R.id.camera_view);
        holder=surfaceView.getHolder();
        camera=Camera.open();
        callback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //camera.stopPreview();

                //Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //imageView.setImageBitmap(bitmap);
                //imageView.invalidate();
                processImage(bytes);
            }
        };
        Button stop= (Button) findViewById(R.id.stop_survey);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interrupt=true;
                close();
            }
        });
        runnable= new Runnable() {
            @Override
            public void run() {
                if (holder.getSurface() == null) {
                    // preview surface does not exist
                    return;
                }

                // stop preview before making changes
                try {
                    camera.stopPreview();
                } catch (Exception e) {
                    // ignore: tried to stop a non-existent preview
                }

                // set preview size and make any resize, rotate or
                // reformatting changes here

                // start preview with new settings
                try {
                    camera.setPreviewDisplay(holder);
                    camera.setDisplayOrientation(90);
                    camera.startPreview();
                    camera.takePicture(null, null, callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this,delay);
            }
        };
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                runnable.run();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (camera!=null)
                    camera.stopPreview();
            }
        });
        //handler.postDelayed(runnable, delay);

    }
    public void processImage(byte[] bytes){
        new AsyncTask<byte[], Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {


            @Override
            protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(byte[]... params) {
                // Use this model to predict, with the image that the user just selected as the input
                return model.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(params[0])))
                        .executeSync();
            }

            @Override
            protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    return;
                }

                for(ClarifaiOutput<Concept> output:predictions){
                    for(Concept c:output.data() ){
                        int i=Detection.check(c.name());
                        if (i>0) {
                            try {
                                Toast.makeText(SurveillenceActivity.this, "Image Matched", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){

                            }
                            attributesCount+=i;
                            Log.i("Name:Confidence", c.name() + ":" + c.value());
                        }
                    }
                }
                if (attributesCount>=2){
                    attributesCount=0;
                    Detection.notify_detected(SurveillenceActivity.this);
                }
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bytes);
    }

    public void close(){
        if (camera!=null)
            camera.release();
        camera=null;
        handler.removeCallbacks(runnable);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surveillence, menu);
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
