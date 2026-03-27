import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

/**
 * PrologueScene — narration ข้อความกลางจอ ตัวอักษรขาวบนหน้าจอดำ
 * กด Space หรือ Enter เพื่อข้ามได้ตลอดเวลา
 *
 * Inheritance: extends GameScene (OOP: Inheritance, Overriding)
 */
public class PrologueScene extends GameScene {

    private static final String[] LINES = {
        "พวกเขากล่าวว่า...\nพระเจ้าเป็นนักคณิตศาสตร์",
        "และที่นี่ 'ไซเฟอร์เนีย' คือกระดานดำผืนใหญ่\nที่พระเจ้าเขียนสมการที่สมบูรณ์แบบที่สุดลงไป",
        "สมการที่ชื่อว่า \"ความรัก\"",
        "ในเมืองนี้ ไม่มีใครต้องเผชิญกับความโดดเดี่ยวที่ไร้จุดหมาย\nไม่มีใครต้องหลงทางในเขาวงกตของความผิดหวัง\nเพราะ 'เดอะ โปรโตคอล' ได้คำนวณเส้นที่ขนานกันให้มาบรรจบกันอย่างแม่นยำ",
        "แต่นั่นแหละคือปัญหา...\nเมื่อโชคชะตาถูกเปลี่ยนให้เป็น 'ตัวเลข'\nความหมายของชีวิตก็กลายเป็นเพียง 'ผลลัพธ์'",
        "มนุษย์น่ะ...\nมักจะหลงลืมไปว่า ความงดงามของบทเพลงไม่ได้อยู่ที่โน้ตตัวสุดท้าย\nแต่อยู่ที่จังหวะที่มันเพี้ยนไปจากเดิม",
        "ผมชื่อ 'ชิน'\nในโลกที่ทุกอย่างมีค่าเท่ากับหนึ่งบวกหนึ่ง...\nผมคือ 'Zero-Index'\nสิ่งที่ไม่แม้แต่จะถูกบันทึกลงในความทรงจำของใครสักคน",
        "เศษส่วนที่หารไม่ลงตัว...\nจุดด่างพร้อยบนกระดานดำที่สะอาดสะอ้าน",
        "และหน้าที่ของผม ไม่ใช่การพิสูจน์สมการ\nแต่คือการส่ง 'ตัวแปรที่ล้ำค่าที่สุด' กลับคืนสู่เครื่องจักร…",
        "เพื่อให้มันมอดไหม้และกลายเป็นเชื้อเพลิงของเมืองแห่งนี้ต่อไป",
        "แม้ว่าพวกเขาจะไม่ยินยอมก็ตาม",
    };

    private enum FadeState { FADE_IN, HOLD, FADE_OUT, DONE }

    private static final int SCREEN_W       = 1280;
    private static final int SCREEN_H       = 720;
    private static final Color BG           = new Color(3, 3, 10);
    private static final int FADE_IN_FRAMES  = 24;
    private static final int HOLD_FRAMES     = 120; // 4 วินาที @ 30fps
    private static final int FADE_OUT_FRAMES = 18;

    private int currentIndex = 0;
    private FadeState state  = FadeState.FADE_IN;
    private int frameCount   = 0;
    private float alpha      = 0f;

    public PrologueScene() { super(); }

    @Override
    public void render(Graphics2D g2d) {
        updateState();
        g2d.setColor(BG);
        g2d.fillRect(0, 0, SCREEN_W, SCREEN_H);
        if (state == FadeState.DONE || currentIndex >= LINES.length) return;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.WHITE);
        g2d.setFont(getThaiFont(Font.PLAIN, 28));
        drawCenteredText(g2d, LINES[currentIndex], SCREEN_W, SCREEN_H);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void updateState() {
        if (state == FadeState.DONE) return;
        frameCount++;
        switch (state) {
            case FADE_IN:
                alpha = Math.min(1f, (float) frameCount / FADE_IN_FRAMES);
                if (frameCount >= FADE_IN_FRAMES) { alpha = 1f; state = FadeState.HOLD; frameCount = 0; }
                break;
            case HOLD:
                if (frameCount >= HOLD_FRAMES) { state = FadeState.FADE_OUT; frameCount = 0; }
                break;
            case FADE_OUT:
                alpha = Math.max(0f, 1f - (float) frameCount / FADE_OUT_FRAMES);
                if (frameCount >= FADE_OUT_FRAMES) { alpha = 0f; advanceLine(); }
                break;
            default: break;
        }
    }

    private void advanceLine() {
        currentIndex++;
        if (currentIndex >= LINES.length) {
            state = FadeState.DONE;
            gsm.changeScene(GameState.OFFICE);
        } else {
            state = FadeState.FADE_IN;
            frameCount = 0;
            alpha = 0f;
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            if (state == FadeState.DONE) return;
            if (state == FadeState.FADE_OUT) { alpha = 0f; advanceLine(); }
            else { state = FadeState.FADE_OUT; frameCount = 0; }
        }
    }

    private void drawCenteredText(Graphics2D g2d, String text, int w, int h) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] lines = text.split("\n", -1);
        int lineHeight = fm.getHeight() + 8;
        int startY = (h - lines.length * lineHeight) / 2 + fm.getAscent();
        for (int i = 0; i < lines.length; i++) {
            int x = (w - fm.stringWidth(lines[i])) / 2;
            g2d.drawString(lines[i], x, startY + i * lineHeight);
        }
    }

    private static Font getThaiFont(int style, int size) {
        String[] candidates = { "TH Sarabun New", "Tahoma", "Dialog" };
        for (String name : candidates) {
            Font f = new Font(name, style, size);
            if (f.getFamily().equalsIgnoreCase(name)) return f;
        }
        return new Font("Dialog", style, size);
    }
}
