import java.awt.event.KeyEvent;

// Interface: กำหนด contract สำหรับ keyboard input (OOP: Interface)
public interface IController {
    void onKeyPressed(KeyEvent e);
    void onKeyReleased(KeyEvent e);
}
