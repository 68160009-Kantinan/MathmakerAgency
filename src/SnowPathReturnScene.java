import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.IntConsumer;
import javax.imageio.ImageIO;

/**
 * SnowPathReturnScene — เดินออกจากโบสถ์พร้อมเรเน่
 * bg: bg_snowpath → bg_bridge_snow (FINAL CHOICE)
 * Inheritance: extends GameScene (OOP: Inheritance, Overriding)
 * Association (has-a): มี DialogueSystem, ChoiceSystem
 */
public class SnowPathReturnScene extends GameScene {

    private BufferedImage bgSnowpath, bgBridge;
    private BufferedImage imgShinNeutral, imgShinGuilt;
    private BufferedImage imgReneCurious, imgReneHappy, imgReneSad;
    private BufferedImage currentBg;
    private BufferedImage currentSprite;  // null = narrator → ไม่วาด sprite
    private boolean showChoice = false;

    // Association (has-a)
    private DialogueSystem dialogue = new DialogueSystem();
    private ChoiceSystem   choices  = new ChoiceSystem();

    public SnowPathReturnScene() {
        super();
        loadAssets();
        currentBg = bgSnowpath;
        loadWalk();
    }

    private void loadAssets() {
        bgSnowpath     = load("bg_snowpath");
        bgBridge       = load("bg_bridge_snow");
        imgShinNeutral = load("shin_neutral");
        imgShinGuilt   = load("shin_guilt");
        imgReneCurious = load("rene_curious");
        imgReneHappy   = load("rene_happy");
        imgReneSad     = load("rene_sad");
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

    // ─── dialogue segments ────────────────────────────────────────────────────────────────────────────

    private void loadWalk() {
        currentBg = bgSnowpath;
        dialogue.loadDialogue(new DialogueLine[]{
            new DialogueLine("", "หิมะยังคงตกโปรยปรายไม่หยุด\nขณะชินกับเรเน่เดินออกจากโบสถ์ที่พังทลาย\nเสียงฝีเท้าสองคู่เหยียบหิมะดังกรอบแกรบไปตลอดทาง", ""),
            new DialogueLine("เรเน่", "...พี่", "rene_sad"),
            new DialogueLine("ชิน", "อะไรเหรอ", "shin_neutral"),
            new DialogueLine("เรเน่", "พี่... ชื่ออะไรเหรอคะ", "rene_curious"),
        }, () -> showChoice4());
    }

    private void showChoice4() {
        showChoice = true;
        choices.showChoices(
            new Choice[]{ new Choice("บอกชื่อ", 1, 0, 0), new Choice("ปฏิเสธ", 0, 0, 0) },
            (IntConsumer)(idx -> {
                showChoice = false;
                if (idx == 0) loadIntroduceShin();
                else          loadAfterIntroduce();
            })
        );
    }

    private void loadIntroduceShin() {
        dialogue.loadDialogue(new DialogueLine[]{
            new DialogueLine("ชิน", "ผมชื่อชิน", "shin_neutral"),
            new DialogueLine("เรเน่", "...พี่ชิน", "rene_happy"),
            new DialogueLine("", "เรเน่ลองเรียกชื่อเขาเบาๆ\nราวกับกำลังทดสอบว่ามันจะหายไปเหมือนอย่างอื่นในชีวิตเธอหรือไม่", ""),
        }, () -> loadAfterIntroduce());
    }

    private void loadAfterIntroduce() {
        dialogue.loadDialogue(new DialogueLine[]{
            new DialogueLine("เรเน่", "หนูชื่อเรเน่นะคะ\nเมื่อกี้... ยังไม่ได้แนะนำตัวดีๆ เลย", "rene_sad"),
            new DialogueLine("", "หลังจากความเงียบอยู่พักใหญ่ เด็กสาวก็เริ่มพูดขึ้นมาอีกครั้ง\nราวกับไม่อยากให้บรรยากาศกลับไปเย็นเฉียบเหมือนเดิม", ""),
            new DialogueLine("เรเน่", "พี่ชินเคย... อยู่ที่โบสถ์แบบนี้ไหมคะ", "rene_curious"),
            new DialogueLine("เรเน่", "หนูอยู่ที่นั่นมาตั้งแต่จำความได้\nคุณครูที่โบสถ์สอนให้หนูอ่านหนังสือ\nแล้วก็... สอนให้หนูปลูกดอกไม้ด้วย", "rene_sad"),
            new DialogueLine("", "เสียงของเธอค่อยๆ มั่นคงขึ้นเมื่อพูดถึงเรื่องที่ตัวเองชอบ", ""),
            new DialogueLine("เรเน่", "หนูชอบดอกไม้มากเลยค่ะ\nโดยเฉพาะดอกสีฟ้า คุณครูบอกว่ามันหายากมาก", "rene_happy"),
            new DialogueLine("", "คำพูดนั้นทำให้ชินชะงักไปเล็กน้อย", ""),
            new DialogueLine("ชิน", "...คนที่ผมรู้จักคนหนึ่งก็ชอบดอกไม้สีฟ้าเหมือนกัน", "shin_guilt"),
            new DialogueLine("เรเน่", "คนสำคัญของพี่เหรอคะ", "rene_curious"),
            new DialogueLine("ชิน", "...ใช่ เธอชื่อเร็น", "shin_neutral"),
            new DialogueLine("", "ชินไม่ได้พูดชื่อนั้นออกมานานแล้ว\nเสียงของมันฟังดูแปลกหู แม้แต่กับตัวเขาเอง", ""),
            new DialogueLine("ชิน", "เธอเคยบอกผมว่าถ้าเธอเกษียณแล้ว\nพวกเราก็จะไปปลูกบ้านและใช้ชีวิตอยู่ด้วยกัน\nและเธอจะปลูกดอกไม้เอาไว้ให้ผมเด็ดเล่นได้ตั้งแต่เช้าถึงเย็นเลย", "shin_guilt"),
            new DialogueLine("", "ชินหัวเราะเบาๆ ในลำคอ ใบหน้าของเขาปรากฏรอยยิ้มบางๆ ออกมา", ""),
            new DialogueLine("เรเน่", "โห้ ที่แห่งนั้นน่าจะเต็มไปด้วยผีเสื้อแน่ๆ เลยค่ะ\nถ้าถึงตอนนั้น พี่เองก็คงจะมีความสุขมากๆ แน่ๆ เลย", "rene_happy"),
            new DialogueLine("ชิน", "...นั่นสินะ", "shin_guilt"),
            new DialogueLine("", "ความเงียบตกลงมาระหว่างทั้งสองคน\nก่อนที่ชินจะพูดต่อช้าๆ", ""),
            new DialogueLine("ชิน", "ผมเคยคิดว่าถ้าผมทำตามคำสั่งทุกอย่าง\nสักวันผมอาจจะได้เจอเธออีกครั้ง", "shin_guilt"),
            new DialogueLine("เรเน่", "...พี่คงคิดถึงเธอมากเลยนะคะ", "rene_sad"),
            new DialogueLine("", "ชินไม่ได้ตอบ แต่ครั้งนี้ เขาไม่ได้ปฏิเสธ", ""),
            new DialogueLine("เรเน่", "ถ้า... ถ้าหนูออกไปจากที่นี่ได้จริงๆ\nหนูอยากลองไปดูทะเลสักครั้ง\nหนูเคยเห็นแค่ในหนังสือ มีเรือลำใหญ่ๆ ที่มีธงรูปสามเหลี่ยมด้วยล่ะ", "rene_curious"),
        }, () -> showChoice5());
    }

    private void showChoice5() {
        showChoice = true;
        choices.showChoices(
            new Choice[]{
                new Choice("ถ้ามีโอกาส ผมจะพาเธอไปดูนะ", 2, 0, 0),
                new Choice("ทะเลไม่ได้สวยอย่างที่คิดหรอก มันเต็มไปด้วยขยะแล้วตอนนี้", -1, 0, 0),
            },
            (IntConsumer)(idx -> {
                showChoice = false;
                loadBeforeBridge();
            })
        );
    }

    private void loadBeforeBridge() {
        dialogue.loadDialogue(new DialogueLine[]{
            new DialogueLine("", "บทสนทนาสั้นๆ ระหว่างทางในคืนหิมะตก\nค่อยๆ เติมเต็มช่องว่างระหว่างคนสองคนที่เพิ่งพบกันไม่ถึงชั่วโมง", ""),
        }, () -> loadBridge());
    }

    private void loadBridge() {
        currentBg = bgBridge;
        currentSprite = null;
        dialogue.loadDialogue(new DialogueLine[]{
            new DialogueLine("", "แสงไฟจากเมืองเริ่มปรากฏขึ้นที่ปลายทาง\nชินหยุดเดิน\nถ้าเดินต่อไปจากจุดนี้ เขาจะไม่อาจย้อนกลับมาได้อีก\nชินครุ่นคิดถึงเรื่องต่างๆ ที่ผ่านมา", ""),
        }, () -> {
            if (gsm.getReneAffection() >= 3) gsm.changeScene(GameState.ENDING_ESCAPE);
            else                             gsm.changeScene(GameState.ENDING_CAPTURE);
        });
    }

    // ─── render ───────────────────────────────────────────────────────────────────────────────────────

    @Override
    public void render(Graphics2D g2d) {
        if (currentBg != null) {
            g2d.drawImage(currentBg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(12, 15, 22), 1280, 720);
        }

        // narrator (spriteKey == "") → currentSprite = null → ไม่วาด
        if (!showChoice && dialogue.isActive()) {
            DialogueLine line = dialogue.getCurrentLine();
            if (line != null) {
                String key = line.getSpriteKey();
                if (key != null && !key.isEmpty()) {
                    currentSprite = resolveSprite(key);
                } else {
                    currentSprite = null;
                }
            }
        }

        if (currentSprite != null) {
            int th = Math.min(currentSprite.getHeight(), 560);
            int tw = currentSprite.getWidth() * th / currentSprite.getHeight();
            int sx = (1280 - tw) / 2;
            g2d.drawImage(currentSprite, sx, 720 - th, tw, th, null);
        }

        dialogue.update();
        if (showChoice) choices.render(g2d);
        else            dialogue.render(g2d);
        repaint();
    }

    private BufferedImage resolveSprite(String key) {
        switch (key) {
            case "shin_neutral": return imgShinNeutral;
            case "shin_guilt":   return imgShinGuilt;
            case "rene_curious": return imgReneCurious;
            case "rene_happy":   return imgReneHappy;
            case "rene_sad":     return imgReneSad;
            default:             return currentSprite;
        }
    }

    // ─── input ───────────────────────────────────────────────────────────────────────────────────────

    @Override
    public void onKeyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (showChoice) {
            if (k == KeyEvent.VK_1) choices.onKeyPressed(1);
            else if (k == KeyEvent.VK_2) choices.onKeyPressed(2);
        } else if (k == KeyEvent.VK_SPACE || k == KeyEvent.VK_ENTER) {
            dialogue.onSpacePressed();
        }
        repaint();
    }
}
