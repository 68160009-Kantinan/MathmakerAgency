// Encapsulation: เก็บข้อมูลหนึ่งบรรทัดของบทสนทนา
public class DialogueLine {

    private String speaker;  // ชื่อผู้พูด ("" = narration)
    private String text;     // ข้อความ
    private String sprite;   // sprite key ที่จะแสดง ("" = ไม่เปลี่ยน)

    public DialogueLine(String speaker, String text, String sprite) {
        this.speaker = speaker;
        this.text    = text;
        this.sprite  = sprite;
    }

    public String getSpeakerName() { return speaker; }
    public String getText()        { return text; }
    public String getSpriteKey()   { return sprite; }
}
