import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Association (uses-a): RenPhone ใช้ GameStateManager
// Overloading: render() มีสองรูปแบบ — ระบุตำแหน่ง และใช้ default
// Encapsulation: ทุก field เป็น private
public class RenPhone {

    private static final int DX = 980, DY = 16;  // default position
    private static final int PW = 280, PH = 90;
    private static final int WARN = 30;

    private GameStateManager gsm;
    private long countdown;

    public RenPhone(GameStateManager gsm, long initialSeconds) {
        this.gsm       = gsm;
        this.countdown = initialSeconds;
    }

    // Overloading: render ระบุตำแหน่ง
    public void render(Graphics2D g2d, int x, int y) {
        boolean warn = gsm.getRenStability() < WARN;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(warn ? new Color(60, 5, 5, 230) : new Color(10, 10, 25, 220));
        g2d.fillRoundRect(x, y, PW, PH, 12, 12);
        g2d.setStroke(new BasicStroke(warn ? 2f : 1f));
        g2d.setColor(warn ? new Color(200, 50, 50) : new Color(100, 120, 180));
        g2d.drawRoundRect(x, y, PW, PH, 12, 12);

        // Timer
        g2d.setColor(warn ? new Color(255, 100, 100) : Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 26));
        g2d.drawString("[ " + fmt(countdown) + " ]", x + 12, y + 46);

        // Stability bar
        int stab = gsm.getRenStability();
        int bw = PW - 24;
        g2d.setColor(new Color(30, 30, 50));
        g2d.fillRoundRect(x + 12, y + 58, bw, 10, 4, 4);
        g2d.setColor(warn ? new Color(220, 60, 60) : new Color(80, 160, 255));
        g2d.fillRoundRect(x + 12, y + 58, (int)(bw * stab / 100.0), 10, 4, 4);

        // Label
        g2d.setColor(new Color(160, 160, 160));
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2d.drawString("STABILITY: " + stab + "%", x + 12, y + 82);
    }

    // Overloading: render ใช้ตำแหน่ง default
    public void render(Graphics2D g2d) { render(g2d, DX, DY); }

    public void vibrate() { /* visual effect — placeholder */ }

    public void tickCountdown() { if (countdown > 0) countdown--; }

    public long getCountdownSeconds()          { return countdown; }
    public void setCountdownSeconds(long secs) { countdown = secs; }

    private String fmt(long s) {
        if (s < 0) s = 0;
        return String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }
}
