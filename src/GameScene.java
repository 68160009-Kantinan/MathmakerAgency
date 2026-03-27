import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

// Abstract Class: base class ของทุก scene (OOP: Abstract Class, Inheritance, Interface)
// extends JPanel เพื่อใช้ Swing, implements IController + IRenderable
public abstract class GameScene extends JPanel implements IController, IRenderable {

    // Association (has-a): ทุก scene มี renderer และ state manager
    protected FallbackRenderer renderer;
    protected GameStateManager gsm;

    public GameScene() {
        setPreferredSize(new Dimension(1280, 720));
        setFocusable(false);  // JFrame จัดการ input แทน
        renderer = new FallbackRenderer();
        gsm = GameStateManager.getInstance();
    }

    /**
     * เมื่อ scene ถูก add เข้า JFrame ให้ส่ง focus กลับ JFrame ทันที
     * ป้องกัน JPanel ดึง focus ไปจาก JFrame ทำให้ KeyListener ไม่ทำงาน
     */
    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (w != null) w.requestFocusInWindow();
        });
    }

    // Overriding: เชื่อม Swing กับ render() ของ subclass (OOP: Overriding)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g);
    }

    // Abstract methods — subclass ต้อง implement (OOP: Abstract Class)
    @Override public abstract void render(Graphics2D g2d);
    @Override public abstract void onKeyPressed(KeyEvent e);

    // Default implementation — subclass override ได้ถ้าต้องการ
    @Override public void onKeyReleased(KeyEvent e) {}
}
