import javax.swing.JFrame;
import javax.swing.SwingUtilities;

// Singleton: มี instance เดียวทั่วโปรแกรม
// Polymorphism: currentScene เป็นชนิด GameScene ชี้ไปยัง subclass ต่างๆ
// Encapsulation: ทุก field เป็น private
public class GameStateManager {

    private static GameStateManager instance;

    // Encapsulation: private fields
    // reneAffection — ความผูกพันระหว่างชินกับเรเน่ (ใช้ตัดสิน ending)
    private int reneAffection = 0;

    private GameState state = GameState.MAIN_MENU;
    private GameScene currentScene;  // Polymorphism: ชี้ไปยัง subclass
    private JFrame frame;

    private GameStateManager() {}

    // Singleton: getInstance()
    public static GameStateManager getInstance() {
        if (instance == null) instance = new GameStateManager();
        return instance;
    }

    public void setFrame(JFrame frame) { this.frame = frame; }

    // Overloading: addAffection แบบระบุค่า
    public void addAffection(int delta) { reneAffection += delta; }

    // Overloading: updateMeters — compat สำหรับ ChoiceSystem (trustDelta ใช้เป็น affection)
    public void updateMeters(int affectionDelta, int unused1, int unused2) {
        reneAffection += affectionDelta;
    }

    // Overloading: updateMeters แบบสองพารามิเตอร์
    public void updateMeters(int affectionDelta, int unused1) {
        reneAffection += affectionDelta;
    }

    // เปลี่ยน scene — Polymorphism: currentScene ชี้ไปยัง subclass ใหม่
    public void changeScene(GameState newState) {
        GameScene scene = createScene(newState);
        if (scene == null) return;
        state = newState;
        currentScene = scene;
        if (frame != null) {
            frame.setContentPane(scene);
            frame.revalidate();
            SwingUtilities.invokeLater(() -> frame.requestFocusInWindow());
        }
    }

    // Factory: สร้าง scene ตาม state (Polymorphism)
    private GameScene createScene(GameState s) {
        switch (s) {
            case MAIN_MENU:               return new MainMenu();
            case PROLOGUE:                return new PrologueScene();
            case OFFICE:                  return new OfficeScene();
            case SNOW_PATH_TO_WINTERHALL: return new SnowPathToWinterhallScene();
            case CHURCH_HALL:             return new ChurchHallScene();
            case SNOW_PATH_RETURN:        return new SnowPathReturnScene();
            case FINAL_CHOICE:            return new FinalChoiceScene();
            case ENDING_ESCAPE:           return new EndingEscapeScene();
            case ENDING_CAPTURE:          return new EndingCaptureScene();
            default:                      return new MainMenu();
        }
    }

    // Getters
    public GameScene getCurrentScene()  { return currentScene; }
    public GameState getCurrentState()  { return state; }
    public int getReneAffection()       { return reneAffection; }

    // compat stubs (ไม่ใช้แล้ว แต่เก็บไว้ให้ compile ผ่าน)
    public int getTrust()               { return reneAffection; }
    public int getGuilt()               { return 0; }
    public int getRenStability()        { return 0; }
}
