import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * ChurchHallScene — พบ Blindfold, Cypher, และเรเน่ในโบสถ์
 * มี 3 choices: CHOICE1 (สู้/คุย), CHOICE2 (defend/obey), CHOICE3 (truth/half/lie)
 * bg เปลี่ยน: gate → hall → balcony → altar → hall (กลับมา)
 * Inheritance: extends GameScene
 * Association: has-a DialogueSystem, ChoiceSystem
 */
public class ChurchHallScene extends GameScene {

    // Backgrounds
    private BufferedImage bgGate, bgHall, bgBalcony, bgAltar;
    private BufferedImage currentBg;

    // Sprites
    private BufferedImage spriteShinNeutral, spriteShinGuilt;
    private BufferedImage spriteReneHappy, spriteReneSad, spriteReneCurious;
    private BufferedImage spriteCypherNeutral, spriteCypherSpeak;
    private BufferedImage currentSprite;

    private DialogueSystem dialogueSystem;
    private ChoiceSystem choiceSystem;

    // Phase ของฉาก
    private enum Phase {
        GATE,           // หน้าโบสถ์
        HALL_ENTER,     // เข้าโบสถ์ พบ Blindfold
        CHOICE1,        // เลือก: สู้ / คุย
        FIGHT_RESULT,   // ผลการต่อสู้ Blindfold
        BALCONY,        // Cypher โผล่บนระเบียง
        CHOICE2,        // เลือก: defend / obey
        FIGHT_CYPHER,   // ต่อสู้ Cypher
        ALTAR,          // โดนเตะ กระแทกแท่นบูชา
        HALL_RENE,      // กลับมาที่ hall พบเรเน่
        CHOICE3,        // เลือก: truth / half / lie
        CHOICE3_RESULT, // บทหลัง choice3
        DONE
    }

    private Phase phase = Phase.GATE;
    private int choice1 = -1; // 1=สู้, 2=คุย
    private int choice2 = -1; // 1=defend, 2=obey

    public ChurchHallScene() {
        super();
        dialogueSystem = new DialogueSystem();
        choiceSystem   = new ChoiceSystem();
        loadAssets();
        startGate();
    }

    private void loadAssets() {
        try { bgGate    = ImageIO.read(new File("assets/bg_church_gate_close.jpg")); } catch (IOException e) { bgGate    = null; }
        try { bgHall    = ImageIO.read(new File("assets/bg_church_hall.jpg"));       } catch (IOException e) { bgHall    = null; }
        try { bgBalcony = ImageIO.read(new File("assets/bg_church_balcony.jpg"));    } catch (IOException e) { bgBalcony = null; }
        try { bgAltar   = ImageIO.read(new File("assets/bg_church_altar.jpg"));      } catch (IOException e) { bgAltar   = null; }

        try { spriteShinNeutral  = ImageIO.read(new File("assets/shin_neutral.png"));  } catch (IOException e) { spriteShinNeutral  = null; }
        try { spriteShinGuilt    = ImageIO.read(new File("assets/shin_guilt.png"));    } catch (IOException e) { spriteShinGuilt    = null; }
        try { spriteReneHappy    = ImageIO.read(new File("assets/rene_happy.png"));    } catch (IOException e) { spriteReneHappy    = null; }
        try { spriteReneSad      = ImageIO.read(new File("assets/rene_sad.png"));      } catch (IOException e) { spriteReneSad      = null; }
        try { spriteReneCurious  = ImageIO.read(new File("assets/rene_curious.png")); } catch (IOException e) { spriteReneCurious  = null; }
        try { spriteCypherNeutral = ImageIO.read(new File("assets/cypher_neutral.png")); } catch (IOException e) { spriteCypherNeutral = null; }
        try { spriteCypherSpeak   = ImageIO.read(new File("assets/cypher_speak.png"));   } catch (IOException e) { spriteCypherSpeak   = null; }

        currentBg = bgGate;
    }

