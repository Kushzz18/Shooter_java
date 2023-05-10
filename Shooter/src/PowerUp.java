import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp {
    private double x;
    private double y;
    private int r;
    private int type;
    private int speed;
    private Color powerUpColor;
    private Enemy e;

    public PowerUp(Enemy e, int type) {
        this.x = e.getx();
        this.y = e.gety();
        this.type = type;
        this.e = e;
        this.speed = 2;
        switch (type) {
            case 1:
                this.powerUpColor = Color.WHITE;
                this.r = 5;
                break;
            case 2:
                this.powerUpColor = Color.PINK;
                this.r = 5;
                break;
            case 3:
                this.powerUpColor = Color.GREEN;
                this.r = 3;
                break;
            case 4:
                this.powerUpColor = Color.YELLOW;
                this.r = 3;
                this.speed = 4;
                break;
            case 5:
                this.powerUpColor = Color.white;
                this.r = 6;
                this.speed = 1;
                break;
            default:
                System.exit(0);
        }

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

    public int getType() {
        return this.type;
    }

    public boolean update() {
        if (this.e.isSlow()) {
            this.y += (double)this.speed * 0.3;
        } else {
            this.y += (double)this.speed;
        }

        return this.y > (double)(GamePanel.HEIGHT - this.r);
    }

    public void draw(Graphics2D g) {
        g.setColor(this.powerUpColor);
        g.fillRect((int)this.x + this.e.getr(), (int)this.y + this.e.getr(), 2 * this.r, 2 * this.r);
    }
}
