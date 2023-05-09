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
            if (this.rank == 2) {
                this.r = 11;
                if (this.setHealth) {
                    this.health = 2;
                }
                this.speed = 2;
                this.enemyColor = Color.BLACK.brighter();
                this.enemyBoundaryColor = Color.MAGENTA;
            }
        } else if (this.type == 2) {
            if (this.rank == 1) {
                this.r = 12;
                if (this.setHealth) {
                    this.health = 3;
                }

                this.speed = 2;
                this.enemyColor = Color.PINK;
                this.enemyBoundaryColor = Color.PINK.darker().darker();
            } else if (this.rank == 2) {
                this.r = 13;
                if (this.setHealth) {
                    this.health = 4;
                }

                this.speed = 2;
                this.enemyColor = Color.ORANGE;
                this.enemyBoundaryColor = this.enemyColor.darker().darker();
            }
        } else if (this.type == 3) {
            if (this.rank == 1) {
                this.r = 14;
                if (this.setHealth) {
                    this.health = 5;
                }

                this.speed = 2;
                this.enemyColor = Color.MAGENTA.brighter();
                this.enemyBoundaryColor = Color.BLUE;
            } else if (this.rank == 2) {
                this.r = 15;
                if (this.setHealth) {
                    this.health = 6;
                }

                this.speed = 1;
                this.enemyColor = Color.DARK_GRAY;
                this.enemyBoundaryColor = Color.CYAN;
            }
        }
        if (this.setXY) {
            this.x = Math.random() * (double)GamePanel.WIDTH - (double)(4 * this.r) + (double)(2 * this.r);
            this.y = (double)(-this.r);
        }

        this.dx = Math.cos(this.angle) * (double)this.speed;
        this.dy = Math.sin(this.angle) * (double)this.speed;
    }
    public double getx() {
        return this.x;
    }

    public double gety() {
        return this.y;
    }

    public int getr() {
        return this.r;
    }

    public int getHealth() {
        return this.health;
    }

    public double getAngle() {
        return this.angle;
    }

    public Color getColor() {
        return this.enemyColor;
    }

    public int getType() {
        return this.type;
    }

    public int getRank() {
        return this.rank;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setSlow(boolean b) {
        this.slow = b;
    }

    public boolean isSlow() {
        return this.slow;
    }
    public void hit() {
        --this.health;
        if (this.health > 0) {
            this.setFlash = true;
            this.startFlashTimer = System.nanoTime();
        }

        if (this.enemyColor == Color.MAGENTA && this.enemyBoundaryColor == Color.BLACK) {
            this.dead = true;
        }

        this.dead = this.health <= 0;
    }
    public void update() {
        if (this.slow) {
            this.x += this.dx * 0.3;
            this.y += this.dy * 0.3;
            this.flashTimer = 500;
        } else {
            this.x += this.dx;
            this.y += this.dy;
            this.flashTimer = 80;
        }

        if (this.type == 1 && this.rank == 1) {
            this.health = 1;
        }

        if (this.x < (double)this.r && this.dx < 0.0) {
            this.dx = -this.dx;
        }

        if (this.y < (double)this.r && this.dy < 0.0) {
            this.dy = -this.dy;
        }

        if (this.x > (double)(GamePanel.WIDTH - this.r) && this.dx > 0.0) {
            this.dx = -this.dx;
        }

        if (this.y > (double)(GamePanel.HEIGHT - this.r) && this.dy > 0.0) {
            this.dy = -this.dy;
        }

        if (this.health > 1) {
            long elapsed = (System.nanoTime() - this.startFlashTimer) / 1000000L;
            if (elapsed > (long)this.flashTimer) {
                this.setFlash = false;
                this.startFlashTimer = 0L;
            }
        }
    }
    public void draw(Graphics2D g){
        int R = this.enemyColor.getRed();
        int G = this.enemyColor.getGreen();
        int B = this.enemyColor.getBlue();
        this.enemyColor = new Color(R, G, B, 150);
        int R1 = this.enemyBoundaryColor.getRed();
        int G1 = this.enemyBoundaryColor.getGreen();
        int B1 = this.enemyBoundaryColor.getBlue();
    }
}