    // ---- Phase starters ----

    private void startGate() {
        phase = Phase.GATE;
        currentBg = bgGate;
        currentSprite = spriteShinNeutral;
        DialogueLine[] lines = {
            new DialogueLine("", "บันไดหินที่นำขึ้นสู่ประตูโบสถ์ถูกปกคลุมด้วยหิมะจนแทบมองไม่เห็นขั้น\nประตูไม้บานใหญ่ปิดสนิท แต่รอยรองเท้าที่เหยียบหิมะยังสดใหม่", ""),
            new DialogueLine("ชิน", "มีคนเข้าออกอยู่จริงๆ สินะ", "shin_neutral"),
        };
        dialogueSystem.loadDialogue(lines, () -> startHallEnter());
    }

    private void startHallEnter() {
        phase = Phase.HALL_ENTER;
        currentBg = bgHall;
        DialogueLine[] lines = {
            new DialogueLine("", "ทันทีที่ผมผลักประตูเข้าไป อากาศเย็นชื้นก็ห่อหุ้มร่างไว้เหมือนหลุมฝังศพ\nแสงจากภายนอกส่องเข้ามาเพียงเล็กน้อย เผยให้เห็นโถงโบสถ์ที่ถูกทิ้งร้าง", ""),
            new DialogueLine("", "และเสียงฝีเท้า... ที่ไม่ใช่ของผม", ""),
            new DialogueLine("", "เงาร่างหลายคนก้าวออกมาจากมุมมืด ใบหน้าถูกปิดด้วยผ้าสีขาว", ""),
            new DialogueLine("ชิน", "พวก Blindfold...", "shin_guilt"),
            new DialogueLine("", "พวกมันไม่พูดอะไร แต่ดาบในมือสะท้อนแสงเย็นเฉียบ", ""),
        };
        dialogueSystem.loadDialogue(lines, () -> showChoice1());
    }

    private void showChoice1() {
        phase = Phase.CHOICE1;
        choiceSystem.showChoices(
            new Choice("พุ่งเข้าโจมตีก่อน", 0, 0, 0),
            new Choice("พยายามพูดก่อน",     0, 0, 0),
            () -> {}
        );
    }

    private void startFightResult() {
        phase = Phase.FIGHT_RESULT;
        DialogueLine[] pathA = {
            new DialogueLine("ชิน", "ผมไม่มีเวลาจะคุยกับพวกนาย", "shin_neutral"),
            new DialogueLine("", "ผมพุ่งเข้าใส่ก่อนที่พวกมันจะตั้งตัวทัน ดาบในมือกรีดอากาศเป็นเส้นโค้ง", ""),
            new DialogueLine("", "การต่อสู้จบลงเร็ว แต่เสียงโลหะกระทบกันดังก้องไปทั่วโบสถ์", ""),
        };
        DialogueLine[] pathB = {
            new DialogueLine("ชิน", "ผมยกมือขึ้นข้างลำตัวเป็นสัญญาณว่าไม่มีอาวุธใดติดตัวมาเลย\nเฮ้ ผมมาตัวเปล่า ช่วยพาผมไปหาหัวหน้าของพวกนายที่ได้ไหม", "shin_neutral"),
            new DialogueLine("", "หนึ่งในนั้นหัวเราะเบาๆ ใต้ผ้าปิดตา ก่อนจะพุ่งเข้ามาโดยไม่ตอบคำพูดใดๆ", ""),
            new DialogueLine("", "สุดท้าย ผมก็ยังต้องชักดาบออกมาอยู่ดี", ""),
        };
        DialogueLine[] chosen = (choice1 == 1) ? pathA : pathB;
        // บรรทัดร่วมหลังสู้เสร็จ
        DialogueLine[] common = {
            new DialogueLine("", "ร่างของพวก Blindfold นอนนิ่งอยู่บนพื้นหินเย็นเฉียบ", ""),
            new DialogueLine("ชิน", "...เป็นแบบนี้อีกแล้ว", "shin_guilt"),
            new DialogueLine("", "เสียงปรบมือดังขึ้นจากชั้นบนของโบสถ์", ""),
        };
        DialogueLine[] all = concat(chosen, common);
        dialogueSystem.loadDialogue(all, () -> startBalcony());
    }

