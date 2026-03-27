// Encapsulation: เก็บข้อมูลตัวเลือกหนึ่งตัว
public class Choice {

    private String text;
    private int trustDelta;
    private int guiltDelta;
    private int stabilityDelta;

    public Choice(String text, int trustDelta, int guiltDelta, int stabilityDelta) {
        this.text           = text;
        this.trustDelta     = trustDelta;
        this.guiltDelta     = guiltDelta;
        this.stabilityDelta = stabilityDelta;
    }

    public String getText()          { return text; }
    public int getTrustDelta()       { return trustDelta; }
    public int getGuiltDelta()       { return guiltDelta; }
    public int getRenStabilityDelta(){ return stabilityDelta; }
}
