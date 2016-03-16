package se.umu.dv3tes.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Tobz0r on 2016-03-15.
 */
public class FlyingEnemy extends GameObject implements Enemy {
    private Player player;
    private Bitmap image;
    private Bitmap[] movingImages;
    private Bitmap[] attackingImages;
    private int ticks;
    private Animator animator;
    private Handler handler;
    private int health;
    private float goalDistance;
    private Resources res;

    public FlyingEnemy(Player player,Bitmap image,int x, int y, Handler handler,int numFrames,Resources res){
        this.image=image;
        ticks=0;
        this.res=res;
        this.player=player;
        this.handler=handler;
        animator=new Animator();
        setX(x);
        health=10;
        setWidth(image.getWidth() / 9);
        setHeight(image.getHeight() / 9);
        setVelY(0);
        setVelX(-10);
        goalDistance =calculateDistance((int)player.getX(),(int)player.getY())/3;
        movingImages =new Bitmap[numFrames];
        attackingImages = new Bitmap[numFrames];
        movingImages[0]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying1),image.getWidth()/3,image.getHeight()/3);
        movingImages[1]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying2), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[2]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying3), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[3]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying4), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[4]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying5), image.getWidth() / 3, image.getHeight() / 3);
        animator.setImages(movingImages);
        animator.setDelay(50);
    }

    @Override
    public void tick() {
        float distance= calculateDistance((int)player.getX(),(int)player.getY());
        if(goalDistance>= distance){
            setAttacking(true);
            setVelX(0);
        }
        setX(getX() + getVelX());
        setY(getY() + getVelY());
        if(health<=0){
            player.addScore(100);
            handler.removeObject(this);
        }
        animator.tick();
        if(isAttacking()) {
            ticks++;
            animator.tick();
            if(ticks>=60){
                handler.addObject(new BasicProjectile(player, BitmapFactory.decodeResource(res, R.drawable.test), (int) player.getX(), (int) player.getY(), handler,this));
                ticks=0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        setY(canvas.getHeight() - (getHeight() * 9));
        image=animator.getImage();
        canvas.drawBitmap(image, getX(), getY(), null);

    }


    @Override
    public void attackThis() {
        health-=10;
    }
}
      /*  Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(getBounds(),myPaint);*/