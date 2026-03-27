import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * EndingEscapeScene — ENDING 1: หนีไปด้วยกัน
 * เงื่อนไข: เลือก "พาเรเน่หนี" และ reneAffection >= 3
 * bg: good_end.jpg
 *
 * Inheritance: extends GameScene (OOP: Inheritance, Overriding)
 */
public class EndingEscapeScene extends GameScene {

    private BufferedImage bg;
    private DialogueSystem dialogueSystem;

    private static final DialogueLine[] LINES = {
        new DialogueLine("", "หลายเดือนหลังจากคืนนั้น", ""),
        new DialogueLine("", "ทะเลสีเทาอ่อนทอดยาวสุดสายตา\nและเด็กสาวที่เคยเห็นมันแค่ในหนังสือ\nกำลังยืนอยู่ตรงหน้ามันจริงๆ", ""),
        new DialogueLine("เรเน่", "...สวยจังเลย", "rene_happy"),
        new DialogueLine("", "ชินไม่ได้ตอบอะไร\nเขาเพียงนั่งอยู่ข้างๆ และมองคลื่นที่ซัดเข้าหาฝั่งไปพร้อมกับเธอ", "shin_neutral"),
        new DialogueLine("", "นี่คืออนาคตที่ไม่มีใครคำนวณไว้", ""),
        new DialogueLine("", "ชินก้มลงมองมือถือเครื่องเก่าๆ ในมือของเขา\nยกมันขึ้นมาและเล็งไปที่สุดขอบฟ้า\nก่อนจะพับฝาของมันลง", ""),
        new DialogueLine("ชิน", "หลับให้สบายนะ เร็น", "shin_neutral"),
        new DialogueLine("", "— จบ —", ""),
    };

    public EndingEscapeScene() {
        super();
        dialogueSystem = new DialogueSystem();
        loadAssets();
        dialogueSystem.loadDialogue(LINES, () -> gsm.changeScene(GameState.MAIN_MENU));
    }

    private void loadAssets() {
        bg = load("good_end");
    }

    private BufferedImage load(String key) {
        for (String ext : new String[]{ ".png", ".jpg" }) {
            File f = new File("assets/" + key + ext);
            if (f.exists()) {
                try { return ImageIO.read(f); } catch (IOException e) {}
            }
        }
        return null;
    }

    // Overriding (OOP: Overriding)
    @Override
    public void render(Graphics2D g2d) {
        if (bg != null) {
            g2d.drawImage(bg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(10, 20, 35), 1280, 720);
        }
        dialogueSystem.update();
        dialogueSystem.render(g2d);
        repaint();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }
}