    private void startBalcony() {
        phase = Phase.BALCONY;
        currentBg = bgBalcony;
        currentSprite = spriteCypherNeutral;
        DialogueLine[] lines = {
            new DialogueLine("", "ชายคนหนึ่งยืนพิงราวระเบียง ใบหน้าเขาไม่ได้ถูกปิดด้วยผ้าเหมือนคนอื่น\nดวงตาคมกริบจับจ้องลงมาที่ผม", ""),
            new DialogueLine("ไซเฟอร์", "น่าประทับใจจริงๆ Zero-Index", "cypher_neutral"),
            new DialogueLine("ชิน", "...ไซเฟอร์", "shin_neutral"),
            new DialogueLine("", "เขามาตัวเปล่าไร้ซึ่งอาวุธใดๆ ท่าทางผ่อนคลายเหมือนกำลังดูละครเวที", ""),
            new DialogueLine("ไซเฟอร์", "นายยังเหมือนเดิมเลยนะ เร็ว เด็ดขาด และ... ซื่อสัตย์", "cypher_speak"),
            new DialogueLine("ชิน", "พูดมากน่า…", "shin_guilt"),
            new DialogueLine("", "ไซเฟอร์ก้าวลงบันไดช้าๆ แล้วคว้าเด็กหญิงตัวเล็กคนหนึ่งออกมาจากด้านหลังแท่นบูชา\nมือของเขาจับไหล่เธอไว้แน่น", ""),
            new DialogueLine("เรเน่", "...", "rene_sad"),
            new DialogueLine("ชิน", "ปล่อยเด็กคนนั้นซะ", "shin_neutral"),
            new DialogueLine("ไซเฟอร์", "นายรู้อยู่แล้วว่าเด็กคนนี้คืออะไร อย่ามาแกล้งทำเป็นคนดีหน่อยเลย", "cypher_neutral"),
            new DialogueLine("", "คำพูดนั้นแทงเข้ามาในอกเหมือนใบมีด", ""),
            new DialogueLine("ไซเฟอร์", "ระบบไม่ได้ส่งนายมา \"ช่วย\" เธอหรอกนะ\nพวกมันก็แค่ต้องการดึงเธอกลับไป... เพื่อต่อชีวิตให้กับเมืองทุเรศๆ แห่งนี้ ก็เท่านั้น\nมันไม่ได้อะไรขึ้นมาเลย ไม่ได้มีอะไรที่พัฒนาขึ้น ทุกๆ อย่างมีแต่จะแย่ลงๆ\nนายก็รู้อยู่แก่ใจแล้วนี่ ชิน", "cypher_neutral"),
        };
        dialogueSystem.loadDialogue(lines, () -> showChoice2());
    }

    private void showChoice2() {
        phase = Phase.CHOICE2;
        choiceSystem.showChoices(
            new Choice("หยุดพูดจาไร้สาระแล้วปล่อยเธอซะ", 0, 0, 0),
            new Choice("ผมมาที่นี่เพื่อพาเธอกลับไป",       0, 0, 0),
            () -> {}
        );
    }

