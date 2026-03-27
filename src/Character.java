// Abstract Class: base class ของตัวละครทุกตัว (OOP: Abstract Class, Encapsulation)
// Overloading: มี 2 constructors
public abstract class Character {

    // Encapsulation: private fields เข้าถึงผ่าน getter/setter
    private String name;
    private String sprite;
    private String emotion;

    // Overloading: constructor แบบรับแค่ชื่อ
    public Character(String name) {
        this(name, "");
    }

    // Overloading: constructor แบบรับชื่อ + sprite เริ่มต้น
    public Character(String name, String defaultSprite) {
        this.name    = name;
        this.sprite  = defaultSprite;
        this.emotion = "neutral";
    }

    // Abstract method — subclass ต้อง implement (OOP: Abstract Class)
    public abstract String getDialogueStyle();

    // Getters
    public String getName()    { return name; }
    public String getSprite()  { return sprite; }
    public String getEmotion() { return emotion; }

    // Setters
    public void setName(String name)       { this.name = name; }
    public void setSprite(String sprite)   { this.sprite = sprite; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    // Overriding: subclass override ได้เพื่อเพิ่มรายละเอียด (OOP: Overriding)
    @Override
    public String toString() {
        return name + " [" + getDialogueStyle() + "] emotion=" + emotion;
    }
}
