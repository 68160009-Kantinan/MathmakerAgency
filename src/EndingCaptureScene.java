import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * EndingCaptureScene — ENDING 2: จับส่งองค์กร
 * เงื่อนไข: เลือก "พาเรเน่กลับองค์กร" หรือ "พาเรเน่หนี" แต่ reneAffection < 3
 * bg: badEnd_room → badEnd_hall (เปลี่ยนที่ index BG_HALL_IDX)
 *
 * Inheritance: extends GameScene (OOP: Inheritance, Overriding)
 */
public class EndingCaptureScene extends GameScene {

    private BufferedImage bgRoom, bgHall;
    private BufferedImage currentBg;
    private BufferedImage imgShinNeutral;
    private BufferedImage imgShinGuilt;
    private BufferedImage imgReneSad;
    private BufferedImage currentSprite;

    private DialogueSystem dialogueSystem;

    private static final int BG_HALL_IDX = 15;

    private static final DialogueLine[] LINES = {
        new DialogueLine("", "แสงสีขาวของห้องทดลองส่องลงมาจากเพดานอย่างเย็นชา\nกลิ่นยาฆ่าเชื้อเจืออยู่ในอากาศจนแสบจมูก", ""),
        new DialogueLine("", "ร่างเล็กของเรเน่ถูกบังคับให้นั่งอยู่บนเตียงโลหะ\nข้อมือของเธอถูกล็อกด้วยสายรัดสีดำที่ชินคุ้นตาเป็นอย่างดี", ""),
        new DialogueLine("เรเน่", "...พี่", "rene_sad"),
        new DialogueLine("", "เสียงเรียกแผ่วเบานั้นสั่นไหวราวกับกำลังจะแตกสลาย", ""),
        new DialogueLine("ชิน", "...", "shin_neutral"),
        new DialogueLine("เรเน่", "พี่... เคยสัญญากับหนูไว้ไม่ใช่เหรอคะ\nว่าพี่จะพาหนูออกไปจากที่นี่...", "rene_sad"),
        new DialogueLine("", "ชินไม่ตอบ\nเขาเพียงแค่ยืนอยู่ตรงนั้นเหมือนรูปปั้นที่ถูกตั้งโชว์ไว้ในห้องทดลอง", "shin_neutral"),
        new DialogueLine("", "มือเล็กๆ ของเรเน่เอื้อมมาคว้าแขนเสื้อของเขาเอาไว้\nแรงสั่นสะเทือนจากนิ้วที่เกาะแน่นนั้น\nไม่ได้ต่างจากตอนที่เธอจับมือเขาไว้กลางหิมะในคืนนั้นเลย", ""),
        new DialogueLine("เรเน่", "...อย่าไปเลยค่ะ พี่", "rene_sad"),
        new DialogueLine("", "ชินค่อยๆ ดึงแขนตัวเองออกจากมือของเด็กสาวทีละนิ้ว\nเหมือนกำลังแกะเศษบางอย่างที่ติดอยู่บนผิวหนัง", "shin_guilt"),
        new DialogueLine("ชิน", "...", "shin_neutral"),
        new DialogueLine("", "เขาหันหลังให้เธอและเริ่มก้าวเดินออกจากห้องทดลองโดยไม่พูดอะไรอีก", "shin_neutral"),
        new DialogueLine("เรเน่", "...อย่าไปเลยค่ะ พี่", "rene_sad"),
        new DialogueLine("", "เสียงประตูเหล็กปิดลงดังสนั่น\nตัดขาดเสียงร้องเรียกที่ดังไล่หลังมา", ""),
        new DialogueLine("เรเน่", "พี่ชิน!!", "rene_sad"),
        new DialogueLine("", "เสียงนั้นยังคงก้องอยู่ในหัวของเขา\nแม้ว่าประตูจะปิดลงไปแล้วก็ตาม", "shin_guilt"),
        new DialogueLine("Teacher", "งานดีนี่ zero-index", ""),
        new DialogueLine("", "ชายในเสื้อคลุมสีขาวยืนพิงกำแพงรออยู่ก่อนแล้ว\nริมฝีปากของเขาโค้งขึ้นเป็นรอยยิ้มที่ไม่เคยส่งถึงดวงตา", ""),
        new DialogueLine("", "เขาดีดหลอดสีเขียวลอยไปในอากาศอย่างไม่ใส่ใจ\nชินยกมือขึ้นรับมันได้โดยสัญชาตญาณ", "shin_neutral"),
        new DialogueLine("Teacher", "รางวัลจากเจ้านายน่ะ\nนายควรจะรู้ว่ามันสำคัญแค่ไหน", ""),
        new DialogueLine("", "หลอดใสขนาดนิ้วหัวแม่มือ ภายในบรรจุของเหลวสีเขียวเรืองแสง", ""),
        new DialogueLine("", "ชินรู้ดีว่ามันคืออะไร\nBio-data\nสารที่หล่อเลี้ยงร่างกายของเร็น", "shin_guilt"),
        new DialogueLine("ชิน", "...", "shin_neutral"),
        new DialogueLine("", "เขากำแคปซูลนั้นไว้แน่น\nจนพลาสติกบางๆ ส่งเสียงเอี๊ยดเบาๆ ใต้แรงกด", "shin_guilt"),
        new DialogueLine("", "การตัดสินใจของเขาเมื่อครู่\nไม่ได้ช่วยเร็น ไม่ได้ช่วยเรเน่\nมันเพียงแค่ทำให้เขายังมีชีวิตอยู่ต่อไปในฐานะเครื่องมือของพวกมัน", "shin_guilt"),
        new DialogueLine("", "ติ้ด... ติ้ด... ติ้ด...\nเสียงสั่นของโทรศัพท์ในกระเป๋าเสื้อดังขึ้น", ""),
        new DialogueLine("", "ชินหยุดเดินอยู่กลางทางเดินที่ว่างเปล่า\nก่อนจะหยิบอุปกรณ์สื่อสารขึ้นมาอย่างช้าๆ", "shin_neutral"),
        new DialogueLine("", "หน้าจอขึ้นชื่อผู้ส่ง: REN\nชินรู้ดีว่ามันไม่ใช่เธอ\nแต่เป็นระบบที่ใช้ข้อมูลชีวภาพของเธอเป็นกุญแจยืนยันตัวตน", "shin_guilt"),
        new DialogueLine("SYSTEM", "MISSION UPDATE\nSUBJECT R-09 SECURED\nOPERATOR: SHIN / ZERO-INDEX\nSTATUS: SUCCESS", ""),
        new DialogueLine("", "ชินเลื่อนหน้าจอลง แม้จะรู้ดีว่าบรรทัดถัดไปคืออะไร", "shin_guilt"),
        new DialogueLine("SYSTEM", "NEXT TARGET ASSIGNED\nSUBJECT ID: F-18\nNAME: SERENA VULPES\nAGE: 18\nSPECIES: FOX-TRIBE", ""),
        new DialogueLine("", "คำว่า FOX-TRIBE ถูกไฮไลต์เป็นสีแดง\nเหมือนเป็นสิ่งที่องค์กรให้ความสำคัญเป็นพิเศษ", ""),
        new DialogueLine("", "ภาพถ่ายปรากฏขึ้นบนหน้าจอ\nหญิงสาวผมสีขาวยาวประบ่า ดวงตาสีฟ้าคมกริบจ้องมองเลนส์อย่างไม่หวั่นไหว\nใบหูจิ้งจอกสีขาวตั้งชันอยู่เหนือศีรษะ", ""),
        new DialogueLine("SYSTEM", "LAST KNOWN LOCATION:\nOUTER DISTRICT — WORKSHOP BLOCK C\nSTATUS: ACTIVE / UNCONTAINED", ""),
        new DialogueLine("ชิน", "...รับทราบ", "shin_neutral"),
        new DialogueLine("", "คำตอบสั้นๆ ถูกส่งกลับไปยังศูนย์บัญชาการ\nไม่กี่วินาทีต่อมา ข้อมูลแผนที่และเส้นทางการเคลื่อนที่ก็ถูกส่งกลับมา\nก่อนที่ชินจะค่อยๆ ลากร่างที่หนักอึ้งและเหนื่อยล้าของตัวเองออกไป", "shin_guilt"),
        new DialogueLine("", "— จบ —", ""),
    };

