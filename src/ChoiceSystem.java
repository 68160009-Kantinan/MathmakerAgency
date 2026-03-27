import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.IntConsumer;

// Association (has-a): GameScene มี ChoiceSystem
// Overloading: showChoices() มีสามรูปแบบ
// Encapsulation: ทุก field เป็น private
public class ChoiceSystem {

    private Choice[] choices = new Choice[0];
    private boolean visible = false;
    private Runnable onComplete;
    private IntConsumer onCompleteWithIndex;  // Overloading: callback แบบรับ index

    // UI constants
    private static final int PX = 200, PY = 420, PW = 880;
    private static final int CH = 64, GAP = 12, PAD = 20;

    private static final Color BG_N  = new Color(10, 10, 30, 210);
    private static final Color BG_H  = new Color(40, 40, 100, 230);
    private static final Color BD_N  = new Color(140, 140, 200, 180);
    private static final Color BD_H  = new Color(200, 200, 255, 255);
    private static final Color NUM_C = new Color(160, 180, 255);

    // Overloading: รับ array + Runnable
    public void showChoices(Choice[] choices, Runnable onComplete) {
        this.choices             = (choices != null) ? choices : new Choice[0];
        this.visible             = this.choices.length > 0;
        this.onComplete          = onComplete;
        this.onCompleteWithIndex = null;
    }

    // Overloading: รับ array + IntConsumer (ส่ง index ที่เลือกกลับ)
    public void showChoices(Choice[] choices, IntConsumer onCompleteWithIndex) {
        this.choices             = (choices != null) ? choices : new Choice[0];
        this.visible             = this.choices.length > 0;
        this.onComplete          = null;
        this.onCompleteWithIndex = onCompleteWithIndex;
    }

    // Overloading: รับสองตัวเลือก
    public void showChoices(Choice a, Choice b, Runnable onComplete) {
        showChoices(new Choice[]{ a, b }, onComplete);
    }

    // กดปุ่มตัวเลข 1, 2, ... เพื่อเลือก
    public void onKeyPressed(int number) {
        if (!visible) return;
        int i = number - 1;
        if (i < 0 || i >= choices.length) return;
        Choice c = choices[i];
        GameStateManager.getInstance().updateMeters(
            c.getTrustDelta(), c.getGuiltDelta(), c.getRenStabilityDelta()
        );
        visible = false;
        if (onCompleteWithIndex != null) onCompleteWithIndex.accept(i);
        else if (onComplete != null)     onComplete.run();
    }

    public void render(Graphics2D g2d) {
        if (!visible || choices.length == 0) return;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (int i = 0; i < choices.length; i++) {
            int y = PY + i * (CH + GAP);
            g2d.setColor(BG_N);  g2d.fillRoundRect(PX, y, PW, CH, 10, 10);
            g2d.setColor(BD_N);  g2d.drawRoundRect(PX, y, PW, CH, 10, 10);
            g2d.setColor(NUM_C);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2d.drawString((i + 1) + ".", PX + PAD, y + CH / 2 + 7);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.PLAIN, 20));
            g2d.drawString(choices[i].getText(), PX + PAD + 36, y + CH / 2 + 7);
        }
        g2d.setColor(new Color(120, 120, 120));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString("กดปุ่ม 1-" + choices.length + " เพื่อเลือก",
                PX, PY + choices.length * (CH + GAP) + 18);
    }

    public boolean isVisible() { return visible; }
    public void hide()         { visible = false; }
}
