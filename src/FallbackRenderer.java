import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

// Association (has-a): GameScene มี FallbackRenderer
// Overloading: drawSprite() มีสองรูปแบบ — แบบระบุขนาด และแบบไม่ระบุ
public class FallbackRenderer {

    // Encapsulation: cache เป็น private
    private Map<String, BufferedImage> cache = new HashMap<>();

    private static final Color PH_BG  = new Color(80, 80, 120);
    private static final Color PH_BD  = new Color(150, 150, 200);

    // Static method: วาดพื้นหลังสีทึบ
    public static void drawBackground(Graphics2D g2d, Color color, int w, int h) {
        g2d.setColor(color);
        g2d.fillRect(0, 0, w, h);
    }

    // Overloading: drawSprite แบบระบุขนาด (scale รูปให้พอดี)
    public void drawSprite(Graphics2D g2d, String key, int x, int y, int w, int h) {
        if (key == null || key.isEmpty()) return;
        BufferedImage img = loadImage(key);
        if (img != null) {
            g2d.drawImage(img, x, y, w, h, null);
        } else {
            drawPlaceholder(g2d, key, x, y, w, h);
        }
    }

    // Overloading: drawSprite แบบไม่ระบุขนาด (ใช้ขนาดจริงของรูป)
    public void drawSprite(Graphics2D g2d, String key, int x, int y) {
        if (key == null || key.isEmpty()) return;
        BufferedImage img = loadImage(key);
        if (img != null) {
            g2d.drawImage(img, x, y, null);
        } else {
            drawPlaceholder(g2d, key, x, y, 200, 350);
        }
    }

    // โหลดรูปจาก assets/ และเก็บ cache — รองรับทั้ง .png และ .jpg
    // คืน null ถ้าไม่พบไฟล์ใดเลย
    private BufferedImage loadImage(String key) {
        if (cache.containsKey(key)) return cache.get(key);
        BufferedImage img = null;
        // ลองโหลด .png ก่อน ถ้าไม่มีค่อยลอง .jpg
        for (String ext : new String[]{ ".png", ".jpg" }) {
            File file = new File("assets/" + key + ext);
            if (file.exists()) {
                try { img = ImageIO.read(file); break; } catch (IOException e) {}
            }
        }
        cache.put(key, img);
        return img;
    }

    // วาดกล่องสีม่วงแทนรูปที่ไม่พบ
    private void drawPlaceholder(Graphics2D g2d, String key, int x, int y, int w, int h) {
        g2d.setColor(PH_BG);  g2d.fillRect(x, y, w, h);
        g2d.setColor(PH_BD);  g2d.drawRect(x, y, w, h);
        g2d.setColor(Color.WHITE);
        g2d.drawString("[" + key + "]", x + 10, y + h / 2);
    }
}
