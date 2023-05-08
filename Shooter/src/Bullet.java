import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
public class Bullet {

    private int x;
    private int y;

    private double dx;
    private double dy;

    private int speed;

    private int r;

    public double angle;

    private Color bulletColor;
    private Color bulletBoundaryColor;

    public Bullet(int angle, int x, int y){


        this.x = x;
        this.y = y;


        speed = 10 ;


        r = 5;


        this.angle = Math.toRadians(angle);


        dx = Math.cos(this.angle)*speed;
        dy = Math.sin(this.angle)*speed;


        bulletColor = Color.RED;
        bulletBoundaryColor = bulletColor.darker().darker();

    }

    public int getx(){return x;}
    public int gety(){return y;}
    public int getr(){return r;}
    public double getAngle(){return angle;}

    public boolean update(){

        x+= dx;
        y+= dy;
        //Return Condition of out of Frame
        if(x > GamePanel.WIDTH-r || y > GamePanel.HEIGHT-r || x < -r || y < -r) return true;
        //Return Default
        return false;

    }

    public void draw(Graphics2D g){

        //Set Stroke
        g.setStroke(new BasicStroke(2));
        //Set Color
        g.setColor(bulletColor);
        //Fill
        g.fillOval(x+r, y+r, 2*r, 2*r);
        //Boundary
        g.setColor(bulletBoundaryColor);
        //Draw Boundary
        g.drawOval(x+r, y+r, 2*r, 2*r);
        //Restore Stroke
        g.setStroke(new BasicStroke(1));

    }
}
