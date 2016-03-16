package se.umu.dv3tes.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;



/**
 * Created by Tobz0r on 2016-03-15.
 */
public class BasicProjectile extends GameObject implements Projectile {

    private int targetX,targetY;
    private Bitmap image;
    private Player player;
    private GameObject shooter;
    private Position position;
    private Handler handler;
    private boolean hostileProjectile=false;

    public BasicProjectile(Player player,Bitmap image,int targetX, int targetY, Handler handler, GameObject shooter){
        this.image=image;
        this.targetY=targetY;
        this.targetX=targetX;
        this.player=player;
        this.shooter=shooter;
        setHeight(image.getHeight() / 3);
        setWidth(image.getWidth() / 3);
        setX(shooter.getX());
        setY(shooter.getY());
        this.handler=handler;
        if(targetX==player.getX() && targetY==player.getY()){
            hostileProjectile=true;
        }
    }

    @Override
    public void tick() {

        float deltaX = getX() - targetX;
        float deltaY = getY() - targetY;
        float distance = (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        setVelX((float) ((-10.0 / distance) * deltaX));
        setVelY((float) ((-10.0 / distance) * deltaY));
        setX(getX() + getVelX());
        setY(getY() + getVelY());
        if ((getX() > targetX - getWidth()) && (targetX + getWidth() > getX())) {
            if ((getY() > targetY - getHeight()) && (targetY + getHeight() > getY())) {
                handler.removeObject(this);
            }
        }
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, getX(),getY(), null);
    }


    @Override
    public boolean isHostile() {
        return hostileProjectile;
    }
}
