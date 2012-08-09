package com.googlecode.androbuntu.Turntable3D;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

/**
 * A utility that projects
 *
 */
class Projector {
    public Projector() {
        mMVP = new float[16];
        mV = new float[4];
        mGrabber = new MatrixGrabber();
    }

    public void setCurrentView(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mViewWidth = width;
        mViewHeight = height;
    }

    public void project(float[] obj, int objOffset, float[] win, int winOffset) {
        if (!mMVPComputed) {
            Matrix.multiplyMM(mMVP, 0, mGrabber.mProjection, 0, mGrabber.mModelView, 0);
            mMVPComputed = true;
        }

        Matrix.multiplyMV(mV, 0, mMVP, 0, obj, objOffset);

        float rw = 1.0f / mV[3];

        win[winOffset] = mX + mViewWidth * (mV[0] * rw + 1.0f) * 0.5f;
        win[winOffset + 1] = mY + mViewHeight * (mV[1] * rw + 1.0f) * 0.5f;
        win[winOffset + 2] = (mV[2] * rw + 1.0f) * 0.5f;
    }

    // Karl
    public void getCurrentViewport(int[] viewport) {
    	viewport[0] = mX;
    	viewport[1] = mY;
    	viewport[2] = mViewWidth;
    	viewport[3] = mViewHeight;
    }

    // Karl
    public void retrieveCurrentProjection(GL10 gl, float[] mat) {
    	getCurrentProjection(gl);
        for (int i=0; i<16; i++)
        	mat[i] = mGrabber.mProjection[i];
    }
    
    // Karl
    public void retrieveCurrentModelView(GL10 gl, float[] mat) {
    	getCurrentModelView(gl);
        for (int i=0; i<16; i++)
        	mat[i] = mGrabber.mModelView[i];
    }

    
    
    /**
     * Get the current projection matrix. Has the side-effect of
     * setting current matrix mode to GL_PROJECTION
     * @param gl
     */
    public void getCurrentProjection(GL10 gl) {
        mGrabber.getCurrentProjection(gl);
        mMVPComputed = false;
    }

    /**
     * Get the current model view matrix. Has the side-effect of
     * setting current matrix mode to GL_MODELVIEW
     * @param gl
     */
    public void getCurrentModelView(GL10 gl) {
        mGrabber.getCurrentModelView(gl);
        mMVPComputed = false;
    }

    private MatrixGrabber mGrabber;
    private boolean mMVPComputed;
    private float[] mMVP;
    private float[] mV;
    private int mX;
    private int mY;
    private int mViewWidth;
    private int mViewHeight;
}
