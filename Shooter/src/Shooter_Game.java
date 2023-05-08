import javax.swing.JFrame;

public class Shooter_Game {
    public Shooter_Game() {
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Shooting Game");
        window.setDefaultCloseOperation(3);
        window.pack();
        window.setContentPane(new GamePanel());
        window.setResizable(false);
        window.setVisible(true);
        window.pack();
    }
}
