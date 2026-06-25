package config;
import java.awt.*;
public class Theme {
    // Backgrounds
    public static final Color BG       = new Color(8, 12, 24);
    public static final Color SIDEBAR  = new Color(12, 17, 35);
    public static final Color CARD     = new Color(18, 24, 45);
    public static final Color CARD2    = new Color(24, 32, 56);
    public static final Color INPUT_BG = new Color(28, 38, 65);
    // Accents
    public static final Color ACCENT   = new Color(99, 102, 241);
    public static final Color ACCENT2  = new Color(139, 92, 246);
    public static final Color GREEN    = new Color(16, 185, 129);
    public static final Color GREEN_LT = new Color(52, 211, 153);
    public static final Color YELLOW   = new Color(245, 158, 11);
    public static final Color RED      = new Color(239, 68, 68);
    public static final Color BLUE     = new Color(59, 130, 246);
    public static final Color ORANGE   = new Color(249, 115, 22);
    public static final Color PINK     = new Color(236, 72, 153);
    public static final Color CYAN     = new Color(6, 182, 212);
    // Text
    public static final Color TEXT     = new Color(241, 245, 249);
    public static final Color SUBTEXT  = new Color(148, 163, 184);
    public static final Color MUTED    = new Color(71, 85, 105);
    public static final Color BORDER   = new Color(40, 52, 85);
    // Table
    public static final Color TBL_HDR  = new Color(22, 30, 55);
    public static final Color ROW_ODD  = new Color(18, 24, 45);
    public static final Color ROW_EVEN = new Color(21, 29, 52);
    // Game colors (HSR, Genshin, Roblox, MLBB, FF, PUBG)
    public static final Color[] GAME_COLORS = {
        new Color(139,92,246), new Color(59,130,246), new Color(249,115,22),
        new Color(239,68,68),  new Color(16,185,129), new Color(245,158,11)
    };
    public static final Color[] GAME_BG = {
        new Color(60,30,120), new Color(20,50,120), new Color(100,40,10),
        new Color(100,20,20), new Color(10,70,50),  new Color(100,60,10)
    };
    // Fonts
    public static final Font F_LOGO  = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font F_HEAD  = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font F_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font F_BODY  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font F_SMBD  = new Font("Segoe UI", Font.BOLD,  11);
    public static final Font F_NUM   = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font F_BIG   = new Font("Segoe UI", Font.BOLD,  32);
}
