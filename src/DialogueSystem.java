import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Association (has-a): GameScene มี DialogueSystem
// Overloading: loadDialogue() มีสองรูปแบบ
// Encapsulation: ทุก field เป็น private
public class DialogueSystem {

    private DialogueLine[] lines = new DialogueLine[0];
    private int index = 0;
    private int charCount = 0;
    private boolean typing = false;
    private Runnable onComplete;

    // UI layout constants
    private static final int BX = 40, BY = 530, BW = 1200, BH = 175;
    private static final int NBW = 220, NBH = 36, PAD = 20;

    // Colors
    private static final Color BOX_BG  = new Color(0, 0, 0, 200);
    private static final Color BOX_BD  = new Color(180, 180, 180, 160);
    private static final Color NAME_BG = new Color(30, 30, 60, 230);
    private static final Color TXT_C   = Color.WHITE;
    private static final Color NAME_C  = new Color(200, 220, 255);

    // Overloading: โหลดหลายบรรทัด
    public void loadDialogue(DialogueLine[] lines, Runnable onComplete) {
        this.lines      = (lines != null) ? lines : new DialogueLine[0];
        this.index      = 0;
        this.charCount  = 0;
        this.typing     = this.lines.length > 0;
        this.onComplete = onComplete;
    }

    // Overloading: โหลดบรรทัดเดียว
    public void loadDialogue(String speaker, String text) {
        loadDialogue(new DialogueLine[]{ new DialogueLine(speaker, text, "") }, null);
    }

    // เพิ่มตัวอักษรทีละตัว (typewriter effect)
    public void update() {
        if (!typing || index >= lines.length) return;
        charCount++;
        if (charCount >= lines[index].getText().length()) {
            charCount = lines[index].getText().length();
            typing = false;
        }
    }

    // กด Space: typing อยู่ → แสดงทั้งหมด, จบแล้ว → บรรทัดถัดไป
    public void onSpacePressed() {
        if (lines.length == 0) return;
        if (typing) {
            charCount = lines[index].getText().length();
            typing = false;
        } else {
            index++;
            if (index >= lines.length) {
                if (onComplete != null) onComplete.run();
            } else {
                charCount = 0;
                typing = true;
            }
        }
    }

    public void render(Graphics2D g2d) {
        if (!isActive()) return;
        DialogueLine line = lines[index];
        String speaker = line.getSpeakerName();
        String full    = line.getText() != null ? line.getText() : "";
        String visible = full.substring(0, Math.min(charCount, full.length()));

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Name box (ซ่อนถ้า narration)
        if (speaker != null && !speaker.isEmpty()) {
            g2d.setColor(NAME_BG);
            g2d.fillRoundRect(BX, BY - NBH - 4, NBW, NBH, 8, 8);
            g2d.setColor(BOX_BD);
            g2d.drawRoundRect(BX, BY - NBH - 4, NBW, NBH, 8, 8);
            g2d.setColor(NAME_C);
            g2d.setFont(thaiFont(Font.BOLD, 18));
            g2d.drawString(speaker, BX + 12, BY - NBH + 20);
        }

        // Dialogue box
        g2d.setColor(BOX_BG);
        g2d.fillRoundRect(BX, BY, BW, BH, 12, 12);
        g2d.setColor(BOX_BD);
        g2d.drawRoundRect(BX, BY, BW, BH, 12, 12);

        // Text
        g2d.setColor(TXT_C);
        g2d.setFont(thaiFont(Font.PLAIN, 22));
        drawWrapped(g2d, visible, BX + PAD, BY + 36, BW - PAD * 2);

        // Arrow prompt
        if (!typing) {
            g2d.setColor(new Color(160, 160, 160));
            g2d.setFont(thaiFont(Font.PLAIN, 14));
            g2d.drawString("▼", BX + BW - 24, BY + BH - 10);
        }
    }

    private void drawWrapped(Graphics2D g2d, String text, int x, int y, int maxW) {
        if (text == null || text.isEmpty()) return;
        FontMetrics fm = g2d.getFontMetrics();
        int lh = fm.getHeight() + 4;
        int dy = y;
        for (String para : text.split("\n", -1)) {
            StringBuilder cur = new StringBuilder();
            for (String word : para.split(" ", -1)) {
                String test = cur.length() == 0 ? word : cur + " " + word;
                if (fm.stringWidth(test) > maxW && cur.length() > 0) {
                    g2d.drawString(cur.toString(), x, dy);
                    dy += lh;
                    cur = new StringBuilder(word);
                } else {
                    cur = new StringBuilder(test);
                }
            }
            if (cur.length() > 0) { g2d.drawString(cur.toString(), x, dy); dy += lh; }
        }
    }

    // คืน Font ที่รองรับภาษาไทย
    private static Font thaiFont(int style, int size) {
        for (String name : new String[]{ "TH Sarabun New", "Tahoma", "Dialog" }) {
            Font f = new Font(name, style, size);
            if (f.getFamily().equalsIgnoreCase(name)) return f;
        }
        return new Font("Dialog", style, size);
    }

    public boolean isActive()      { return lines.length > 0 && index < lines.length; }
    public boolean isTypewriting() { return typing; }
    public int getCurrentIndex()   { return index; }
    public DialogueLine getCurrentLine() { return isActive() ? lines[index] : null; }
}