    public EndingCaptureScene() {
        super();
        dialogueSystem = new DialogueSystem();
        loadAssets();
        dialogueSystem.loadDialogue(LINES, () -> gsm.changeScene(GameState.MAIN_MENU));
    }

    private void loadAssets() {
        bgRoom         = load("badEnd_room");
        bgHall         = load("badEnd_hall");
        imgShinNeutral = load("shin_neutral");
        imgShinGuilt   = load("shin_guilt");
        imgReneSad     = load("rene_sad");
        currentBg      = bgRoom;
        currentSprite  = imgShinNeutral;
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

    @Override
    public void render(Graphics2D g2d) {
        currentBg = (dialogueSystem.getCurrentIndex() >= BG_HALL_IDX && bgHall != null)
                    ? bgHall : bgRoom;
        if (currentBg != null) {
            g2d.drawImage(currentBg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(20, 20, 20), 1280, 720);
        }
        updateSprite();
        if (currentSprite != null) {
            int th = Math.min(currentSprite.getHeight(), 560);
            int tw = currentSprite.getWidth() * th / currentSprite.getHeight();
            int sx = (1280 - tw) / 2;
            g2d.drawImage(currentSprite, sx, 720 - th, tw, th, null);
        }
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
            case "shin_neutral": currentSprite = imgShinNeutral; break;
            case "shin_guilt":   currentSprite = imgShinGuilt;   break;
            case "rene_sad":     currentSprite = imgReneSad;     break;
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }
}
