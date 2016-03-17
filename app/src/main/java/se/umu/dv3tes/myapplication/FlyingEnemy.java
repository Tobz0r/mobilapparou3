package se.umu.dv3tes.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Tobz0r on 2016-03-15.
 */
public class FlyingEnemy extends GameObject implements Enemy {
    private Player player;
    private Bitmap image;
    private static Bitmap[] movingImages;
    private int ticks;
    private Animator animator;
    private Handler handler;
    private int health;
    private float goalDistance;
    private Resources res;
    private boolean spawned=true;

    public FlyingEnemy(Player player,Bitmap image,int x, int y, Handler handler,int numFrames,Resources res){
        this.image=image;
        ticks=0;
        this.res=res;
        this.player=player;
        this.handler=handler;
        animator=new Animator();
        setX(x);
        health=START_HEALTH;
        setWidth(image.getWidth() / 9);
        setHeight(image.getHeight() / 9);
        setVelY(0);
        setVelX(-10);

      //  opts.inJustDecodeBounds=true;
        goalDistance =calculateDistance((int)player.getX(),(int)player.getY())/3;
        if(movingImages==null){
            initiateBitmaps(numFrames,res,image);
        }
        animator.setImages(movingImages);
        animator.setDelay(50);


    }

    private static void  initiateBitmaps(int numFrames, Resources res, Bitmap image){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inDither=false;                     //Disable Dithering mode
        opts.inMutable=true;
        opts.inTempStorage=new byte[32 * 1024];
        movingImages =new Bitmap[numFrames];
        movingImages[0]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying1,opts),image.getWidth()/3,image.getHeight()/3);
        movingImages[1]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying2,opts), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[2]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying3,opts), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[3]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying4,opts), image.getWidth() / 3, image.getHeight() / 3);
        movingImages[4]= getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.flying5,opts), image.getWidth() / 3, image.getHeight() / 3);
    }

    @Override
    public void tick() {
        float distance= calculateDistance((int)player.getX(),(int)player.getY());
        if(goalDistance>= distance ||(getX()<=0)){
            setAttacking(true);
            setVelX(0);
        }
        setX(getX() + getVelX());
        setY(getY() + getVelY());
        if(health<=0){
            player.addScore(100);
           /* for(Bitmap tempImage:movingImages){
                tempImage.recycle();
                tempImage=null;
            }*/
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
       // setY(canvas.getHeight() - (getHeight() * 9));
       // setY(canvas.getHeight()/2);
        if(spawned) {
            Random rn = new Random();
            setY(getHeight() + rn.nextInt((canvas.getHeight() - (getHeight() * 3)) - getHeight() + 1));
            spawned=false;
        }
        image=animator.getImage();
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(75, health*2, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(getHealthBar(health),myPaint);
        canvas.drawBitmap(image, getX(), getY(), null);

    }

    /**
     * Gets called when you damage a flying enemy,
     * a flying enemy takes 3 hits to kill
     */
    @Override
    public void attackThis() {
        health-=34;
    }
}
      /*  Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(getBounds(),myPaint);*/