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
}
