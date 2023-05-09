import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int r;
    private int speed;
    private boolean setHealth;
    private int health;
    private boolean dead;
    private double angle;
    private int maxAngle;
    private int minAngle;
    private int type;
    private int rank;
    private boolean setXY;
    private Color enemyColor;
    private Color enemyBoundaryColor;
    private boolean setFlash;
    private long startFlashTimer;
    private int flashTimer;
    private boolean slow;

    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;
        this.maxAngle = 130;
        this.minAngle = 45;
        this.angle = Math.toRadians(Math.random() * (double) this.maxAngle + (double) this.minAngle);
        this.setHealth = true;
        this.setXY = true;
        this.init();
    }

    public Enemy(int type, int rank, int health, double angle, double x, double y) {
        this.type = type;
        this.rank = rank;
        this.angle = angle;
        this.health = health;
        if (rank == 1 && health == 1) {
            int health = true;
        }
        this.setHealth = false;
        this.x = x;
        this.y = y;
        this.setXY = false;
        this.init();
    }

    public void init() {
        this.dead = false;
        this.flashTimer = 80;
        if (this.type == 1) {
            if (this.rank == 1) {
                this.r = 9;
                if (this.setHealth) {
                    this.health = 1;
                }
                this.speed = 3;
                this.enemyColor = Color.MAGENTA;
                this.enemyBoundaryColor = Color.BLACK;
            }
        }
    }
}
