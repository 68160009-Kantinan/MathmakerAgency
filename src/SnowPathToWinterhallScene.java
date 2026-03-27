import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * SnowPathToWinterhallScene — เดินจากออฟฟิศไปยัง Winterhall District
 * 4 backgrounds: bg_city_snow_night → bg_snowpath → bg_bridge_snow → bg_winterhall_gate
 * Inheritance: extends GameScene
 */
public class SnowPathToWinterhallScene extends GameScene {

    // Backgrounds 4 ฉาก
    private BufferedImage bgCitySnow;
    private BufferedImage bgSnowpath;
    private BufferedImage bgBridgeSnow;
    private BufferedImage bgWinterhallGate;
    private BufferedImage currentBg;

    // Sprites
    private BufferedImage spriteShingNeutral;
    private BufferedImage spriteShingGuilt;
    private BufferedImage currentSprite;

    private DialogueSystem dialogueSystem;
    private boolean sceneStarted = false;

    // index ที่ bg เปลี่ยน (0-based index ใน LINES)
    private static final int BG_SNOWPATH_IDX       = 5;   // บรรทัดที่ 6 เปลี่ยนเป็น bg_snowpath
    private static final int BG_BRIDGE_IDX         = 11;  // บรรทัดที่ 12 เปลี่ยนเป็น bg_bridge_snow
    private static final int BG_WINTERHALL_IDX     = 17;  // บรรทัดที่ 18 เปลี่ยนเป็น bg_winterhall_gate

    private static final DialogueLine[] LINES = {
        // bg_city_snow_night
        new DialogueLine("", "ทันทีที่ผมผลักประตูกระจกออกไป ลมหนาวก็พุ่งเข้าปะทะร่างเหมือนตั้งใจรออยู่แล้ว", ""),
        new DialogueLine("", "ความอบอุ่นจากภายในอาคารถูกทิ้งไว้ด้านหลัง\nถูกแทนที่ด้วยอากาศเย็นเฉียบที่กัดลึกถึงกระดูก", ""),
        new DialogueLine("ชิน", "...หนาวกว่าที่คิดไว้แฮะ", "shin_neutral"),
        new DialogueLine("", "หิมะโปรยลงมาช้าๆ จากท้องฟ้าสีเทาหม่น\nละอองขาวเล็กๆ เกาะบนบ่าของผมก่อนจะละลายหายไป\nถนนหน้าอาคารสำนักงานว่างเปล่า เหมือนผู้คนทั้งเมืองหายไปพร้อมกันในเวลาเดียว", ""),
        // marker: index 4 = last line before bg_snowpath
        new DialogueLine("", "— — —", ""),
        // bg_snowpath (index 5)
        new DialogueLine("", "ผมเริ่มก้าวเดินไปตามทางเท้า เสียงรองเท้าบดหิมะดังกรอบแกรบเบาๆ ใต้ฝ่าเท้า\nแสงไฟถนนสีส้มส่องลงมาจากด้านบน ทำให้เงาของผมทอดยาวไปบนพื้นขาวโพลน", ""),
        new DialogueLine("ชิน", "ไซเฟอร์เนียตอนกลางคืน เงียบเกินไปทุกครั้งที่หิมะตก", "shin_guilt"),
        new DialogueLine("", "ตึกกระจกสูงที่เรียงรายอยู่สองข้างทาง สะท้อนภาพเมืองซ้ำซ้อนกันไปมาเหมือนภาพลวงตา\nหน้าต่างแทบทุกบานมืดสนิท มีเพียงบางห้องที่ยังเปิดไฟทิ้งไว้เหมือนลืมปิด", ""),
        new DialogueLine("ชิน", "คนส่วนใหญ่คงกลับบ้านไปแล้ว", "shin_neutral"),
        new DialogueLine("ชิน", "หรือไม่ก็... ไม่อยากออกมาเผชิญอากาศแบบนี้", "shin_guilt"),
        // marker: index 10 = last line before bg_bridge_snow
        new DialogueLine("", "— — —", ""),
        // bg_bridge_snow (index 11)
        new DialogueLine("", "สะพานคอนกรีตเล็กๆ ปรากฏขึ้นตรงหน้า\nมันเป็นเส้นแบ่งระหว่างเขตธุรกิจกับเขตเมืองเก่า\nด้านล่าง แม่น้ำสีดำไหลเอื่อยท่ามกลางเศษน้ำแข็งที่ลอยกระทบกันเป็นระยะ", ""),
        new DialogueLine("ชิน", "อีกฝั่งของสะพาน...", "shin_neutral"),
        new DialogueLine("ชิน", "ผมไม่ได้เหยียบไปที่นั่นมานานแล้ว", "shin_guilt"),
        new DialogueLine("", "มือของผมล้วงเข้าไปในกระเป๋าเสื้อโค้ตโดยไม่รู้ตัว\nนิ้วสัมผัสเข้ากับด้ามดาบที่พกติดมาจากออฟฟิศ\nความเย็นของโลหะซึมผ่านผ้าเข้ามา เตือนให้ผมนึกถึงอดีตที่พยายามลืม", ""),
        new DialogueLine("ชิน", "ผมเคยสาบานว่าจะไม่ใช้มันอีก", "shin_guilt"),
        new DialogueLine("ชิน", "แต่คำสาบานก็ไม่เคยมีความหมายเท่าภารกิจ", "shin_neutral"),
        // marker: index 17 = last line before bg_winterhall_gate
        new DialogueLine("", "— — —", ""),
        // bg_winterhall_gate (index 18)
        new DialogueLine("", "ป้ายเหล็กเก่าที่เขียนว่า \"Winterhall District\" เอียงอยู่ข้างถนน\nสีบนตัวอักษรถูกกาลเวลากับหิมะกัดกร่อนจนแทบอ่านไม่ออก", ""),
        new DialogueLine("", "ทันทีที่ผมก้าวข้ามเส้นแบ่งเขต บรรยากาศของเมืองก็เปลี่ยนไปอย่างชัดเจน\nตึกสูงถูกแทนที่ด้วยอาคารอิฐเก่า ถนนแคบลง และแสงไฟก็ดูริบหรี่กว่าที่ควรจะเป็น", ""),
        new DialogueLine("ชิน", "ที่นี่... ยังเหมือนเดิมเลยสินะ", "shin_neutral"),
        new DialogueLine("", "Winterhall เคยเป็นเขตศาสนาเก่า ก่อนที่รัฐบาลจะจัดให้ความเชื่อเป็นข้อมูลที่ไม่จำเป็นต่อระบบ\nหลังจากนั้น มันก็ถูกปล่อยทิ้งไว้เหมือนหน้าหนังสือที่ไม่มีใครอยากเปิดอ่านอีก", ""),
        new DialogueLine("ชิน", "และตอนนี้ พวก Blindfold ก็เลือกซ่อนตัวอยู่ที่นี่", "shin_guilt"),
    };

