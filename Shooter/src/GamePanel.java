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
    }
}
