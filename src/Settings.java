import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.TreeMap;

public class Settings {

    private static final Image _1 = getImage("assets/number/1.png");
    private static final Image _2 = getImage("assets/number/2.png");
    private static final Image _3 = getImage("assets/number/3.png");
    private static final Image _4 = getImage("assets/number/4.png");
    private static final Image _5 = getImage("assets/number/5.png");
    private static final Image _6 = getImage("assets/number/6.png");
    private static final Image _7 = getImage("assets/number/7.png");
    private static final Image _8 = getImage("assets/number/8.png");
    private static final Image _9 = getImage("assets/number/9.png");
    private static final Image _0 = getImage("assets/number/0.png");

    private static final Image arrow = getImage("assets/arrow.png");
    private static final Image logo = getImage("assets/Logo.png");
    private static final Image alter_logo = getImage("assets/LogoAlternative.png");
    private static final Image trans_square = getImage("assets/trans_square.png");

    private static final Image title = getImage("assets/Texts/Tetris.png");
    private static final Image a = getImage("assets/Texts/A.png");
    private static final Image q = getImage("assets/Texts/Q.png");
    private static final Image e = getImage("assets/Texts/E.png");
    private static final Image d = getImage("assets/Texts/D.png");
    private static final Image s = getImage("assets/Texts/S.png");
    private static final Image z = getImage("assets/Texts/Z.png");
    private static final Image escape = getImage("assets/Texts/Escape.png");
    private static final Image loadgame = getImage("assets/Texts/Loadgame.png");
    private static final Image newgame = getImage("assets/Texts/Newgame.png");
    private static final Image settings = getImage("assets/Texts/Settings.png");
    private static final Image profil = getImage("assets/Texts/Profil.png");
    private static final Image menu = getImage("assets/Texts/menu.png");
    private static final Image left = getImage("assets/Texts/left.png");
    private static final Image right = getImage("assets/Texts/right.png");
    private static final Image downquick = getImage("assets/Texts/downquickly.png");
    private static final Image leftrotation = getImage("assets/Texts/leftrotation.png");
    private static final Image rightrotation = getImage("assets/Texts/rightrotation.png");
    private static final Image keybinding = getImage("assets/Texts/Keybinding.png");
    private static final Image exit = getImage("assets/Texts/Exit.png");
    private static final Image fullscreen = getImage("assets/Texts/fullscreen.png");
    private static final Image window = getImage("assets/Texts/window.png");
    private static final Image audio = getImage("assets/Texts/audio.png");
    private static final Image sfx = getImage("assets/Texts/sfx.png");
    private static final Image musics = getImage("assets/Texts/musics.png");
    private static final Image score = getImage("assets/Texts/score.png");
    private static final Image dep = getImage("assets/Texts/2p.png");
    private static final Image up = getImage("assets/Texts/up.png");
    private static final Image down = getImage("assets/Texts/down.png");
    private static final Image point = getImage("assets/Texts/point.png");
    private static final Image petitpoint = getImage("assets/Texts/petitpoint.png");
    private static final Image valid = getImage("assets/Texts/valid.png");
    private static final Image back = getImage("assets/Texts/back.png");
    private static final Image level = getImage("assets/Texts/level.png");
    private static final Image stat = getImage("assets/Texts/statistics.png");
    private static final Image next = getImage("assets/Texts/next.png");
    private static final Image scoreboard = getImage("assets/Texts/scoreboard.png");
    private static final Image statistics = getImage("assets/Texts/stat.png");
    private static final Image total = getImage("assets/Texts/total.png");
    private static final Image english = getImage("assets/Texts/english.png");
    private static final Image gameover = getImage("assets/Texts/gameover.png");
    private static final Image nextlevel = getImage("assets/Texts/nextlevel.png");
    private static final Image gameovercolor = getImage("assets/gameovercolor.png");
    private static final Image barre = getImage("assets/barre.png");
    private static final Image barreSelection = getImage("assets/selector_barre.png");




    private static final Image red_square = getImage("assets/red_square.png");
    private static final Image green_square = getImage("assets/green_square.png");
    private static final Image blue_square = getImage("assets/blue_square.png");
    private static final Image white_square = getImage("assets/white_square.png");
    private static final Image pink_square = getImage("assets/pink_square.png");
    private static final Image black_square = getImage("assets/black_square.png");
    private static final Image cyan_square = getImage("assets/cyan_square.png");
    private static final Image orange_square = getImage("assets/orange_square.png");
    private static final Image yellow_square = getImage("assets/yellow_square.png");
    private static final Image dark_square = getImage("assets/dark_square.png");
    private static final Image gray_square = getImage("assets/gray_square.png");


