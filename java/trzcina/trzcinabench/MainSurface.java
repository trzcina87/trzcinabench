package trzcina.trzcinabench;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainSurface extends SurfaceView {

    public SurfaceHolder surfaceholder;
    public volatile boolean gotowe;

    public MainSurface(MainActivity activity) {
        super(activity);
        surfaceholder = getHolder();
        gotowe = false;
        surfaceholder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gotowe = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

}