    public SnowPathToWinterhallScene() {
        super();
        dialogueSystem = new DialogueSystem();
        loadAssets();
    }

    private void loadAssets() {
        try { bgCitySnow       = ImageIO.read(new File("assets/bg_city_snow_night.jpg")); } catch (IOException e) { bgCitySnow       = null; }
        try { bgSnowpath       = ImageIO.read(new File("assets/bg_snowpath.png"));        } catch (IOException e) { bgSnowpath       = null; }
        try { bgBridgeSnow     = ImageIO.read(new File("assets/bg_bridge_snow.jpg"));     } catch (IOException e) { bgBridgeSnow     = null; }
        try { bgWinterhallGate = ImageIO.read(new File("assets/bg_winterhall_gate.jpg")); } catch (IOException e) { bgWinterhallGate = null; }
        try { spriteShingNeutral = ImageIO.read(new File("assets/shin_neutral.png")); } catch (IOException e) { spriteShingNeutral = null; }
        try { spriteShingGuilt   = ImageIO.read(new File("assets/shin_guilt.png"));   } catch (IOException e) { spriteShingGuilt   = null; }
        currentBg = bgCitySnow;
    }

    private void startScene() {
        sceneStarted = true;
        dialogueSystem.loadDialogue(LINES, () -> gsm.changeScene(GameState.CHURCH_HALL));
    }

    // Overriding: render() จาก GameScene
    @Override
    public void render(Graphics2D g2d) {
        if (!sceneStarted) startScene();

        updateBackground();
        updateSprite();

        if (currentBg != null) {
            g2d.drawImage(currentBg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(10, 15, 30), 1280, 720);
        }

        if (currentSprite != null) {
            int th = Math.min(currentSprite.getHeight(), 560);
            int tw = currentSprite.getWidth() * th / currentSprite.getHeight();
            int sx = (1280 - tw) / 2;
            int sy = 720 - th;
            g2d.drawImage(currentSprite, sx, sy, tw, th, null);
        }

        dialogueSystem.update();
        dialogueSystem.render(g2d);
        repaint();
    }

    private void updateBackground() {
        int idx = dialogueSystem.getCurrentIndex();
        if (idx >= BG_WINTERHALL_IDX) {
            currentBg = bgWinterhallGate != null ? bgWinterhallGate : currentBg;
        } else if (idx >= BG_BRIDGE_IDX) {
            currentBg = bgBridgeSnow != null ? bgBridgeSnow : currentBg;
        } else if (idx >= BG_SNOWPATH_IDX) {
            currentBg = bgSnowpath != null ? bgSnowpath : currentBg;
        } else {
            currentBg = bgCitySnow != null ? bgCitySnow : currentBg;
        }
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
            case "shin_neutral": currentSprite = spriteShingNeutral; break;
            case "shin_guilt":   currentSprite = spriteShingGuilt;   break;
            default: break;
        }
    }

    // Overriding: onKeyPressed() จาก GameScene
    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }
}
