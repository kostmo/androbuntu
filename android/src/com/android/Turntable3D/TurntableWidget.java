/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.Turntable3D;

import com.android.Turntable3D.GLSurfaceView;

import javax.microedition.khronos.opengles.GL;

import android.app.Activity;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.ContextMenu;
//import android.view.View;
import android.content.Context;

import android.view.KeyEvent;
import android.graphics.PixelFormat;
import android.hardware.SensorListener;
import android.hardware.SensorManager;


public class TurntableWidget extends Activity {

	
    private SensorManager mSensorManager;

	
	// Karl
	public float[] last_tap;

	
	UbuntuLogoRenderer spriterenderer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		
    	
		last_tap = new float[2];
		last_tap[0] = 0;
		last_tap[1] = 0;
		
        super.onCreate(savedInstanceState);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        
        mGLSurfaceView = new GLSurfaceView(this, last_tap);
        mGLSurfaceView.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }});
        spriterenderer = new UbuntuLogoRenderer(this, last_tap);
        
        mGLSurfaceView.setRenderer(spriterenderer);
        

        mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);	// FIXME
        
        setContentView(mGLSurfaceView);
    }


    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	
    	switch (keyCode) {
    	
    	case KeyEvent.KEYCODE_CAMERA:
    	case KeyEvent.KEYCODE_DPAD_CENTER:
    		spriterenderer.spin_direction = !spriterenderer.spin_direction;
    		break;

    	case KeyEvent.KEYCODE_DPAD_UP:
    		spriterenderer.spin_increment_multiplier++;
    		break;
    		
    	case KeyEvent.KEYCODE_DPAD_DOWN:
    		spriterenderer.spin_increment_multiplier--;
    		break;
    		
    	default:
    		return super.onKeyDown(keyCode, event);
    	}
    	
    	return true;
  	
    }
    
    
    
	/*
    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { 
    public boolean onCreateOptionsMenu(Menu menu) {
        // Hold on to this
//        mMenu = menu;

    	
    	// THIS SEEMS TO ONLY WORK ONCE.
    	
    	
    	spriterenderer.spin_direction = !spriterenderer.spin_direction;
    	return true;
    }
*/
	
    
    
    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(mListener);
        super.onStop();
//        android.util.Log.d("skia", "stop " + mSensorManager);
    }

    
    
    // FIXME!!!!!!
    private final SensorListener mListener = new SensorListener() {

        private final float[] mScale = new float[] { 2, 2.5f, 0.5f };   // accel

        private float[] mPrev = new float[3];

        public void onSensorChanged(int sensor, float[] values) {
            boolean show = false;
            float[] diff = new float[3];

            for (int i = 0; i < 3; i++) {
                diff[i] = Math.round(mScale[i] * (values[i] - mPrev[i]) * 0.45f);
                if (Math.abs(diff[i]) > 0) {
                    show = true;
                }
                mPrev[i] = values[i];
            }

            if (show) {
                // only shows if we think the delta is big enough, in an attempt
                // to detect "serious" moves left/right or up/down
                android.util.Log.e("test", "sensorChanged " + sensor + " (" + values[0] + ", " + values[1] + ", " + values[2] + ")"
                                   + " diff(" + diff[0] + " " + diff[1] + " " + diff[2] + ")");
            }

            long now = android.os.SystemClock.uptimeMillis();
            if (now - mLastGestureTime > 1000) {
                mLastGestureTime = 0;

                float x = diff[0];
                float y = diff[1];
                boolean gestX = Math.abs(x) > 3;
                boolean gestY = Math.abs(y) > 3;

                if ((gestX || gestY) && !(gestX && gestY)) {
                    if (gestX) {
                        if (x < 0) {
                            android.util.Log.e("test", "<<<<<<<< LEFT <<<<<<<<<<<<");
                        } else {
                            android.util.Log.e("test", ">>>>>>>>> RITE >>>>>>>>>>>");
                        }
                    } else {
                        if (y < -2) {
                            android.util.Log.e("test", "<<<<<<<< UP <<<<<<<<<<<<");
                        } else {
                            android.util.Log.e("test", ">>>>>>>>> DOWN >>>>>>>>>>>");
                        }
                    }
                    mLastGestureTime = now;
                }
            }
        }

        private long mLastGestureTime;

        public void onAccuracyChanged(int sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    
    
    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
        
        
        int mask = 0;
//      mask |= SensorManager.SENSOR_ORIENTATION;
      mask |= SensorManager.SENSOR_ACCELEROMETER;

      mSensorManager.registerListener(mListener, mask, SensorManager.SENSOR_DELAY_FASTEST);
//      android.util.Log.d("skia", "resume " + mSensorManager);
        
    }

    private GLSurfaceView mGLSurfaceView;
}
