// Inheritance: Shin extends Character (OOP: Inheritance, Overriding)
public class Shin extends Character {

    public Shin() { super("ชิน", "shin_neutral"); }

    // Overloading: constructor รับชื่อที่กำหนดเอง
    public Shin(String name) { super(name, "shin_neutral"); }

    // Overriding: กำหนด dialogue style ของชิน (OOP: Overriding)
    @Override
    public String getDialogueStyle() { return "PROTAGONIST"; }

    @Override
    public String toString() { return "Shin{" + getName() + ", " + getEmotion() + "}"; }
}
