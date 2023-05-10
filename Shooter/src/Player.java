import java.awt.*;

public class Player {
    private boolean firing;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int r;
    private int lives;
    private int speed;
    private Color playerColor;
    private Color playerBoundaryColor;
    private int score;
    private long firingTimer;
    private int firingDelay;
    private long recoveryTimer;
    private boolean recovering;
    private long switchTimer = 0L;
    private int powerLevel;
    private int power;
    private int[] requiredPower = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private boolean dead;

    public Player() {
        this.x = GamePanel.WIDTH / 2;
        this.y = GamePanel.HEIGHT / 2;
        this.speed = 6;
        this.r = 10;
        this.firing = false;
        this.playerColor = Color.RED;
        this.playerBoundaryColor = Color.WHITE;
        this.lives = 5;
        this.firingTimer = System.nanoTime();
        this.firingDelay = 150;
        this.recovering = false;
        this.dead = false;
        this.score = 0;
    }
    public int getx() {
        return this.x;
    }
    public int gety(){
        return this.y;
    }
    public int getr(){
        return this.r;
    }
    public int getLives(){
        return this.lives;
    }
    public int getSpeed(){
        return this.speed;
    }
    public int getPowerLevel(){
        return this.requiredPower[this.powerLevel];
    }
    public int getScore(){
        return this.score;
    }
    public int getPower(){
        return this.power;
    }
    public int getRequiredPower() {
        return this.requiredPower[this.powerLevel];
    }
    public void setLeft(boolean set) {
        this.left = set;
    }
    public void setRight(boolean set) {
        this.right = set;
    }
    public void setUp(boolean set) {
        this.up = set;
    }
    public void setDown(boolean set) {
        this.down = set;
    }
    public void setFiring(boolean set) {
        this.firing = set;
    }
    public boolean isRecovering(){
        return this.recovering;
    }
    public boolean isOver(){
        return this.dead;
    }
    public void loseLife(){
        --this.lives;
        if (this.lives <= 0) {
            this.dead = true;
        }
        this.recovering = true;
        this.recoveryTimer = System.nanoTime();
    }
    public void addLife(){
        ++this.lives;
    }
    public void addPower(int n) {
        this.power += n;
        if (this.power >= this.requiredPower[this.powerLevel]) {
            this.power -= this.requiredPower[this.powerLevel];
            ++this.powerLevel;
        }
    }
    public void addScore(int n){
        this.score += n;
    }
    public void update(){
        if (this.left) {
            this.dx = -this.speed;
        }
        if(this.right){
            this.dx = this.speed;
        }
        if(this.up){
            this.dy = -this.speed;
        }
        if(this.down){
            this.dy = this.speed;
        }
        this.x += this.dx;
        this.y += this.dy;
        if (this.x < 2) {
            this.x = 1;
        }

        if (this.y < 2) {
            this.y = 1;
        }

        if (this.x > GamePanel.WIDTH - 2 * this.r - 3) {
            this.x = GamePanel.WIDTH - 2 * this.r - 3;
        }

        if (this.y > GamePanel.HEIGHT - 2 * this.r - 3) {
            this.y = GamePanel.HEIGHT - 2 * this.r - 3;
        }
        this.dx = 0;
        this.dy = 0;
        long elapsed;
        if (this.firing) {
            elapsed = (System.nanoTime() - this.firingTimer) / 1000000L;
            if (elapsed > (long)this.firingDelay) {
                this.firingTimer = System.nanoTime();
                if (this.powerLevel < 2) {
                    GamePanel.bullets.add(new Bullet(270, this.x, this.y));
                } else if (this.powerLevel < 4) {
                    GamePanel.bullets.add(new Bullet(270, this.x + 5, this.y));
                    GamePanel.bullets.add(new Bullet(270, this.x - 5, this.y));
                } else {
                    GamePanel.bullets.add(new Bullet(270, this.x, this.y));
                    GamePanel.bullets.add(new Bullet(280, this.x + 5, this.y));
                    GamePanel.bullets.add(new Bullet(265, this.x - 5, this.y));
                }
            }
        }
        elapsed = (System.nanoTime() - this.recoveryTimer) / 1000000L;
        if (elapsed > 2000L && this.recovering) {
            this.recovering = false;
            this.recoveryTimer = 0L;
        }
    }
    public void draw(Graphics2D g){
        if(!this.recovering && !GamePanel.player.isOver()){
            g.setStroke(new BasicStroke(2.0F));
            g.setColor(this.playerColor);
            g.setColor(this.playerBoundaryColor);
            g.fillOval(this.x,this.y,2*this.r,2*this.r);
            g.drawOval(this.x,this.y,2*this.r,2*this.r);
            g.setStroke(new BasicStroke(1.0F));
        }
        else{
            long elapsed = (System.nanoTime() - this.switchTimer) / 1000000L;
            if (elapsed > 50L || GamePanel.player.isOver()) {
                g.setStroke(new BasicStroke(3.0F));
                g.setColor(Color.WHITE);
                g.fillOval(this.x, this.y, 2 * this.r, 2 * this.r);
                g.setColor(Color.RED);
                g.drawOval(this.x, this.y, 2 * this.r, 2 * this.r);
                g.setStroke(new BasicStroke(1.0F));
                this.switchTimer = System.nanoTime();
            }
        }
    }
}
