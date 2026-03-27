import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * OfficeScene — ฉากออฟฟิศ ชินรับภารกิจใหม่
 * Inheritance: extends GameScene
 * Association: has-a DialogueSystem, FallbackRenderer
 */
public class OfficeScene extends GameScene {

    private BufferedImage bg;
    private BufferedImage spriteShingGuilt;
    private BufferedImage spriteShingNeutral;
    private BufferedImage currentSprite;

    private DialogueSystem dialogueSystem;
    private boolean sceneStarted = false;

    // Dialogue lines ตาม script
    private static final DialogueLine[] LINES = {
        new DialogueLine("", "17:02 น.", ""),
        new DialogueLine("", "ตัวเลขสีขาวเล็กๆ ที่มุมหน้าจอกะพริบช้าๆ\nราวกับมันกำลังหายใจอยู่ในห้องที่เงียบเกินไป", ""),
        new DialogueLine("", "ผมยังนั่งอยู่ที่เดิม หลังโต๊ะตัวเดิม ทำงานเดิมซ้ำไปซ้ำมาเหมือนทุกวัน\nเก้าอี้ล้อเลื่อนใต้ตัวผมครางเบาๆ ทุกครั้งที่ผมขยับ", ""),
        new DialogueLine("", "แสงเย็นจากหน้าต่างถูกตึกฝั่งตรงข้ามกลืนไปเกือบหมด\nทิ้งไว้เพียงเงาครึ่งห้องที่ทอดยาวบนพื้นพรมสีหม่น", ""),
        new DialogueLine("", "บนหน้าจอ สเปรดชีตยาวเหยียดเต็มไปด้วยตัวเลขที่เรียงตัวอย่างเป็นระเบียบ\nตัวเลขเหล่านั้นไม่มีความหมายสำหรับใครบางคน\nแต่สำหรับเมืองนี้ มันคือทุกสิ่ง", ""),
        new DialogueLine("", "ไซเฟอร์เนียถูกสร้างขึ้นบนสมการ\nที่นี่ ทุกการกระทำถูกคาดการณ์ ทุกชีวิตถูกประเมินค่า\nและความผิดพลาด — คือสิ่งที่ระบบพยายามลบออกให้หมด", ""),
        new DialogueLine("ชิน", "ผู้คนชอบพูดกันว่า พระเจ้าเป็นนักคณิตศาสตร์", "shin_guilt"),
        new DialogueLine("ชิน", "และเมืองนี้ ก็เป็นเพียงกระดานดำของพระองค์", "shin_neutral"),
        new DialogueLine("ชิน", "ผมไม่เคยศรัทธาความคิดแบบนั้น", "shin_guilt"),
        new DialogueLine("ชิน", "แต่ก็ไม่เคยมีหลักฐานพอจะปฏิเสธมันเช่นกัน", "shin_neutral"),
        new DialogueLine("", "เสียงแจ้งเตือนดังขึ้นจากมุมขวาล่างของหน้าจอ\nแหลมและชัดเจนเกินไปสำหรับห้องที่เงียบขนาดนี้", ""),
        new DialogueLine("", "นิ้วของผมหยุดอยู่เหนือแป้นพิมพ์\nเคอร์เซอร์บนหน้าจอกระพริบต่อไป เหมือนมันกำลังรอคำสั่งจากผมเพียงคนเดียว", ""),
        new DialogueLine("SYSTEM", "\"คำสั่งใหม่ถูกส่งถึงคุณแล้ว\"", ""),
        new DialogueLine("SYSTEM", "\"โปรดตรวจสอบทันที\"", ""),
        new DialogueLine("ชิน", "\"...อีกแล้วเหรอ\"", "shin_guilt"),
        new DialogueLine("", "ผมถอนหายใจเบาๆ ก่อนจะเลื่อนเมาส์ไปคลิกเปิดข้อความ\nหน้าจอทั้งจอเปลี่ยนเป็นพื้นสีดำสนิท ตัวอักษรสีขาวค่อยๆ ปรากฏขึ้นทีละบรรทัด\nข้อความถูกเขียนด้วยไบนารี่โค้ดก่อนแล้วจึงสลับมาเป็นตัวอักษร\nบ่งบอกถึงผู้เขียนที่ดูลึกลับและเยือกเย็น", ""),
        new DialogueLine("SYSTEM", "\"ผู้รับ: SHIN // ZERO-INDEX\"", ""),
        new DialogueLine("", "ปลายนิ้วของผมหยุดนิ่งอยู่บนเมาส์", ""),
        new DialogueLine("ชิน", "ชื่อรหัสนั้น ผมไม่ได้เห็นมันมาหลายปีแล้ว\nครั้งสุดท้ายที่คำว่า Zero-Index ปรากฏขึ้นต่อหน้าผม\nคือคืนเดียวกับที่ผมตัดสินใจจะไม่จับดาบอีก", "shin_guilt"),
        new DialogueLine("SYSTEM", "\"ภารกิจ: กู้คืนตัวแปร RV-001\"", ""),
        new DialogueLine("SYSTEM", "\"สถานะ: เด็กหญิง\"", ""),
        new DialogueLine("ชิน", "ผมอ่านบรรทัดนั้นซ้ำสองครั้ง", "shin_guilt"),
        new DialogueLine("ชิน", "ไม่ใช่เพราะผมอ่านไม่ทัน", "shin_guilt"),
        new DialogueLine("ชิน", "แต่เพราะผมหวังว่ามันจะเปลี่ยนไปเอง", "shin_guilt"),
        new DialogueLine("SYSTEM", "\"ตำแหน่งปัจจุบัน: Winterhall District\"", ""),
        new DialogueLine("SYSTEM", "\"คาดว่ากลุ่มต่อต้าน The Blindfold น่าจะไปถึงก่อนแล้ว\"", ""),
        new DialogueLine("ชิน", "\"...เด็กงั้นเหรอ\"", "shin_neutral"),
        new DialogueLine("", "ผมเอนหลังพิงพนักเก้าอี้ เสียงสปริงภายในมันครางเบาๆ เหมือนกำลังประท้วง\nผมเคยทำงานแบบนี้มานับครั้งไม่ถ้วน แต่ครั้งนี้ มันต่างออกไปเล็กน้อย", ""),
        new DialogueLine("", "บางทีอาจเป็นเพราะคำว่า เด็กหญิง\nหรือบางทีอาจเป็นเพราะชื่อรหัสที่ถูกปลุกขึ้นมาจากอดีต\nหรือบางที อาจเป็นเพราะผมเริ่มเหนื่อยกับการเป็นชิ้นส่วนหนึ่งของสมการนี้เต็มทน", ""),
        new DialogueLine("", "เมื่อผมเงยหน้าขึ้นจากหน้าจออีกครั้ง ด้านนอกหน้าต่างก็มืดสนิทแล้ว\nพนักงานคนอื่นกลับไปหมดตั้งแต่เมื่อไหร่ก็ไม่รู้\nเหลือเพียงเสียงเครื่องปรับอากาศ กับเสียงหัวใจของผมเอง", ""),
        new DialogueLine("", "บางครั้งผมรู้สึกเหมือนมีใครบางคนยืนอยู่ด้านหลัง\nเฝ้ามองทุกการเคลื่อนไหวของผม ทุกการตัดสินใจ\nราวกับผมเป็นเพียงตัวละครในเรื่องราวของใครสักคน\nแต่ทุกครั้งที่ผมหันกลับไป ก็ไม่มีใครอยู่ตรงนั้นเลย", ""),
        new DialogueLine("", "ผมลุกขึ้นจากเก้าอี้ ข้อเข่ารู้สึกตึงเล็กน้อยจากการนั่งนานเกินไป\nเสื้อโค้ตสีเข้มถูกพาดอยู่บนพนักเก้าอี้ข้างๆ ผมหยิบมันขึ้นมาสวมช้าๆ", ""),
        new DialogueLine("", "ใต้โต๊ะทำงาน มีดาบหักเล่มหนึ่งถูกซ่อนไว้ในที่ที่ไม่มีใครมองเห็น\nมันควรจะเป็นเพียงของที่ระลึกจากอดีตที่ผมทิ้งไว้ข้างหลัง\nแต่ผมก็ยังไม่เคยกล้าทิ้งมันไปจริงๆ", ""),
        new DialogueLine("", "ผมจ้องมันอยู่นานกว่าที่ควร\nก่อนจะก้มลง แล้วหยิบมันขึ้นมา\nน้ำหนักของมันยังคงคุ้นมือ อย่างน่าหงุดหงิด", ""),
        new DialogueLine("ชิน", "\"...เข้าใจแล้ว\"", "shin_neutral"),
        new DialogueLine("", "ผมไม่ได้พูดกับใครโดยเฉพาะ\nบางทีผมอาจพูดกับหน้าจอ\nหรือบางที ผมอาจพูดกับใครบางคนที่กำลังเฝ้ามองอยู่เงียบๆ", ""),
        new DialogueLine("", "ภารกิจใหม่เริ่มต้นขึ้นแล้ว\nและผมก็ไม่มีข้ออ้างจะปฏิเสธมันอีกต่อไป", ""),
    };

