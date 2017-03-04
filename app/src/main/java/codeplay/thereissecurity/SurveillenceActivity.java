package codeplay.thereissecurity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


public class SurveillenceActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST=9999;
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    boolean interrupt=false;
    public static int count=0;
    final int delay=8000;
    Camera camera;
    SurfaceView surfaceView;
    Camera.PictureCallback callback;
    SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveillence);
        handler=new Handler();
        imageView= (ImageView) findViewById(R.id.captured_image);
        surfaceView= (SurfaceView) findViewById(R.id.camera_view);
        holder=surfaceView.getHolder();
        camera=Camera.open();
        callback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                camera.stopPreview();
                Bitmap bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
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
                /*try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    capture();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                runnable.run();
                //handler.postDelayed(runnable,delay);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        //handler.postDelayed(runnable, delay);
    }
    public void capture(){

    }

    /*public void capture() {

        if (camera != null) {
            try {

                camera.setPreviewDisplay(holder);
                camera.startPreview();
                camera.takePicture(null, null, callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //camera.stopPreview();
        }
        else
            close();
    }*/

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==CAMERA_REQUEST && resultCode== Activity.RESULT_OK){
            count++;
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
*/
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