    private void startFightCypher() {
        phase = Phase.FIGHT_CYPHER;
        DialogueLine[] pathA = {
            new DialogueLine("ชิน", "หยุดพูดจาไร้สาระซะที\nปล่อยเด็กคนนั้นมาเดี๋ยวนี้", "shin_guilt"),
            new DialogueLine("ไซเฟอร์", "หึ... ฟังดูเหมือนพระเอกในนิทานเลยนะ\nแต่นายก็รู้อยู่แก่ใจนี่ ว่าถ้านายพาเธอกลับไป\nชะตากรรมของเด็กคนนี้จะจบลงแบบไหน", "cypher_speak"),
            new DialogueLine("", "ชินเอื้อมมือเข้าไปใต้เสื้อคลุม กำด้ามดาบเอาไว้แน่นจนข้อนิ้วซีดขาว", ""),
            new DialogueLine("ชิน", "...มันคือหน้าที่ของผม", "shin_guilt"),
        };
        DialogueLine[] pathB = {
            new DialogueLine("ชิน", "หน้าที่ของผมคือการนำเธอกลับไป ไม่ว่าจะเกิดอะไรขึ้นก็ตาม", "shin_neutral"),
            new DialogueLine("ไซเฟอร์", "โว้วๆ... บทพนักงานผู้ซื่อสัตย์นี่นายท่องจำมาดีจริงๆ\nแต่มันก็น่าตลกดีนะ", "cypher_neutral"),
        };
        DialogueLine[] common = {
            new DialogueLine("", "ไซเฟอร์ยิ้มบางๆ ก่อนจะยกมือขึ้นลูบศีรษะของเรเน่อย่างแผ่วเบา", ""),
            new DialogueLine("ไซเฟอร์", "รอฉันตรงนี้ก่อนนะ\nเดี๋ยวฉันจะรีบกลับมา หลังจากจัดการแขกที่ไม่ได้รับเชิญคนนี้เสร็จ\nไม่ต้องกลัวไปนะ", "cypher_speak"),
            new DialogueLine("", "ไซเฟอร์ส่งยิ้มให้อย่างอบอุ่น เรเน่พยักหน้าหงึกๆ อย่างสับสน", ""),
            new DialogueLine("", "เสียงโลหะเสียดสีกันดังก้องไปทั่วทั้งโบสถ์\nก่อนที่โซ่เส้นยาวหลายสิบเส้นจะพุ่งทะยานออกมาจากความมืด\nเปรี้ยง!!", ""),
            new DialogueLine("", "พื้นหินใต้เท้าของชินแตกร้าวเป็นทางยาวเมื่อโซ่เส้นหนึ่งฟาดลงมาเฉียดตัวเขาไปเพียงไม่กี่เซนติเมตร", ""),
            new DialogueLine("ชิน", "เกือบไปแล้ว...", "shin_guilt"),
            new DialogueLine("", "โซ่นับสิบเหวี่ยงไปมาราวกับงูเหล็กคลุ้มคลั่ง\nแต่ชินก็ยังสามารถใช้ดาบปัดป้องการโจมตีแต่ละครั้งได้อย่างหวุดหวิด\nทุกครั้งที่โลหะปะทะกัน เสียงดังสนั่นราวกับสายฟ้าฟาดลงกลางโบสถ์ร้าง", ""),
            new DialogueLine("ไซเฟอร์", "อ้าว ไหนล่ะ นักฆ่าฉายา Zero-Index ที่เขาร่ำลือกัน\nนายเก่งมากไม่ใช่เหรอ...\nหรือว่าเป็นเพราะ \"เร็น\" จากไปแล้ว", "cypher_speak"),
            new DialogueLine("", "คำพูดนั้นทำให้การเคลื่อนไหวของชินชะงักลงเพียงเสี้ยววินาที\nโซ่เส้นหนึ่งพุ่งเข้ามาฟาดใส่เขาอย่างเต็มแรง\nเปรี้ยง!!", ""),
        };
        DialogueLine[] chosen = (choice2 == 1) ? pathA : pathB;
        DialogueLine[] all = concat(chosen, common);
        dialogueSystem.loadDialogue(all, () -> startAltar());
    }

