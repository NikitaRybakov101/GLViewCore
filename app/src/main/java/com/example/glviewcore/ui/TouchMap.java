package com.example.glviewcore.ui;

import android.view.MotionEvent;

import com.example.glviewcore.glcore.math.Vec3;

public class TouchMap {

    private final MainRender map3D;

    public TouchMap(MainRender map3D) {
        this.map3D = map3D;
    }

    float tempX = 0;
    float tempY = 0;

    public boolean processingTouch(MotionEvent motionEvent) {

        if(motionEvent.getAction() == 0) {
            tempX = motionEvent.getX();
            tempY = motionEvent.getY();
        }

        if(motionEvent.getAction() == 2) {

            float deltaX = - (motionEvent.getX() - tempX) / (40f * 30f);
            float deltaY = - (motionEvent.getY() - tempY) / (40f * 30f);

            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            Vec3 vector3Pos = map3D.getCamera().getPosition();
            Vec3 vector3look = map3D.getCamera().getLookAt();

            Vec3 newVector3Pos = new Vec3(vector3Pos.x + deltaX,vector3Pos.y  ,vector3Pos.z + deltaY);
            map3D.getCamera().setPosition(newVector3Pos);

            Vec3 newVector3look = new Vec3(vector3look.x + deltaX,vector3look.y,vector3look.z + deltaY);
            map3D.getCamera().setLookAt(newVector3look);

            map3D.getCamera().updateViewMatrix();
        }

   //     rotate(motionEvent);
   //     zoom(motionEvent);

        return true;
    }


    private double tempL = 0;
    private double posX = 0;

    private void rotate(MotionEvent event)
    {
        int pointerCount = event.getPointerCount();
        int actionMask = event.getActionMasked();

        switch (actionMask)
        {
            case MotionEvent.ACTION_DOWN:
                posX = 0;
            case MotionEvent.ACTION_POINTER_UP:
                posX = 0;
            case MotionEvent.ACTION_MOVE:
                if(pointerCount == 1)
                {
                    if(posX == 0)
                    {
                        posX = event.getX();
                        break;
                    }
                    double dx = event.getX() - posX;
                    posX = event.getX();
              //      map3D.setRotateBuilding((float) (dx / 18f));
                }
                break;
        }
    }

    private void zoom(MotionEvent event)
    {
        int actionMask = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        switch (actionMask)
        {
            case MotionEvent.ACTION_DOWN:
                tempL = 0;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                tempL = 0;
            case MotionEvent.ACTION_MOVE:

                if(pointerCount >= 2)
                {
                    double dx = event.getX(1) - event.getX(0);
                    double dy = event.getY(1) - event.getY(0);
                    double l = Math.sqrt(dx * dx + dy * dy);

                    if(tempL == 0)
                    {
                        tempL = l;
                        break;
                    }
                    double dl = l - tempL;
                    tempL = l;
                 //   map3D.setScaleBuilding((float) (dl / 18f));
                }
                break;
        }
    }

}
