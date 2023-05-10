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
        for (i = 0; i < bullets.size(); ++i) {
            remove = ((Bullet) bullets.get(i)).update();
            if (remove) {
                bullets.remove(i);
                --i;
            }
        }
        for (i = 0; i < enemies.size(); i++) {
            ((Enemy) enemies.get(i)).update();
        }
        double px = 0;
        double ex = 0;
        double ey = 0;
        int r;
        double dx;
        double dy;
        double dist;
        if (!this.laserTaken) {
            for (i = 0; i < bullets.size(); ++i) {
                Bullet b = (Bullet) bullets.get(i);
                px = (double) b.getx();
                ex = (double) b.gety();
                ey = (double) b.getr();
            }
            for (r = 0; r < enemies.size(); ++r) {
                Enemy enemy = (Enemy) enemies.get(r);
                dx = enemy.getx();
                dy = enemy.gety();
                dist = (double) enemy.getr();
                dx = px - dx;
                dy = ex - dy;
                dist = Math.sqrt(dx * dx + dy * dy);
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
                    if (!removed) {
                        enemy.hit();
                    }
                    bullets.remove(i);
                    player.addScore(enemy.getType() + enemy.getRank());
                    --i;
                    break;
                }
            }
        }
        for (i = 0; i < enemies.size(); ++i) {
            if (((Enemy) enemies.get(i)).isDead()) {
                explosions.add(new Explosion((Enemy) enemies.get(i), ((Enemy) enemies.get(i)).getr() * 5));
                double rand = Math.random();
                if (rand < 0.001) {
                    powerups.add(new PowerUp((Enemy) enemies.get(i), 1));
                } else if (rand < 0.005) {
                    powerups.add(new PowerUp((Enemy) enemies.get(i), 3));
                } else if (rand < 0.03) {
                    powerups.add(new PowerUp((Enemy) enemies.get(i), 2));
                } else if (rand < 0.07) {
                    powerups.add(new PowerUp((Enemy) enemies.get(i), 5));
                } else if (rand < 0.1) {
                    powerups.add(new PowerUp((Enemy) enemies.get(i), 4));
                }
                enemies.remove(i);
                --i;
            }
        }
        int y;
        //int i;
        //double dist;
        if (!player.isRecovering() && !player.isOver()) {
            i = player.getx();
            y = player.gety();
            i = player.getr();
            for (i = 0; i < enemies.size(); ++i) {
                Enemy e = (Enemy) enemies.get(i);
                ex = e.getx();
                ey = e.gety();
                dist = (double) e.getr();
                dx = (double) i - ex;
                dy = (double) y - ey;
                dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < (double) i + dist) {
                    player.loseLife();
                    player.addScore(-(e.getRank() + e.getType()));
                    explosions.add(new Explosion((Enemy) enemies.get(i), ((Enemy) enemies.get(i)).getr() * 5));
                    enemies.remove(i);
                    --i;
                    player.addPower(1);
                }
            }
        }
        for (i = 0; i < explosions.size(); ++i) {
            remove = ((Explosion) explosions.get(i)).update();
            if (remove) {
                explosions.remove(i);
                --i;
            }
        }
        for (i = 0; i < powerups.size(); ++i) {
            remove = ((PowerUp) powerups.get(i)).update();
            if (remove) {
                powerups.remove(i);
            }
        }
        for (i = 0; i < powerups.size(); i++) {
            PowerUp p = (PowerUp) powerups.get(i);
            px = p.getx();
            ex = p.gety();
            int pr = p.getr();
            int ppx = player.getx();
            r = player.gety();
            int ppr = player.getr();
            dx = px - (double) ppx;
            dy = ex - (double) r;
            dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < (double) (pr + ppr)) {
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

                        while (true) {
                            if (j >= enemies.size()) {
                                break label184;
                            }

                            ((Enemy) enemies.get(j)).setSlow(true);
                            ++j;
                        }
                    default:
                        System.exit(0);
                }

                powerups.remove(i);
            }
        }
        if (this.laser != null && this.laserTaken && !player.isOver()) {
            i = player.getx() + player.getr();
            y = player.gety();
            for (i = 0; i < enemies.size(); i++) {
                Enemy e = (Enemy) enemies.get(i);
                ex = e.getx();
                ey = e.gety();
                r = e.getr();
                dist = ex < (double) i ? (double) i - ex : ex - (double) i;
                if (ey < (double) y && dist < (double) r) {
                    explosions.add(new Explosion(e, r * 5));
                    player.addScore(e.getType() + e.getRank());
                    enemies.remove(i);
                }
            }
        }
        if (this.laser != null) {
            remove = laser.update();
            if (player.isRecovering()) remove = player.isRecovering();
            if (remove) {
                laserTaken = false;
                laser = null;
            }
        }
        if (slowStartTimer != 0) {
            slowElapsed = (System.nanoTime() - slowStartTimer) / 1000000;
            if (slowElapsed > slowLength) {
                slowStartTimer = 0;
                for (i = 0; i < enemies.size(); i++)
                    enemies.get(i).setSlow(false);
            }
        }


    }

    public void gameRender() {

        //Draw Background
        if (!player.isOver()) {

            g.setColor(bgColor);
            g.fillRect(0, 0, WIDTH, HEIGHT);

        } else {
            g.setColor(new Color(200, 120, 100));
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        if (slowStartTimer != 0) {
            g.setColor(new Color(255, 255, 255, 64));
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }


        //Score
        g.setFont(new Font("Century Gothic", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + player.getScore(), WIDTH - 80, 20);

        //Credits
        g.setFont(new Font("Century Gothic", Font.BOLD, 15));
        g.drawString("", WIDTH - 170, HEIGHT - 10);

        //Wave Number
        if (waveStartTimer != 0) {

            g.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            String s = String.format("- W A V E  %d -", waveNumber);
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
            if (alpha > 255) alpha = 255;
            g.setColor(new Color(255, 255, 255, alpha));
            g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);

        }


        //Lives
        for (int i = 0; i < player.getLives(); i++) {

            g.setColor(bgColor.brighter().brighter());
            g.fillOval(15 + (25 * i), 15, player.getr() * 2, player.getr() * 2);
            g.setStroke(new java.awt.BasicStroke(2));
            g.setColor(Color.BLACK);
            g.drawOval(15 + (25 * i), 15, player.getr() * 2, player.getr() * 2);
            g.setStroke(new java.awt.BasicStroke(1));

        }

        //Power
        g.setColor(Color.YELLOW);
        g.fillRect(20, 60, player.getPower() * 8, 8);
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.YELLOW.darker());
        for (int i = 0; i < player.getRequiredPower(); i++) {
            g.drawRect(20 + 8 * i, 60, 8, 8);
        }
        g.setStroke(new BasicStroke(1));

        //Draw Laser
        if (laserTaken)
            laser.draw(g);

        //Draw Player
        player.draw(g);

        //draw enemies
        for (int i = 0; i < enemies.size(); i++)
            enemies.get(i).draw(g);

        //Bullet
        if (!laserTaken)
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).draw(g);
            }

        //explosion
        for (int i = 0; i < explosions.size(); i++)
            explosions.get(i).draw(g);

        //PowerUp
        for (int i = 0; i < powerups.size(); i++) powerups.get(i).draw(g);

        // draw slow down meter
        if (slowStartTimer != 0 && !laserTaken) {
            g.setColor(Color.WHITE);
            g.drawRect(10, 80, 100, 10);
            g.fillRect(10, 80,
                    (int) (100 - 100.0 * slowElapsed / slowLength), 10);
        } else if (slowStartTimer != 0) {
            g.setColor(Color.WHITE);
            g.drawRect(10, 100, 100, 10);
            g.fillRect(10, 100,
                    (int) (100 - 100.0 * slowElapsed / slowLength), 10);
        }

        //Laser timer
        if (laserTaken) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1));
            g.drawRect(10, 80,
                    100, 8);
            g.fillRect(10, 80,
                    (int) (100 - 100.0 * Laser.elapsed / Laser.laserTimer), 8);
            g.setStroke(new BasicStroke(1));
        }

        //over
        if (player.isOver()) {

            //Over Display
            g.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            String s = "G A M E    O V E R";
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g.setColor(Color.WHITE);
            g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
            //setters off
            player.setLeft(false);
            player.setRight(false);
            player.setUp(false);
            player.setDown(false);
            player.setFiring(false);
            //Listener off
            removeKeyListener(this);
        }
    }

    public void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    private void createNewEnemies() {
        int n = waveNumber * 3;
        int type = 0;
        int rank = 0;
        for (int i = 0; i < n; i++) {
            type = (waveNumber < 3) ? 1 : (int) (Math.random() * (3)) + 1;
            rank = (type == 1) ? (int) (Math.random() * (2)) + 1 : (int) (Math.random() * (2)) + 1;
            enemies.add(new Enemy(type, rank));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) player.setLeft(true);
        if (keyCode == KeyEvent.VK_RIGHT) player.setRight(true);
        if (keyCode == KeyEvent.VK_UP) player.setUp(true);
        if (keyCode == KeyEvent.VK_DOWN) player.setDown(true);
        if (keyCode == KeyEvent.VK_SPACE) player.setFiring(true);
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) player.setLeft(false);
        if (keyCode == KeyEvent.VK_RIGHT) player.setRight(false);
        if (keyCode == KeyEvent.VK_UP) player.setUp(false);
        if (keyCode == KeyEvent.VK_DOWN) player.setDown(false);
        if (keyCode == KeyEvent.VK_SPACE) player.setFiring(false);

    }
}