import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

// Inheritance: extends GameScene (OOP: Inheritance, Overriding, Polymorphism)
public class MainMenu extends GameScene {

    private static final Color BG    = new Color(5, 5, 20);
    private static final Color TITLE = new Color(200, 220, 255);
    private static final Color ACCENT = new Color(100, 130, 200);
    private static final Color DIM   = new Color(60, 60, 80);

    public MainMenu() { super(); }

    // Overriding: วาด Main Menu (OOP: Overriding)
    @Override
    public void render(Graphics2D g2d) {
        FallbackRenderer.drawBackground(g2d, BG, 1280, 720);
        renderer.drawSprite(g2d, "bg_menu", 0, 0, 1280, 720); // scale รูปให้พอดีหน้าจอ

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // เส้นตกแต่ง
        g2d.setColor(ACCENT);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawLine(200, 180, 1080, 180);

        // ชื่อเกม
        g2d.setFont(new Font("Serif", Font.BOLD, 64));
        g2d.setColor(TITLE);
        String title = "THE MATHMAKER AGENCY";
        g2d.drawString(title, (1280 - g2d.getFontMetrics().stringWidth(title)) / 2, 280);

        // Subtitle
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2d.setColor(DIM);
        String sub = "— Visual Novel —";
        g2d.drawString(sub, (1280 - g2d.getFontMetrics().stringWidth(sub)) / 2, 320);

        g2d.setColor(ACCENT);
        g2d.drawLine(200, 350, 1080, 350);

        // ปุ่ม Start
        g2d.setFont(new Font("Dialog", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        String start = "[ ~START~ ]";
        g2d.drawString(start, (1280 - g2d.getFontMetrics().stringWidth(start)) / 2, 450);

        // Exit button
        g2d.setFont(new Font("Dialog", Font.PLAIN, 22));
        g2d.setColor(new Color(180, 180, 180));
        String exit = "[ Esc -- EXIT ]";
        g2d.drawString(exit, (1280 - g2d.getFontMetrics().stringWidth(exit)) / 2, 520);

        // Version
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2d.setColor(new Color(50, 50, 70));
        g2d.drawString("v1.0  |  OOP Java Project", 20, 700);
    }

    // Overriding: รับ keyboard input (OOP: Overriding)
    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
            gsm.changeScene(GameState.PROLOGUE);
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
