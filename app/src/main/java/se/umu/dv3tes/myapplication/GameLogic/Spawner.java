package se.umu.dv3tes.myapplication.GameLogic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

import se.umu.dv3tes.myapplication.GameObjects.Enemies.BasicEnemy;
import se.umu.dv3tes.myapplication.GameObjects.Enemies.FlyingEnemy;
import se.umu.dv3tes.myapplication.GameObjects.GameObject;
import se.umu.dv3tes.myapplication.GameObjects.Player.Player;
import se.umu.dv3tes.myapplication.GameObjects.Projectiles.BasicProjectile;
import se.umu.dv3tes.myapplication.Powerups.DefensiveShield;
import se.umu.dv3tes.myapplication.Powerups.EvilDecoy;
import se.umu.dv3tes.myapplication.R;
import se.umu.dv3tes.myapplication.GameObjects.Player.Player;


/**
 * Created by Tobz0r on 2016-03-15.
 */
public class Spawner {

    private Handler handler;
    private int scoreKeep = 100; //set to 100 to make level start at 1
    private int level =0;
    private Resources res;
    private Player player;
    private int width,height;
    private Bitmap flyingPicture;
    private Bitmap walkingPicture;
    private Bitmap shieldPicture;
    private Bitmap deathPicture;
    private Random random;

    public Spawner(Handler handler, Resources res, Player player,int w, int h) {
        this.handler = handler;
        this.res=res;
        width=w;
        height=h;
        this.player=player;
        setUpPictures(res);
        this.random=new Random();

    }
    private void setUpPictures(Resources res){
        flyingPicture=BitmapFactory.decodeResource(res, R.drawable.flying1);
        walkingPicture=BitmapFactory.decodeResource(res,R.drawable.walking1);
        shieldPicture=BitmapFactory.decodeResource(res,R.drawable.shield);
        deathPicture=BitmapFactory.decodeResource(res,R.drawable.death);
        int tempW=deathPicture.getWidth();
        int tempH=deathPicture.getHeight();
        deathPicture.recycle();
        deathPicture= GameObject.getResizedBitmap(BitmapFactory.decodeResource(res,R.drawable.death),tempW/3,tempH/3);
    }

    public void tick() {

        scoreKeep++;
        int powerUpValue=(random.nextInt(1000)+1);
        if(powerUpValue<=2){
            if(powerUpValue==2) {
                handler.addObject(new DefensiveShield(player, handler, shieldPicture, width));
            }else {
                handler.addObject(new EvilDecoy(player, handler, deathPicture, width));
            }
        }
        if (scoreKeep >= 100) {
            scoreKeep = 0;
            level++;
            if (level == 1) {
                handler.addObject(new BasicEnemy(player, walkingPicture, width, height, handler,5,res));
                handler.addObject(new DefensiveShield(player,handler,shieldPicture,width));

            }
            if(level%2==0){
                handler.addObject(new FlyingEnemy(player, flyingPicture, width, height, handler,5,res));
                handler.addObject(new BasicEnemy(player, walkingPicture, width, height, handler,5,res));
                handler.addObject(new BasicEnemy(player, walkingPicture, width, height, handler,5,res));


            }
            if (level % 10 == 0) {
                handler.addObject(new FlyingEnemy(player, flyingPicture, width, height, handler,5,res));
                handler.addObject(new FlyingEnemy(player, flyingPicture, width, height, handler,5,res));

            }
            if (level % 25 == 0) {
                handler.addObject(new BasicEnemy(player, walkingPicture, width, height, handler,5,res));
                handler.addObject(new FlyingEnemy(player, flyingPicture, width, height, handler,5,res));

            }
            if (level % 5 == 0) {
            }
        }
    }

    public void spawnProjectiles(int x, int y){
        handler.addObject(new BasicProjectile(player,x,y, handler,player));

    }

    public int getLevel(){
        return level;
    }
}

