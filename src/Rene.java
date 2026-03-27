// Inheritance: Rene extends Character (OOP: Inheritance, Overriding)
public class Rene extends Character {

    public Rene() { super("เรเน่", "rene_happy"); }

    // Overloading: constructor รับชื่อที่กำหนดเอง
    public Rene(String name) { super(name, "rene_happy"); }

    // Overriding: กำหนด dialogue style ของเรเน่ (OOP: Overriding)
    @Override
    public String getDialogueStyle() { return "NPC_WARM"; }

    @Override
    public String toString() { return "Rene{" + getName() + ", " + getEmotion() + "}"; }
}