    private void startAltar() {
        phase = Phase.ALTAR;
        currentBg = bgAltar;
        currentSprite = spriteShinGuilt;
        DialogueLine[] lines = {
            new DialogueLine("", "แรงกระแทกทำให้ร่างของชินลอยละลิ่วก่อนจะทะลุผนังหินด้านข้างออกไป\nกระแทกกับพื้นอีกห้องหนึ่ง\nฝุ่นหินฟุ้งกระจายปกคลุมไปทั่ว", ""),
            new DialogueLine("ชิน", "...ไอ้หมอนั่น", "shin_guilt"),
            new DialogueLine("", "เลือดสีเข้มไหลอาบข้างแก้ม\nแต่ชินก็ยังฝืนดันตัวเองลุกขึ้นยืน ดาบในมือยังคงไม่หลุดไปไหน", ""),
            new DialogueLine("ไซเฟอร์", "นายยังยืนได้อีกเหรอ\nสมกับเป็นของเล่นชิ้นโปรดของพวกมันจริงๆ", "cypher_neutral"),
            new DialogueLine("", "ชินพุ่งตัวไปข้างหน้า ฝ่าเงามืดและโซ่ที่พุ่งเข้ามาอีกครั้ง\nก่อนจะเหวี่ยงดาบฟันตัดโซ่หลายเส้นขาดสะบั้น\nเสียงโลหะกระทบพื้นดังระงม", ""),
            new DialogueLine("ชิน", "เลิกพูดถึงชื่อของเธอซะที!!", "shin_guilt"),
            new DialogueLine("", "ชินพุ่งตัวเข้าไปโดยไม่ลังเล ดาบในมือฟาดลงด้วยแรงทั้งหมดที่มี\nแต่ปลายดาบกลับหยุดลงกลางอากาศตรงหน้าของเขา...\nมีเพียงโซ่ที่ถูกขึงเอาไว้แทนร่างของไซเฟอร์", ""),
            new DialogueLine("ชิน", "หา", "shin_neutral"),
            new DialogueLine("ไซเฟอร์", "Trick or treat", "cypher_speak"),
            new DialogueLine("", "เสียงกระซิบดังขึ้นข้างหลังเพียงเสี้ยววินาที\nก่อนที่แรงกระแทกจะมาถึง\nลำแข้งของไซเฟอร์ประเคนเข้าเต็มหน้าท้องของชินอย่างแม่นยำ", ""),
            new DialogueLine("", "แรงเตะนั้นหนักหน่วงเกินกว่าที่ร่างกายมนุษย์ปกติจะรับไหว\nร่างของชินลอยกระเด็นถอยหลังก่อนจะพุ่งเข้ากระแทกกับโพเดียมหน้าแท่นบัลลังก์อย่างจัง\nโครม!!", ""),
            new DialogueLine("", "ไม้เก่าแตกกระจาย เศษซากกระเด็นไปทั่วพื้นหิน", ""),
            new DialogueLine("ชิน", "...อั่ก…", "shin_guilt"),
            new DialogueLine("ไซเฟอร์", "นายยังไม่เข้าใจอีกเหรอ\nฉันเห็นภาพนี้ตั้งแต่นายยกดาบขึ้นครั้งแรกแล้ว", "cypher_neutral"),
            new DialogueLine("", "ไซเฟอร์เดินวนรอบตัวชินอย่างสบายๆ\nราวกับกำลังชมการแสดงที่รู้ตอนจบอยู่แล้ว", ""),
            new DialogueLine("ไซเฟอร์", "นายเก่งนะ ถ้าเป็นเมื่อก่อน...\nบางทีฉันอาจจะโดนฟันขาดครึ่งไปแล้วก็ได้\nแต่ปัญหาคือนายไม่ใช่คนเดิมอีกแล้ว", "cypher_speak"),
            new DialogueLine("ไซเฟอร์", "มีไอ้แก่คนนึงเคยบ่นกับฉันเอาไว้\n'การสูญเสียทำให้คนเปลี่ยนไป'\nแต่ก่อนฉันก็คิดว่ามันเพ้อเจ้อ\nแต่พอฉันได้มาเห็นสภาพของนายแล้ว แม่งโครตอุบาตเลยว่ะ", "cypher_speak"),
            new DialogueLine("", "ไซเฟอร์หยุดยืนอยู่ตรงหน้าเขา\nปลายรองเท้าจ่ออยู่ห่างจากใบหน้าของชินเพียงคืบเดียว\nเขาสามารถปิดฉากการต่อสู้ได้ในตอนนี้ แต่เขากลับไม่ทำ", ""),
            new DialogueLine("ไซเฟอร์", "หึหึหึ… ฮ่าฮ่าฮ่าฮ่าฮ่า\nน่าสนใจจริงๆ", "cypher_speak"),
            new DialogueLine("ไซเฟอร์", "วันนี้ฉันจะปล่อยพวกนายไปก่อนก็แล้วกัน\nเพราะภาพที่ฉันเห็น... มันน่าสนใจกว่านั้นมาก", "cypher_speak"),
            new DialogueLine("", "ดวงตาของไซเฟอร์เรืองแสงแผ่วๆ ขณะมองลงมาที่ชิน\nราวกับกำลังจ้องมองอะไรบางอย่างที่อยู่นอกเหนือจากปัจจุบัน", ""),
            new DialogueLine("ไซเฟอร์", "สุดท้ายแล้วนายจะเป็นคนพาเด็กคนนั้นกลับไปด้วยตัวเอง\nและตอนที่นายรู้ว่านายทำอะไรลงไป...\nสีหน้าของนายตอนนั้นน่าจะดูน่าสนใจกว่าการฆ่านายตอนนี้เยอะ", "cypher_speak"),
            new DialogueLine("", "เสียงไซเรนห่างไกลดังลอดเข้ามาทางหน้าต่างกระจกสีที่แตกเป็นช่องๆ\nหน่วยสนับสนุนขององค์กรกำลังเข้ามาใกล้", ""),
            new DialogueLine("ไซเฟอร์", "อ่า... ดูเหมือนเวลาของฉันจะหมดแล้ว", "cypher_speak"),
            new DialogueLine("", "ร่างนั้นปรากฏอยู่ตรงหน้าเรเน่ด้วยความเร็ว", ""),
            new DialogueLine("ไซเฟอร์", "ไม่ต้องห่วง ฉันไม่ได้ทิ้งเธอหรอก\nฉันแค่... ปล่อยให้นายเป็นคนเลือกทางให้เธอแทน", "cypher_speak"),
            new DialogueLine("", "โซ่หลายเส้นพุ่งออกมาจากความมืด พันรอบแขนและลำตัวของไซเฟอร์\nก่อนจะกระชากร่างของเขาถอยกลับเข้าไปยังทางเดินด้านหลังแท่นบูชา\nท่ามกลางความงงงวยของชินที่กำลังนอนเจ็บอยู่", ""),
            new DialogueLine("", "เพียงไม่กี่วินาที เงาร่างของเขาก็หายไปจากสายตา", ""),
        };
        dialogueSystem.loadDialogue(lines, () -> startHallRene());
    }