    private static final Image T = getImage("assets/Tetriminos/T.png");
    private static final Image L = getImage("assets/Tetriminos/L.png");
    private static final Image J = getImage("assets/Tetriminos/J.png");
    private static final Image I = getImage("assets/Tetriminos/I.png");
    private static final Image O = getImage("assets/Tetriminos/O.png");
    private static final Image S = getImage("assets/Tetriminos/S.png");
    private static final Image Z = getImage("assets/Tetriminos/Z.png");



    static final TreeMap<String, Image> Textures = new TreeMap<>();

    private static void loadTextures() {
        Textures.put("1", _1);
        Textures.put("2", _2);
        Textures.put("3", _3);
        Textures.put("4", _4);
        Textures.put("5", _5);
        Textures.put("6", _6);
        Textures.put("7", _7);
        Textures.put("8", _8);
        Textures.put("9", _9);
        Textures.put("0", _0);
        Textures.put("arrow", arrow);
        Textures.put("red", red_square);
        Textures.put("green", green_square);
        Textures.put("blue", blue_square);
        Textures.put("white", white_square);
        Textures.put("pink", pink_square);
        Textures.put("black", black_square);
        Textures.put("orange", orange_square);
        Textures.put("yellow", yellow_square);
        Textures.put("cyan", cyan_square);
        Textures.put("trans", trans_square);
        Textures.put("gray", gray_square);
        Textures.put("dark", dark_square);

        Textures.put("T_I", I);
        Textures.put("T_J", J);
        Textures.put("T_L", L);
        Textures.put("T_O", O);
        Textures.put("T_S", S);
        Textures.put("T_Z", Z);
        Textures.put("T_T", T);

        Textures.put("K_a", a);
        Textures.put("K_q", q);
        Textures.put("K_e", e);
        Textures.put("K_d", d);
        Textures.put("K_z", z);
        Textures.put("K_s", s);
        Textures.put("K_escape", escape);


        Textures.put("leftrotation", leftrotation);
        Textures.put("rightrotation", rightrotation);
        Textures.put("left", left);
        Textures.put("right", right);
        Textures.put("downquick", downquick);
        Textures.put("menu", menu);
        Textures.put("exit", exit);

        Textures.put("alter_logo", alter_logo);
        Textures.put("keybinding", keybinding);
        Textures.put("profil", profil);
        Textures.put("title", title);
        Textures.put("newgame", newgame);
        Textures.put("loadgame", loadgame);
        Textures.put("settings", settings);
        Textures.put("fullscreen", fullscreen);
        Textures.put("window", window);
        Textures.put("audio", audio);
        Textures.put("score", score);
        Textures.put("sfx", sfx);
        Textures.put("musics", musics);
        Textures.put(":", dep);
        Textures.put("up", up);
        Textures.put("down", down);
        Textures.put("valid", valid);
        Textures.put("back", back);
        Textures.put("point", point);
        Textures.put("petitpoint", petitpoint);
        Textures.put("level", level);
        Textures.put("next", next);
        Textures.put("stat", stat);
        Textures.put("scoreboard", scoreboard);
        Textures.put("stat", statistics);
        Textures.put("total", total);
        Textures.put("english", english);
        Textures.put("gameover", gameover);
        Textures.put("nextlevel", nextlevel);
        Textures.put("gameovercolor", gameovercolor);
        Textures.put("barre", barre);
        Textures.put("barreSelection", barreSelection);
        //Textures.put("gameovercolor", gameovercolor);
        //Textures.put("gameovercolor", gameovercolor);

        Textures.put("logo", logo);

        //        Textures.put("title", cursor);
    }

    public static Tetriminos[] loadTetriminos() {
        Tetriminos I = new Tetriminos(new int[][]{
            {1, 1, 1, 1}
        }, "I");
        Tetriminos L = new Tetriminos(new int[][]{
            {0, 0, 1},
            {1, 1, 1}
        }, "L");
        Tetriminos J = new Tetriminos(new int[][]{
            {1, 0, 0},
            {1, 1, 1}
        }, "J");

        Tetriminos T = new Tetriminos(new int[][]{
            {0, 1, 0},
            {1, 1, 1}
        }, "T");

        Tetriminos O = new Tetriminos(new int[][]{
            {1, 1},
            {1, 1}
        }, "O");
        Tetriminos Z = new Tetriminos(new int[][]{
            {1, 1, 0},
            {0, 1, 1}
        }, "Z");
        Tetriminos S = new Tetriminos(new int[][]{
            {0, 1, 1},
            {1, 1, 0}
        }, "S");


        return new Tetriminos[]{I, L, J, T, O, Z, S};
    }

    public static void setup() {
        loadTextures();
    }


    /**
     * Fonction permetant d'obtenir une image a partir d'un chemin de texture
     *
     * @param filename un chemin de texture
     * @return une image correspondant au chemin de texture
     */
    public static Image getImage(String filename) {
        return new ImageIcon(filename).getImage();
    }
}