    public OfficeScene() {
        super();
        dialogueSystem = new DialogueSystem();
        loadAssets();
    }

    private void loadAssets() {
        try { bg = ImageIO.read(new File("assets/bg_office.png")); } catch (IOException e) { bg = null; }
        try { spriteShingGuilt  = ImageIO.read(new File("assets/shin_guilt.png"));   } catch (IOException e) { spriteShingGuilt  = null; }
        try { spriteShingNeutral = ImageIO.read(new File("assets/shin_neutral.png")); } catch (IOException e) { spriteShingNeutral = null; }
    }

    private void startScene() {
        sceneStarted = true;
        dialogueSystem.loadDialogue(LINES, () -> gsm.changeScene(GameState.SNOW_PATH_TO_WINTERHALL));
    }

    // Overriding: render() จาก GameScene (OOP: Overriding)
    @Override
    public void render(Graphics2D g2d) {
        if (!sceneStarted) startScene();

        // อัปเดต sprite ตาม dialogue line ปัจจุบัน
        updateSprite();

        // วาด background
        if (bg != null) {
            g2d.drawImage(bg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(20, 20, 35), 1280, 720);
        }

        // วาด sprite (scale ให้สูงไม่เกิน 560px ติดขอบล่าง)
        if (currentSprite != null) {
            int th = Math.min(currentSprite.getHeight(), 560);
            int tw = currentSprite.getWidth() * th / currentSprite.getHeight();
            int sx = (1280 - tw) / 2;
            int sy = 720 - th;
            g2d.drawImage(currentSprite, sx, sy, tw, th, null);
        }

        // วาด dialogue box
        dialogueSystem.update();
        dialogueSystem.render(g2d);

        repaint();
    }

    private void updateSprite() {
        DialogueLine line = dialogueSystem.getCurrentLine();
        if (line == null) return;
        String key = line.getSpriteKey();
        if (key == null || key.isEmpty()) {
            currentSprite = null;  // narrator — ไม่วาด sprite
            return;
        }
        switch (key) {
            case "shin_guilt":   currentSprite = spriteShingGuilt;   break;
            case "shin_neutral": currentSprite = spriteShingNeutral; break;
            default: break;
        }
    }

    // Overriding: onKeyPressed() จาก GameScene (OOP: Overriding)
    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }
}