    private void startHallRene() {
        phase = Phase.HALL_RENE;
        currentBg = bgHall;
        currentSprite = spriteShinGuilt;
        DialogueLine[] lines = {
            new DialogueLine("", "เสียงโลหะเงียบลงอีกครั้ง\nเหลือเพียงชินที่ยังนอนหอบหายใจอยู่ท่ามกลางเศษไม้แตก\nและเด็กสาวที่ยืนตัวสั่นอยู่ไม่ไกลออกไป", ""),
            new DialogueLine("ชิน", "...แฮ่ก...", "shin_guilt"),
            new DialogueLine("", "ชินยันตัวลุกขึ้นช้าๆ มือข้างหนึ่งกุมหน้าท้องที่ยังปวดหนึบจากแรงเตะเมื่อครู่\nสายตาของเขาเลื่อนขึ้นไปหยุดอยู่ที่ร่างเล็กๆ ไม่ไกลออกไป\nเด็กสาวตัวน้อยกำลังมองมาที่เขาด้วยความเป็นห่วง", ""),
            new DialogueLine("เรเน่", "...พี่", "rene_curious"),
            new DialogueLine("ชิน", "เธอ... ไม่เป็นอะไรใช่ไหม", "shin_neutral"),
            new DialogueLine("", "เรเน่ถอยห่างออกจากชินไปอย่างอัตโนมัติ\nดูเหมือนว่าเธอจะได้ยินบทสนทนาของไซเฟอร์เข้าเต็มๆ", ""),
            new DialogueLine("เรเน่", "พี่... ไม่ได้เป็นพวกเดียวกับเขาใช่ไหมคะ\nแล้ว... ทำไมเขาถึงโจมตีพี่ล่ะคะ", "rene_curious"),
            new DialogueLine("ชิน", "เรื่องนั้น…\nผมคือตัวแทนจากองค์กร 'เอเทอร์น่า'\nและผมมาเพื่อรับตัวเธอกลับไป...", "shin_guilt"),
            new DialogueLine("", "คำพูดประโยคเดิมที่เขาเคยท่องจำซ้ำแล้วซ้ำเล่าเกือบหลุดออกมาจนจบเหมือนทุกครั้ง", ""),
            new DialogueLine("ชิน", "…", "shin_guilt"),
            new DialogueLine("", "ชินชะงักไปครู่หนึ่ง เสียงของไซเฟอร์ยังคงก้องอยู่ในหัวของเขา", ""),
            new DialogueLine("", "แบบนี้อีกแล้ว...\nคำพูดเดิมๆ ที่เขาใช้กับเด็กคนอื่น\nคำพูดที่ตามมาด้วยการหายตัวไปของพวกเขา", ""),
            new DialogueLine("ชิน", "ผม... ผมมาที่นี่เพื่อพาเธอไปหาคู่ชีวิตของเธอน่ะ", "shin_guilt"),
            new DialogueLine("", "คำโกหกที่ไม่สมบูรณ์แบบนัก หลุดออกมาจากปากของเขาอย่างไม่มั่นคง\nแม้แต่ตัวชินเองก็ยังรู้ว่ามันฟังดูไม่สมเหตุสมผล", ""),
            new DialogueLine("เรเน่", "...คู่รัก?", "rene_curious"),
            new DialogueLine("", "เรเน่เอียงศีรษะเล็กน้อย สีหน้าของเธอเต็มไปด้วยความสับสนมากกว่าความเชื่อ", ""),
            new DialogueLine("เรเน่", "หนู... ไม่เคยมีใครแบบนั้นเลยนะคะ", "rene_sad"),
            new DialogueLine("", "คำตอบนั้นทำให้ชินพูดไม่ออกไปชั่วขณะ\nเขารู้ดีว่าเรื่องที่ตัวเองเพิ่งพูดออกไปนั้นไม่มีมูลความจริงเลยแม้แต่น้อย", ""),
            new DialogueLine("เรเน่", "พี่... กำลังโกหกหนูอยู่หรือเปล่าคะ", "rene_curious"),
            new DialogueLine("", "คำถามนั้นไม่ได้มีน้ำเสียงกล่าวโทษ\nแต่มันตรงเกินพอที่จะทำให้ชินรู้สึกเหมือนถูกบีบคอ", ""),
        };
        dialogueSystem.loadDialogue(lines, () -> showChoice3());
    }

