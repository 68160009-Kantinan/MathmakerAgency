import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * FinalChoiceScene — จุดตัดสินใจสุดท้ายบนสะพาน
 * CHOICE A: "พาเรเน่หนี" → reneAffection >= 3 → ENDING_ESCAPE, น้อยกว่า → ENDING_CAPTURE
 * CHOICE B: "พาเรเน่กลับองค์กร" → ENDING_CAPTURE เสมอ
 * Inheritance: extends GameScene
 * Association: has-a DialogueSystem, ChoiceSystem
 */
public class FinalChoiceScene extends GameScene {

    private BufferedImage bg;
    private BufferedImage spriteShinNeutral, spriteShinGuilt;
    private BufferedImage currentSprite;

    private DialogueSystem dialogueSystem;
    private ChoiceSystem choiceSystem;

    private enum Phase { INTRO, CHOICE, DONE }
    private Phase phase = Phase.INTRO;

    public FinalChoiceScene() {
        super();
        dialogueSystem = new DialogueSystem();
        choiceSystem   = new ChoiceSystem();
        loadAssets();
        startIntro();
    }

    private void loadAssets() {
        try { bg = ImageIO.read(new File("assets/bg_bridge_snow.jpg")); } catch (IOException e) { bg = null; }
        try { spriteShinNeutral = ImageIO.read(new File("assets/shin_neutral.png")); } catch (IOException e) { spriteShinNeutral = null; }
        try { spriteShinGuilt   = ImageIO.read(new File("assets/shin_guilt.png"));   } catch (IOException e) { spriteShinGuilt   = null; }
        currentSprite = spriteShinNeutral;
    }

    private void startIntro() {
        phase = Phase.INTRO;
        DialogueLine[] lines = {
            new DialogueLine("", "แสงไฟจากเมืองเริ่มปรากฏขึ้นที่ปลายทาง", ""),
            new DialogueLine("", "ชินหยุดเดิน\nถ้าเดินต่อไปจากจุดนี้ เขาจะไม่อาจย้อนกลับมาได้อีก", ""),
            new DialogueLine("", "ชินครุ่นคิดถึงเรื่องต่างๆ ที่ผ่านมา", ""),
        };
        dialogueSystem.loadDialogue(lines, () -> showFinalChoice());
    }

    private void showFinalChoice() {
        phase = Phase.CHOICE;
        choiceSystem.showChoices(
            new Choice("พาเรเน่หนี",         0, 0, 0),
            new Choice("พาเรเน่กลับองค์กร",  0, 0, 0),
            () -> {}
        );
    }

    private void resolveChoice(int num) {
        phase = Phase.DONE;
        if (num == 1) {
            // พาเรเน่หนี — ตรวจ reneAffection
            if (gsm.getReneAffection() >= 3) {
                gsm.changeScene(GameState.ENDING_ESCAPE);
            } else {
                gsm.changeScene(GameState.ENDING_CAPTURE);
            }
        } else {
            // พาเรเน่กลับองค์กร — ENDING_CAPTURE เสมอ
            gsm.changeScene(GameState.ENDING_CAPTURE);
        }
    }

    // Overriding: render() จาก GameScene
    @Override
    public void render(Graphics2D g2d) {
        if (bg != null) {
            g2d.drawImage(bg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(10, 12, 25), 1280, 720);
        }
        updateSprite();
        if (currentSprite != null) {
            int th = Math.min(currentSprite.getHeight(), 560);
            int tw = currentSprite.getWidth() * th / currentSprite.getHeight();
            int sx = (1280 - tw) / 2;
            int sy = 720 - th;
            g2d.drawImage(currentSprite, sx, sy, tw, th, null);
        }
        dialogueSystem.update();
        dialogueSystem.render(g2d);
        choiceSystem.render(g2d);
        repaint();
    }

    private void updateSprite() {
        DialogueLine line = dialogueSystem.getCurrentLine();
        if (line == null) return;
        String key = line.getSpriteKey();
        if (key == null || key.isEmpty()) return;
        switch (key) {
            case "shin_neutral": currentSprite = spriteShinNeutral; break;
            case "shin_guilt":   currentSprite = spriteShinGuilt;   break;
            default: break;
        }
    }

    // Overriding: onKeyPressed() จาก GameScene
    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (choiceSystem.isVisible()) {
            if (key == KeyEvent.VK_1) { choiceSystem.onKeyPressed(1); resolveChoice(1); }
            else if (key == KeyEvent.VK_2) { choiceSystem.onKeyPressed(2); resolveChoice(2); }
        } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }
}
