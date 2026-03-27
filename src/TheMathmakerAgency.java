import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// Entry point ของเกม
// Polymorphism: ส่ง KeyEvent ไปยัง currentScene.onKeyPressed()
//               โดยไม่ต้องรู้ว่า scene ปัจจุบันเป็น class อะไร
public class TheMathmakerAgency extends JFrame {

    private GameStateManager gsm;

    public TheMathmakerAgency() {
        super("The Mathmaker Agency");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(1280, 720));

        gsm = GameStateManager.getInstance();
        gsm.setFrame(this);

        // Polymorphism: ส่ง input ไปยัง currentScene โดยตรงจาก JFrame
        // เพื่อให้รับ input ได้แม้ focus อยู่ที่ JFrame
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                GameScene s = gsm.getCurrentScene();
                if (s != null) s.onKeyPressed(e);
            }
        });

        gsm.changeScene(GameState.MAIN_MENU);

        // Game loop 30fps
        new Timer(1000 / 30, e -> {
            GameScene s = gsm.getCurrentScene();
            if (s != null) s.repaint();
        }).start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TheMathmakerAgency());
    }
}