    private void showChoice3() {
        phase = Phase.CHOICE3;
        choiceSystem.showChoices(new Choice[]{
            new Choice("พูดความจริง", 2, 0, 0),
            new Choice("เสแสร้ง",     1, 0, 0),
            new Choice("หลอกลวง",    -1, 0, 0),
        }, () -> {});
    }

    private void startChoice3Result(int choice) {
        phase = Phase.CHOICE3_RESULT;
        DialogueLine[] pathA = {
            new DialogueLine("ชิน", "...ใช่ ผมโกหก", "shin_guilt"),
            new DialogueLine("", "คำสารภาพสั้นๆ หลุดออกมาหลังจากความเงียบยาวนาน", ""),
            new DialogueLine("ชิน", "ผมไม่รู้ว่าจะอธิบายยังไงดี\nแต่ที่แน่ๆ คือ... ที่ที่ผมกำลังจะพาเธอไป\nมันอาจจะไม่ใช่ที่ที่ดีอย่างที่ผมพูดไว้", "shin_guilt"),
            new DialogueLine("เรเน่", "...", "rene_sad"),
            new DialogueLine("", "เรเน่มองเขาอยู่นาน แต่ครั้งนี้เธอไม่ได้ถอยหนี", ""),
        };
        DialogueLine[] pathB = {
            new DialogueLine("ชิน", "ผมแค่... ไม่อยากให้เธอกลัวไปมากกว่านี้", "shin_neutral"),
        };
        DialogueLine[] pathC = {
            new DialogueLine("ชิน", "มันไม่ใช่เรื่องที่เธอต้องถามตอนนี้\nแค่ตามผมมาก็พอ", "shin_neutral"),
        };
        DialogueLine[] common = {
            new DialogueLine("", "ลมหนาวพัดผ่านโบสถ์ที่พังทลาย\nส่งเสียงหวีดหวิวลอดผ่านซากหน้าต่างกระจกสี", ""),
            new DialogueLine("ชิน", "ไปกันเถอะ", "shin_neutral"),
        };
        DialogueLine[] chosen;
        if (choice == 1)      chosen = pathA;
        else if (choice == 2) chosen = pathB;
        else                  chosen = pathC;
        DialogueLine[] all = concat(chosen, common);
        dialogueSystem.loadDialogue(all, () -> gsm.changeScene(GameState.SNOW_PATH_RETURN));
    }

