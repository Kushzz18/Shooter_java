import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable,KeyListener {
    private static final long serialVersionUID = 10L;
    private Color bgColor = new Color(50, 100, 100);
    public static int WIDTH;
    public static int HEIGHT;
    private int FPS;
    private boolean running;
    public Graphics2D g;
    public BufferedImage image;
    private Thread thread;
    public static Player player;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Explosion> explosions;
    public static ArrayList<PowerUp> powerups;
    private Laser laser;
    private boolean laserTaken;
    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveDelay;
    private int waveNumber;
    private boolean waveStart;
    private long slowStartTimer;
    private int slowLength;
    private long slowElapsed;


    public GamePanel() {
        WIDTH = 600;
        HEIGHT = 600;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.FPS = 60;
        this.setFocusable(true);
        this.requestFocus();
        this.waveStartTimer = 0L;
        this.waveStartTimerDiff = 0L;
        this.waveDelay = 2500;
        this.slowLength = 10000;

    }

    public void addNotify() {
        super.addNotify();
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
        this.addKeyListener(this);
    }

    public void run() {
        this.running = true;
        this.image = new BufferedImage(WIDTH, HEIGHT, 1);
        this.g = (Graphics2D) this.image.getGraphics();
        //player = new Player();
        enemies = new ArrayList();
        bullets = new ArrayList();
        powerups = new ArrayList();
        explosions = new ArrayList();
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        long targetTime = (long) (1000 / this.FPS);
        while (this.running) {
            long startTime = System.nanoTime();
            this.gameUpdate();
            long URDTimeMillis = (System.nanoTime() - startTime) / 1000000L;
            long waitTime = targetTime - URDTimeMillis;

            try {
                Thread.sleep(waitTime);
            } catch (Exception var10) {
            }
        }
    }

    public void gameUpdate() {
        if (this.waveStartTimer == 0L && enemies.size() == 0) {
            ++this.waveNumber;
            this.waveStart = false;
            this.waveStartTimer = System.nanoTime();
        } else {
            this.waveStartTimerDiff = (System.nanoTime() - this.waveStartTimer) / 1000000L;
            if (this.waveStartTimerDiff > (long) this.waveDelay) {
                this.waveStart = true;
                this.waveStartTimer = 0L;
                this.waveStartTimerDiff = 0L;
            }
        }
        if (this.waveStart && enemies.size() == 0) {
            this.createNewEnemies();
        }
        player.update();

        int i;
        boolean remove;
        for(i = 0; i < bullets.size(); ++i) {
            remove = ((Bullet)bullets.get(i)).update();
            if (remove) {
                bullets.remove(i);
                --i;
            }
        }
        for (i=0; i< enemies.size() ; i++){
            (Enemy)enemies.get(i)).update();
        }
        double px;
        double ex;
        double ey;
        int r;
        double dx;
        double dy;
        double dist;
        if(!this.laserTaken){
            for(i=0;i<bullets.size();i++){
                Bullet bullet=(Bullet)bullets.get(i);
                px=(double)bullet.getx();
                ex = (double)bullet.gety();
                ey = (double)bullet.getr();
            }
        for(r=0;r< enemies.size();r++){
            Enemy enemy = (Enemy).enemies.get(r);
            dx = enemy.getx();
            dy = enemy.gety();
            dist = (double)enemy.getr();
            double dx = px - dx;
            double dy = ex - dy;
            double dist = Math.sqrt(dx * dx + dy * dy);
            boolean removed = false;
            if (dist < ey + dist) {
                if (enemy.getHealth() == 2) {
                    explosions.add(new Explosion(enemy, enemy.getr() * 5));
                    enemies.add(new Enemy(enemy.getRank() - 1 > 0 ? enemy.getRank() - 1 : 1, enemy.getType() - 1 > 0 ? enemy.getType() - 1 : 1, enemy.getHealth() - 1, enemy.getAngle() + Math.toRadians(70.0), enemy.getx() - (double) enemy.getr(), enemy.gety()));
                    enemies.add(new Enemy(enemy.getRank() - 1 > 0 ? enemy.getRank() - 1 : 1, enemy.getType() - 1 > 0 ? enemy.getType() - 1 : 1, enemy.getHealth() - 1, enemy.getAngle() - Math.toRadians(60.0), enemy.getx() + (double) enemy.getr(), enemy.gety()));
                    enemies.remove(r);
                    if (this.slowStartTimer != 0L) {
                        for (int k = 0; k < enemies.size(); ++k) {
                            ((Enemy) enemies.get(k)).setSlow(true);
                        }
                    }
                    removed = true;
                }
                if(!removed){
                    enemy.hit();
                }
                bullets.remove(i);
                player.addScore(enemy.getType() + enemy.getRank());
                --i;
                break;
            }
        }
    }
        for(i = 0; i < enemies.size(); ++i) {
            if (((Enemy)enemies.get(i)).isDead()) {
                explosions.add(new Explosion((Enemy)enemies.get(i), ((Enemy)enemies.get(i)).getr() * 5));
                double rand = Math.random();
                if (rand < 0.001) {
                    powerups.add(new PowerUp((Enemy)enemies.get(i), 1));
                } else if (rand < 0.005) {
                    powerups.add(new PowerUp((Enemy)enemies.get(i), 3));
                } else if (rand < 0.03) {
                    powerups.add(new PowerUp((Enemy)enemies.get(i), 2));
                } else if (rand < 0.07) {
                    powerups.add(new PowerUp((Enemy)enemies.get(i), 5));
                } else if (rand < 0.1) {
                    powerups.add(new PowerUp((Enemy)enemies.get(i), 4));
                }
                enemies.remove(i);
                --i;
            }
        }
        int y;
        int i;
        double dist;
        if (!player.isRecovering() && !player.isOver()) {
            i = player.getx();
            y = player.gety();
            i = player.getr();
            for(int i = 0; i < enemies.size(); ++i) {
                Enemy e = (Enemy)enemies.get(i);
                double ex = e.getx();
                double ey = e.gety();
                dist = (double)e.getr();
                double dx = (double)i - ex;
                double dy = (double)y - ey;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < (double)i + dist) {
                    player.loseLife();
                    player.addScore(-(e.getRank() + e.getType()));
                    explosions.add(new Explosion((Enemy)enemies.get(i), ((Enemy)enemies.get(i)).getr() * 5));
                    enemies.remove(i);
                    --i;
                    player.addPower(1);
                }
            }
        }
        for(i = 0; i < explosions.size(); ++i) {
            remove = ((Explosion)explosions.get(i)).update();
            if (remove) {
                explosions.remove(i);
                --i;
            }
        }
        for(i = 0; i < powerups.size(); ++i) {
            remove = ((PowerUp)powerups.get(i)).update();
            if (remove) {
                powerups.remove(i);
            }
        }
        for(i = 0; i < powerups.size(); i++) {
            PowerUp p = (PowerUp)powerups.get(i);
            px = p.getx();
            ex = p.gety();
            int pr = p.getr();
            int ppx = player.getx();
            r = player.gety();
            int ppr = player.getr();
            dx = px - (double)ppx;
            dy = ex - (double)r;
            dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < (double)(pr + ppr)) {
                int type = p.getType();
                label184:
                switch (type) {
                    case 1:
                        player.addLife();
                        break;
                    case 2:
                        player.addPower(1);
                        break;
                    case 3:
                        player.addPower(2);
                        break;
                    case 4:
                        this.laser = new Laser();
                        this.laserTaken = true;
                        break;
                    case 5:
                        this.slowStartTimer = System.nanoTime();
                        int j = 0;

                        while(true) {
                            if (j >= enemies.size()) {
                                break label184;
                            }

                            ((Enemy)enemies.get(j)).setSlow(true);
                            ++j;
                        }
                    default:
                        System.exit(0);
                }

                powerups.remove(i);
            }
        }
        if(this.laser != null && this.laserTaken && !player.isOver()){
            i = player.getx() + player.getr();
            y = player.gety();
            for(i=0;i< enemies.size();i++){
                Enemy e = (Enemy)enemies.get(i);
                ex = e.getx();
                ey = e.gety();
                r = e.getr();
                dist = ex < (double)i ? (double)i - ex : ex - (double)i;
                if (ey < (double)y && dist < (double)r) {
                    explosions.add(new Explosion(e, r * 5));
                    player.addScore(e.getType() + e.getRank());
                    enemies.remove(i);
                }
            }
        }
        if(this.laser !=null){
            
        }
    }


    public void gameDraw(){


        Graphics g2 = this.getGraphics();


        g2.drawImage(image, 0, 0, null);


        g2.dispose();

    }
    private void createNewEnemies(){

        int n = waveNumber *3;
        int type = 0;
        int rank = 0;

        for(int i = 0; i < n; i++){

            type=(waveNumber<3)?1:(int) (Math.random() * (3)) + 1;
            rank=(type==1)?(int) (Math.random() * (2)) + 1:(int) (Math.random() * (2)) + 1;
            enemies.add(new Enemy(type, rank));
        }

    }

    public void keyPressed(KeyEvent e){

        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT) player.setLeft(true);
        if(keyCode == KeyEvent.VK_RIGHT) player.setRight(true);
        if(keyCode == KeyEvent.VK_UP) player.setUp(true);
        if(keyCode == KeyEvent.VK_DOWN) player.setDown(true);
        if(keyCode == KeyEvent.VK_SPACE) player.setFiring(true);

    }
}