    // Overriding: render() จาก GameScene
    @Override
    public void render(Graphics2D g2d) {
        if (currentBg != null) {
            g2d.drawImage(currentBg, 0, 0, 1280, 720, null);
        } else {
            FallbackRenderer.drawBackground(g2d, new Color(15, 10, 20), 1280, 720);
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
        if (key == null || key.isEmpty()) {
            currentSprite = null;  // narrator — ไม่วาด sprite
            return;
        }
        switch (key) {
            case "shin_neutral":   currentSprite = spriteShinNeutral;   break;
            case "shin_guilt":     currentSprite = spriteShinGuilt;     break;
            case "rene_happy":     currentSprite = spriteReneHappy;     break;
            case "rene_sad":       currentSprite = spriteReneSad;       break;
            case "rene_curious":   currentSprite = spriteReneCurious;   break;
            case "cypher_neutral": currentSprite = spriteCypherNeutral; break;
            case "cypher_speak":   currentSprite = spriteCypherSpeak;   break;
            default: break;
        }
    }

    // Overriding: onKeyPressed() จาก GameScene
    @Override
    public void onKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (choiceSystem.isVisible()) {
            if (key == KeyEvent.VK_1) handleChoice(1);
            else if (key == KeyEvent.VK_2) handleChoice(2);
            else if (key == KeyEvent.VK_3 && phase == Phase.CHOICE3) handleChoice(3);
        } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            dialogueSystem.onSpacePressed();
        }
    }

    private void handleChoice(int num) {
        choiceSystem.onKeyPressed(num);
        if (phase == Phase.CHOICE1) {
            choice1 = num;
            startFightResult();
        } else if (phase == Phase.CHOICE2) {
            choice2 = num;
            startFightCypher();
        } else if (phase == Phase.CHOICE3) {
            startChoice3Result(num);
        }
    }

    // utility: concat สอง array
    private DialogueLine[] concat(DialogueLine[] a, DialogueLine[] b) {
        DialogueLine[] result = new DialogueLine[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
