import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.TreeSet;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Window extends JFrame {

    private Clip clipMusic;
    private FloatControl musicVolume;
    private FloatControl sfxVolume;

    private float volumeMusic = 0.7f;
    private float volumeSfx = 0.7f;

    public void playSound(String file) {
        try{
            Clip clipSfx = AudioSystem.getClip();
            File fichierAudio = new File("assets/mp3/"+file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
            clipSfx.open(audioStream);
            sfxVolume = (FloatControl) clipSfx.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumeSFX(volumeSfx);
            clipSfx.start();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void playMusic(String file) {
        try{
            if(clipMusic.isRunning() || clipMusic.isOpen()) {
                clipMusic.close();
            }
            File fichierAudio = new File("assets/mp3/"+file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
            clipMusic.open(audioStream);
            musicVolume = (FloatControl) clipMusic.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumeMUSIC(volumeMusic);
            clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
            clipMusic.start();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void stopMusic() {
        if (clipMusic != null && clipMusic.isRunning()) {
            clipMusic.stop();
        }
    }

    private void setVolumeMUSIC(float volume) {
        if(musicVolume != null) {
            float min = musicVolume.getMinimum();
            float max = musicVolume.getMaximum();
            float range = max-min;
            float gain = min + (range * volume);
            System.out.println("min: " + min);
            System.out.println("max: " + max);
            System.out.println("range: " + range);
            System.out.println("gain: " + gain);
            musicVolume.setValue(gain);
        }
    }

    private void setVolumeSFX(float volume) {
        if(sfxVolume != null) {
            float min = sfxVolume.getMinimum();
            float max = sfxVolume.getMaximum();
            float range = max-min;
            float gain = min + (range * volume);
            System.out.println("min: " + min);
            System.out.println("max: " + max);
            System.out.println("range: " + range);
            System.out.println("gain: " + gain);
            sfxVolume.setValue(gain);
        }
    }

    private static Font monocraft;

    private static int difficultyClassique[] = new int[100];

    public static final int height = 900;
    public static final int width = 1600;

    private TreeMap<String, Long> cooldown = new TreeMap<>();

    private BufferedImage onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    private Graphics2D onscreen = onscreenImage.createGraphics();
    private Graphics2D offscreen = offscreenImage.createGraphics();

    private static TreeSet<Integer> keysDown;
    private static ScoreBoard scoreboardClassique;
    private static ScoreBoard scoreboardInfini;

    private static int language = 0; // 0 english 1 french 2
    private static TreeMap<String, ArrayList<String>> words;

    public float fps = 0;
    private int speed;
    private boolean isHold = false;
    private long firstTake;
    private String letterTapped = "";

    private Level grille;
    private boolean pressedHold = false;
    private int cooldownTime = 50;

    private boolean transVersion = false;
    private Double d_Trans = 0.0;
    private Double d_T = 120.0;
    private Double d_S = 45.0;
    private Double d_Z = 270.0;
    private Double d_J = 72.0;
    private Double d_L = 195.0;
    private Double d_O = 100.0;
    private Double d_I = 10.0;

    public Window() {
        this.setSize(width, height);
    }

    private void setup() {
        setupWords();
        scoreboardClassique = function.exportScoreboard(true);
        scoreboardInfini = function.exportScoreboard(false);
        try {
            clipMusic = AudioSystem.getClip();
            //clipSfx = AudioSystem.getClip();
        } catch(Exception e) {
            System.out.println(e);
        }

        for(int i = 1; i<101; i++) {
            difficultyClassique[i-1] = (int) (Math.pow(10, (double) i/8.0)*100);
        }

        Settings.setup();
        this.setTitle("Tetris");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setContentPane(new JLabel(new ImageIcon(onscreenImage)));
        this.pack();
        this.requestFocusInWindow();
        this.setVisible(true);
        keysDown = new TreeSet<Integer>();

        try {
            monocraft = Font.createFont(Font.TRUETYPE_FONT, new File("assets/Monocraft.ttf")).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                letterTapped = String.valueOf(e.getKeyChar());

                if(!isHold) {
                    firstTake = System.currentTimeMillis();
                    isHold = true;
                }
                keysDown.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(grille != null) {
                    if(e.getKeyCode() == KeyEvent.VK_A) {
                        if(grille.checkCollisionLeft()) {
                            grille.x-=1;
                        }
                    } else if(e.getKeyCode() == KeyEvent.VK_D) {
                        if(grille.checkCollisionRight()) {
                            grille.x+=1;
                        }
                    } else if(e.getKeyCode() == KeyEvent.VK_Q) {
                        grille.getTetriminos().RtoLeft();
                        grille.checkPlacement();
                    } else if(e.getKeyCode() == KeyEvent.VK_E) {
                        grille.getTetriminos().RtoRight();
                        grille.checkPlacement();
                    } else if(e.getKeyCode() == KeyEvent.VK_L) {
                        playSound("clear.wav");
                    }
                }
                isHold = false;
                pressedHold = false;
                keysDown.remove(e.getKeyCode());
                System.out.println(e.getKeyCode());
            }
        });

        cooldown.put("Left", 0L); // E
        cooldown.put("Right", 0L); // A
        cooldown.put("RLeft", 0L); // Q
        cooldown.put("RRight", 0L); // D

        cooldown.put("back", 0L); // E
        cooldown.put("valid", 0L); // A
        cooldown.put("right", 0L); // Q
        cooldown.put("left", 0L); // D
        cooldown.put("up", 0L); // Z
        cooldown.put("down", 0L); // S
        cooldown.put("escape", 0L); //echap
        cooldown.put("exit", 0L); //x
        cooldown.put("delete", 0L); //x
        cooldown.put("entrer", 0L); //entrer
        cooldown.put("tapped", 0L); //entrer
        cooldown.put("temp", 0L); //temp
    }

    private String getLetterTapped() {
        return letterTapped;
    }

    public void run() {
        setup();
        this.setVisible(true);
        boolean isOpen = true;
        AffineTransform trans = new AffineTransform();
        trans.rotate(Math.toRadians(0), 800, 450);
        offscreen.setTransform(trans);
        boolean transVersion = false;
        int selection = 0;
        playMusic("menu.wav");
        setVolumeMUSIC(0.7f);
        while (isOpen) {

            if(keysDown.contains(KeyEvent.VK_S) && System.currentTimeMillis() - cooldown.get("down") > 150) {
                selection+=1;
                selection%=4;
                cooldown.put("down", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_W) && System.currentTimeMillis() - cooldown.get("up") > 150) {
                selection-=1;
                selection = selection<0? 3:selection;
                cooldown.put("up", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                switch (selection) {
                    case 0:
                        New();
                        break;
                    case 1:
                        Load();
                        break;
                    case 2:
                        Settings();
                        break;
                    case 3:
                        isOpen = false;
                        break;

                    default:
                        break;
                }
            }

            renderMainMenu(transVersion, trans, selection);
        }
        stopMusic();
        this.dispose();
    }

    private void New() {
        boolean newMenu = true;
        String pseudo = "";
        int selection = 0;
        int classique = 1;
        while(newMenu) {

            if(keysDown.contains(KeyEvent.VK_S) && System.currentTimeMillis() - cooldown.get("down") > 150) {
                selection+=1;
                selection%=4;
                cooldown.put("down", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_W) && System.currentTimeMillis() - cooldown.get("up") > 150) {
                selection-=1;
                selection = selection<0? 3:selection;
                cooldown.put("up", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                cooldown.put("back", System.currentTimeMillis());
                newMenu = false;
            }

            switch (selection) {
                case 0:
                    if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("right") > 150) {
                        classique+=1;
                        classique%=2;
                        cooldown.put("right", System.currentTimeMillis());
                    }

                    if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("left") > 150) {
                        classique-=1;
                        classique = classique<0? 1:classique;
                        cooldown.put("left", System.currentTimeMillis());
                    }
                    break;

                case 1:
                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                        cooldown.put("valid", System.currentTimeMillis());
                        cooldown.put("tapping", System.currentTimeMillis());
                        boolean inTapping = true;
                        while (inTapping) {
                            if(keysDown.contains(KeyEvent.VK_ENTER) && System.currentTimeMillis() - cooldown.get("entrer") > 150) {
                                cooldown.put("entrer", System.currentTimeMillis());
                                inTapping = false;
                            }
                            renderNewMenu(selection, classique, inTapping, pseudo);
                            if(pseudo.length()<10) {
                                if(!keysDown.isEmpty() && !keysDown.contains(KeyEvent.VK_BACK_SPACE) && !keysDown.contains(KeyEvent.VK_ENTER) && !keysDown.contains(KeyEvent.VK_SPACE) && !keysDown.contains(KeyEvent.VK_SEMICOLON) && System.currentTimeMillis() - cooldown.get("tapped") > 150) {
                                    cooldown.put("tapped", System.currentTimeMillis());
                                    if(getLetterTapped().charAt(0)>32 && getLetterTapped().charAt(0)<126) {
                                        pseudo+=getLetterTapped();
                                    }
                                }
                            }
                            if(pseudo.length()>0) {
                                if(keysDown.contains(KeyEvent.VK_BACK_SPACE) && System.currentTimeMillis() - cooldown.get("delete") > 150) {
                                    cooldown.put("delete", System.currentTimeMillis());
                                    pseudo = pseudo.substring(0, pseudo.length()-1);
                                }
                            }

                        }
                    }
                    break;

                case 2:
                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                        cooldown.put("valid", System.currentTimeMillis());
                        grille = new Level(1);
                        grille.setPseudo(pseudo);
                        stopMusic();
                        playMusic("game.wav");
                        if(classique%2!=0) {
                            ClassiqueGame();
                        } else {
                            InfinityGame();
                        }
                        newMenu = false;
                    }
                    break;

                case 3:
                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                        cooldown.put("valid", System.currentTimeMillis());
                        newMenu = false;
                    }
                    break;

                default:
                    break;
            }
            renderNewMenu(selection, classique, false, pseudo);
        }
    }

    private void Load() {
        boolean loadMenu = true;
        int selection = 0;
        Data saves[] = function.exportData();
        int max = saves.length<1? saves.length+1:saves.length;
        while (loadMenu) {

            if(keysDown.contains(KeyEvent.VK_S) && System.currentTimeMillis() - cooldown.get("down") > 150) {
                selection+=1;
                selection%=max+1;
                cooldown.put("down", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_W) && System.currentTimeMillis() - cooldown.get("up") > 150) {
                selection-=1;
                selection = selection<0? max:selection;
                cooldown.put("up", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                cooldown.put("back", System.currentTimeMillis());
                loadMenu = false;
            }

            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                if(selection<saves.length) {
                    launchGameFromData(saves[selection]);
                    loadMenu = false;
                } else {
                    loadMenu = false;
                }
            }


            if(keysDown.contains(KeyEvent.VK_X) && System.currentTimeMillis() - cooldown.get("delete") > 150) {
                cooldown.put("delete", System.currentTimeMillis());
                boolean areYouSurAboutThat = false;
                while (!areYouSurAboutThat) {
                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                        cooldown.put("valid", System.currentTimeMillis());
                        // delete
                        Data newSaves[] = new Data[saves.length-1];
                        for(int i = 0; i<saves.length; i++) {
                            if(i<selection) {
                                newSaves[i] = saves[i];
                            } else if(i>selection) {
                                newSaves[i-1] = saves[i];
                            }
                        }
                        function.writeData(newSaves);
                        saves = function.exportData();
                        max = saves.length<1? saves.length+1:saves.length;
                        areYouSurAboutThat = true;
                    }
                    if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                        cooldown.put("back", System.currentTimeMillis());
                        areYouSurAboutThat = true;
                    }
                    renderLoadMenu(selection, saves, true);
                }
            }

            renderLoadMenu(selection, saves, false);
        }
    }

    private void Settings() {
        boolean openMenu = true;
        int selection = 0;
        while(openMenu) {
            if(keysDown.contains(KeyEvent.VK_S) && System.currentTimeMillis() - cooldown.get("down") > 150) {
                selection+=1;
                selection%=4;
                cooldown.put("down", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_W) && System.currentTimeMillis() - cooldown.get("up") > 150) {
                selection-=1;
                selection = selection<0? 3:selection;
                cooldown.put("up", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                cooldown.put("back", System.currentTimeMillis());
                openMenu = false;
            }

            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                switch (selection) {
                    case 0:
                        KeyBinding();
                        break;
                    case 1:
                        language+=1;
                        language%=2;
                        break;
                    case 2:
                        Audio();
                        break;
                    case 3:
                        openMenu = false;
                        break;

                    default:
                        break;
                }
            }

            renderSettingsMenu(selection);

        }

    }

    private void KeyBinding() {
        boolean keymenuOpen = true;
        int page = 0;
        while (keymenuOpen) {
            if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                cooldown.put("back", System.currentTimeMillis()) ; keymenuOpen = false;
            }
            if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("right") > 150) {
                cooldown.put("right", System.currentTimeMillis()) ; page++; page%=3;
            }
            if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("left") > 150) {
                cooldown.put("left", System.currentTimeMillis()) ; page--; page = page<0?2:page;
            }
            rendererKeyBinding(page);
        }
    }

    private void Audio() {
        boolean openMenu = true;
        int selection = 0;
        while(openMenu) {
            if(keysDown.contains(KeyEvent.VK_S) && System.currentTimeMillis() - cooldown.get("down") > 150) {
                selection+=1;
                selection%=3;
                cooldown.put("down", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_W) && System.currentTimeMillis() - cooldown.get("up") > 150) {
                selection-=1;
                selection = selection<0? 2:selection;
                cooldown.put("up", System.currentTimeMillis());
            }

            if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                cooldown.put("back", System.currentTimeMillis());
                openMenu = false;
            }
            switch (selection) {
                case 1:
                    if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("left") > 150) {
                        cooldown.put("left", System.currentTimeMillis());
                        volumeMusic-=0.05f;
                        if(volumeMusic<0) {
                            volumeMusic = 0f;
                        }
                        setVolumeMUSIC(volumeMusic);
                        playSound("volumeModify.wav");
                        System.out.println("down volume");
                    }

                    if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("right") > 150) {
                        cooldown.put("right", System.currentTimeMillis());
                        volumeMusic+=0.05f;
                        if(volumeMusic>1) {
                            volumeMusic = 1f;
                        }
                        setVolumeMUSIC(volumeMusic);
                        playSound("volumeModify.wav");
                        System.out.println("up volume");
                    }
                    break;
                case 0:
                    if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("left") > 150) {
                        cooldown.put("left", System.currentTimeMillis());
                        volumeSfx-=0.05f;
                        if(volumeSfx<0) {
                            volumeSfx = 0f;
                        }
                        setVolumeSFX(volumeSfx);
                        playSound("volumeModify.wav");
                        System.out.println("down volume");
                    }

                    if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("right") > 150) {
                        cooldown.put("right", System.currentTimeMillis());
                        volumeSfx+=0.05f;
                        if(volumeSfx>1) {
                            volumeSfx = 1f;
                        }
                        setVolumeSFX(volumeSfx);
                        playSound("volumeModify.wav");
                        System.out.println("up volume");
                    }
                    break;
            }
            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                if(selection == 2) {
                    openMenu = false;
                }
            }
            renderAudioMenu(selection);
        }

    }

    private void launchGameFromData(Data save) {
        grille = new Level(save);
        stopMusic();
        playMusic("game.wav");
        if(save.getClassique()) {
            ClassiqueGame();
        } else {
            InfinityGame();
        }
    }

    private void ClassiqueGame() {

        boolean alive = true;
        boolean saveGame = false;
        while(alive) {
            speed = grille.getSpeed();
            long lastdelta = System.currentTimeMillis();
            boolean gamealive = true;
            while (gamealive) {
                checkHold();
                alive = !grille.gameOver();
                long now = System.currentTimeMillis();
                float delta = (now - lastdelta);
                lastdelta = now;

                if(keysDown.contains(KeyEvent.VK_S)) {
                    grille.setSpeed(speed*10);
                } else {
                    grille.setSpeed(speed);
                }

                if(pressedHold) {
                    if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("Left") > cooldownTime) {
                        if(grille.checkCollisionLeft()) {
                            grille.x-=1;
                        }
                        cooldown.put("Left", System.currentTimeMillis());
                    }

                    if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("Right") > cooldownTime) {
                        if(grille.checkCollisionRight()) {
                            grille.x+=1;
                        }
                        cooldown.put("Right", System.currentTimeMillis());
                    }
                    if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("RLeft") > cooldownTime) {
                        grille.getTetriminos().RtoLeft();
                        grille.checkPlacement();
                        cooldown.put("RLeft", System.currentTimeMillis());
                    }

                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("RRight") > cooldownTime) {
                        grille.getTetriminos().RtoRight();
                        grille.checkPlacement();
                        cooldown.put("RRight", System.currentTimeMillis());
                    }
                }

                if(keysDown.contains(KeyEvent.VK_ESCAPE) && System.currentTimeMillis() - cooldown.get("escape") > 150) {
                    cooldown.put("escape", System.currentTimeMillis());
                    boolean menuPause = true;
                    while (menuPause) {
                        if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                            cooldown.put("valid", System.currentTimeMillis());
                            save(true);
                            gamealive = false;
                            alive = false;
                            saveGame = true;
                            menuPause = false;
                        }

                        if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                            cooldown.put("back", System.currentTimeMillis());
                            menuPause = false;
                        }

                        if(keysDown.contains(KeyEvent.VK_X) && System.currentTimeMillis() - cooldown.get("exit") > 150) {
                            cooldown.put("exit", System.currentTimeMillis());
                            gamealive = false;
                            alive = false;
                            menuPause = false;
                            saveGame = true;
                        }


                        this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), false, false, true, true);
                    }
                }


                if(grille.x < 0) {
                    grille.x = 0;
                } else if(grille.x > Level.WIDTH) {
                    grille.x = Level.WIDTH-1;
                }

                grille.refresh(delta);
                if(grille.isSoundDeleteLayer()) {
                    playSound("clear.wav");
                }
                if(grille.getScore() > difficultyClassique[grille.getDifficulty()-1]) {
                    gamealive = false;
                }
                this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), false, false, false, true);
            }

            boolean toMenu = false;
            while(!toMenu && alive) {
                this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), false, !gamealive, false, true);
                if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                    cooldown.put("valid", System.currentTimeMillis());
                    toMenu = true;
                    grille.levelUp();
                }
            }
        }

        boolean toMenu = false;
        while(!toMenu && !saveGame) {
            this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), true, false, false, true);
            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                toMenu = true;
                scoreboardClassique.addPlayer(grille.getDifficulty(), grille.getScore(), grille.getPseudo());
                scoreboardClassique.sort();
                function.writeScoreboard(scoreboardClassique, true);
                function.deleteSave(grille.getPseudo());
            }

        }
        stopMusic();
    }

    private void InfinityGame() {

        boolean saveGame = false;
        boolean alive = true;
        speed = grille.getSpeed();
        long lastdelta = System.currentTimeMillis();
        while(alive) {
                checkHold();
                alive = !grille.gameOver();
                long now = System.currentTimeMillis();
                float delta = (now - lastdelta);
                lastdelta = now;

                if(keysDown.contains(KeyEvent.VK_S)) {
                    grille.setSpeed(speed*10);
                } else {
                    grille.setSpeed(speed);
                }

                if(pressedHold) {
                    if(keysDown.contains(KeyEvent.VK_A) && System.currentTimeMillis() - cooldown.get("Left") > cooldownTime) {
                        if(grille.checkCollisionLeft()) {
                            grille.x-=1;
                        }
                        cooldown.put("Left", System.currentTimeMillis());
                    }

                    if(keysDown.contains(KeyEvent.VK_D) && System.currentTimeMillis() - cooldown.get("Right") > cooldownTime) {
                        if(grille.checkCollisionRight()) {
                            grille.x+=1;
                        }
                        cooldown.put("Right", System.currentTimeMillis());
                    }
                    if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("RLeft") > cooldownTime) {
                        grille.getTetriminos().RtoLeft();
                        grille.checkPlacement();
                        cooldown.put("RLeft", System.currentTimeMillis());
                    }

                    if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("RRight") > cooldownTime) {
                        grille.getTetriminos().RtoRight();
                        grille.checkPlacement();
                        cooldown.put("RRight", System.currentTimeMillis());
                    }
                }

                if(keysDown.contains(KeyEvent.VK_ESCAPE) && System.currentTimeMillis() - cooldown.get("escape") > 150) {
                    cooldown.put("escape", System.currentTimeMillis());
                    boolean menuPause = true;
                    while (menuPause) {
                        if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                            cooldown.put("valid", System.currentTimeMillis());
                            save(false);
                            alive = false;
                            saveGame = true;
                            menuPause = false;
                        }

                        if(keysDown.contains(KeyEvent.VK_Q) && System.currentTimeMillis() - cooldown.get("back") > 150) {
                            cooldown.put("back", System.currentTimeMillis());
                            menuPause = false;
                        }

                        if(keysDown.contains(KeyEvent.VK_X) && System.currentTimeMillis() - cooldown.get("exit") > 150) {
                            cooldown.put("exit", System.currentTimeMillis());
                            alive = false;
                            saveGame = true;
                            menuPause = false;
                        }


                        this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), false, false, true, false);
                    }
                }


                if(grille.x < 0) {
                    grille.x = 0;
                } else if(grille.x > Level.WIDTH) {
                    grille.x = Level.WIDTH-1;
                }

                grille.refresh(delta);
                if(grille.isSoundDeleteLayer()) {
                    playSound("clear.wav");
                }
                grille.setDifficulty((int) (Math.pow(Math.log10(grille.getScore()), 1.5)));
                this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), false, false, false, false);
        }

        boolean toMenu = false;
        while(!toMenu && !saveGame) {
            this.renderGame(grille.getGrid(), grille.getNextTetriminos(), grille.x, grille.y, grille.getScore(), grille.getnbTetris(), grille.getDifficulty(), true, false, false, false);
            if(keysDown.contains(KeyEvent.VK_E) && System.currentTimeMillis() - cooldown.get("valid") > 150) {
                cooldown.put("valid", System.currentTimeMillis());
                toMenu = true;
                scoreboardInfini.addPlayer(grille.getDifficulty(), grille.getScore(), grille.getPseudo());
                scoreboardInfini.sort();
                function.writeScoreboard(scoreboardInfini, false);
                function.deleteSave(grille.getPseudo());
            }
        }
        stopMusic();
    }

    private void renderMainMenu(boolean transVersion, AffineTransform trans, int selection) {
        nettoyer();
        for(int i = 0; i<36; i++) {
            for(int j = 0; j<64; j++) {
                offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
            }
        }

        drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);
        drawString(words.get("newgame").get(language), 70f, 130, 480, Color.white, Color.black);
        drawString(words.get("loadgame").get(language), 70f, 130, 600, Color.white, Color.black);
        drawString(words.get("settings").get(language), 70f, 130, 720, Color.white, Color.black);
        drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);
        /*drawString("New game", 65f, 0, 0, Color.white, Color.black);
        drawString("Load game", 65f, 0, 0, Color.white, Color.black);
        drawString("Profil", 65f, 0, 0, Color.white, Color.black);
        drawString("Settings", 65f, 0, 0, Color.white, Color.black);
        drawString("Exit", 65f, 0, 0, Color.white, Color.black);
    */
        offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
        //offscreen.drawImage(Settings.Textures.get("title"), 50, 50, (int) (289*1.5), (int) (89*1.5), null);
        //offscreen.drawImage(Settings.Textures.get("newgame"), 120, 280, 433, 98, null);
        //offscreen.drawImage(Settings.Textures.get("loadgame"), 120, 400, 497, 98, null);
        //offscreen.drawImage(Settings.Textures.get("profil"), 120, 520, 271, 89, null);
        //offscreen.drawImage(Settings.Textures.get("settings"), 120, 640, 379, 98, null);
        //offscreen.drawImage(Settings.Textures.get("exit"), 120, 760, 181, 89, null);
        offscreen.drawImage(Settings.Textures.get("arrow"), 20, 400+(selection*120), 90, 90, null);

        renderPieceMoved();

        rafraichir();

    }

    private void renderNewMenu(int selection, int classique, boolean inTapping, String thetapper) {
        nettoyer();
        for(int i = 0; i<36; i++) {
            for(int j = 0; j<64; j++) {
                offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
            }
        }
        renderPieceMoved();

        drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);


        if(classique%2==0) {
            drawString(words.get("infini").get(language), 70f, 130, 480, Color.white, Color.black);
        } else {
            drawString(words.get("classique").get(language), 70f, 130, 480, Color.white, Color.black);
        }
        if(inTapping || thetapper.length()!=0) {
            drawString(thetapper, 70f, 130, 600, Color.white, Color.black);
        } else {
            drawString("name", 70f, 130, 600, Color.white, Color.black);
        }
        drawString(words.get("valid").get(language), 70f, 130, 720, Color.white, Color.black);

        drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);
        offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
        offscreen.drawImage(Settings.Textures.get("arrow"), 20, 400+((selection)*120), 90, 90, null);
        rafraichir();
    }

    private void renderLoadMenu(int selection, Data[] saves, boolean delete) {
        nettoyer();
        for(int i = 0; i<36; i++) {
            for(int j = 0; j<64; j++) {
                offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
            }
        }
        renderPieceMoved();

        drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);
        int decalage = 0;
        int push = 0;

        if(saves.length>0) {
            if(selection>3) {
                decalage = selection-3;
                if(selection == saves.length) {
                    decalage-=1;
                }
            } else {
                if(saves.length<4) {
                    push = 4-saves.length;
                }
            }
            for(int i = decalage; i<saves.length; i++) {
                if(i<4+decalage) {
                    drawString(saves[i].getPseudo(), 70f, 130, 360+(120*(push+i-decalage)), Color.white, Color.black);
                }
            }
        } else {
            push=3;
            drawString(words.get("nosave").get(language), 70f, 130, 360+(120*(push-decalage)), Color.white, Color.black);
        }
        System.out.println("push:" + push);
        System.out.println("decal:" + decalage);
        System.out.println("select:" + selection);

        // drawString(words.get("newgame").get(language), 70f, 130, 480, Color.white, Color.black);
        // drawString(words.get("loadgame").get(language), 70f, 130, 600, Color.white, Color.black);
        // drawString(words.get("settings").get(language), 70f, 130, 720, Color.white, Color.black);
        drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);
        /*drawString("New game", 65f, 0, 0, Color.white, Color.black);
        drawString("Load game", 65f, 0, 0, Color.white, Color.black);
        drawString("Profil", 65f, 0, 0, Color.white, Color.black);
        drawString("Settings", 65f, 0, 0, Color.white, Color.black);
        drawString("Exit", 65f, 0, 0, Color.white, Color.black);
    */
        offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
        //offscreen.drawImage(Settings.Textures.get("title"), 50, 50, (int) (289*1.5), (int) (89*1.5), null);
        //offscreen.drawImage(Settings.Textures.get("newgame"), 120, 280, 433, 98, null);
        //offscreen.drawImage(Settings.Textures.get("loadgame"), 120, 400, 497, 98, null);
        //offscreen.drawImage(Settings.Textures.get("profil"), 120, 520, 271, 89, null);
        //offscreen.drawImage(Settings.Textures.get("settings"), 120, 640, 379, 98, null);
        //offscreen.drawImage(Settings.Textures.get("exit"), 120, 760, 181, 89, null);
        offscreen.drawImage(Settings.Textures.get("arrow"), 20, 280+((push+selection-decalage)*120), 90, 90, null);
        if(delete) {
            drawString(words.get("delete").get(language)+"?", 70f, 650, 360+(120*(push+selection-decalage)), Color.white, Color.black);
        }
        rafraichir();
    }

    private void renderSettingsMenu(int selection) {
        nettoyer();
        for(int i = 0; i<36; i++) {
            for(int j = 0; j<64; j++) {
                offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
            }
        }

        offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
        //offscreen.drawImage(Settings.Textures.get("title"), 50, 50, (int) (289*1.5), (int) (89*1.5), null);
        drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);
        drawString(words.get("keybinding").get(language), 70f, 130, 480, Color.white, Color.black);

        //offscreen.drawImage(Settings.Textures.get("keybinding"), 120, 400, 433, 98, null);
        switch (language) {
            case 0:
                drawString(words.get("english").get(language), 70f, 130, 600, Color.white, Color.black);
                break;
            case 1:
                drawString(words.get("french").get(language), 70f, 130, 600, Color.white, Color.black);
                break;
            default:
                break;
        }

        drawString(words.get("audio").get(language), 70f, 130, 720, Color.white, Color.black);
        drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);

        //offscreen.drawImage(Settings.Textures.get("audio"), 120, 640, 253, 89, null);
        //offscreen.drawImage(Settings.Textures.get("exit"), 120, 760, 181, 89, null);
        offscreen.drawImage(Settings.Textures.get("arrow"), 20, 400+(selection*120), 90, 90, null);

        renderPieceMoved();

        rafraichir();
    }

    private void rendererKeyBinding(int page) {
        nettoyer();
            for(int i = 0; i<36; i++) {
                for(int j = 0; j<64; j++) {
                    offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
                }
            }

            int lenWord[][] = {
                {335, 275},
                {235, 315},
                {325, 425},
                {365, 425},

                {375, 370},
                {695, 560},
                {745, 835},
                {795, 835},

                {365, 455},
                {315, 425},
                {420, 510},
            };

            if(page == 0) {
                drawString(words.get("down").get(language), 70f, 130, 360, Color.white, Color.black);
                drawString(words.get("up").get(language), 70f, 130, 480, Color.white, Color.black);
                drawString(words.get("left").get(language), 70f, 130, 600, Color.white, Color.black);
                drawString(words.get("right").get(language), 70f, 130, 720, Color.white, Color.black);

                drawString(words.get("2p").get(language), 70f, lenWord[0][language], 360, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[1][language], 480, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[2][language], 600, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[3][language], 720, Color.white, Color.black);

                drawString(words.get("k_Z").get(language), 70f, lenWord[0][language]+70, 360, Color.white, Color.black);
                drawString(words.get("k_S").get(language), 70f, lenWord[1][language]+70, 480, Color.white, Color.black);
                drawString(words.get("k_Q").get(language), 70f, lenWord[2][language]+70, 600, Color.white, Color.black);
                drawString(words.get("k_D").get(language), 70f, lenWord[3][language]+70, 720, Color.white, Color.black);

            } else if(page == 1) {
                drawString(words.get("pause").get(language), 70f, 130, 360, Color.white, Color.black);
                drawString(words.get("downquick").get(language), 70f, 130, 480, Color.white, Color.black);
                drawString(words.get("leftR").get(language), 70f, 130, 600, Color.white, Color.black);
                drawString(words.get("rightR").get(language), 70f, 130, 720, Color.white, Color.black);

                drawString(words.get("2p").get(language), 70f, lenWord[4][language], 360, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[5][language], 480, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[6][language], 600, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[7][language], 720, Color.white, Color.black);

                drawString(words.get("k_escape").get(language), 70f, lenWord[4][language]+70, 360, Color.white, Color.black);
                drawString(words.get("k_S").get(language), 70f, lenWord[5][language]+70, 480, Color.white, Color.black);
                drawString(words.get("k_A").get(language), 70f, lenWord[6][language]+70, 600, Color.white, Color.black);
                drawString(words.get("k_E").get(language), 70f, lenWord[7][language]+70, 720, Color.white, Color.black);

            } else {
                drawString(words.get("valid").get(language), 70f, 130, 480, Color.white, Color.black);
                drawString(words.get("back").get(language), 70f, 130, 600, Color.white, Color.black);
                drawString(words.get("delete").get(language), 70f, 130, 720, Color.white, Color.black);

                drawString(words.get("2p").get(language), 70f, lenWord[8][language], 480, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[9][language], 600, Color.white, Color.black);
                drawString(words.get("2p").get(language), 70f, lenWord[10][language], 720, Color.white, Color.black);

                drawString(words.get("k_A").get(language), 70f, lenWord[8][language]+70, 480, Color.white, Color.black);
                drawString(words.get("k_E").get(language), 70f, lenWord[9][language]+70, 600, Color.white, Color.black);
                drawString(words.get("k_X").get(language), 70f, lenWord[10][language]+70, 720, Color.white, Color.black);
            }
            for(int i = 0; i<3; i++) {
                if(i == page) {
                    offscreen.drawImage(Settings.Textures.get("point"), 120+350+(i*150), 760, 73, 71, null);
                } else {
                    offscreen.drawImage(Settings.Textures.get("petitpoint"), 120+350+(i*150), 760, 37, 44, null);
                }
            }

            offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
            drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);
            drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);
            renderPieceMoved();
            rafraichir();

    }

    private void renderAudioMenu(int selection) {
        nettoyer();
        for(int i = 0; i<36; i++) {
            for(int j = 0; j<64; j++) {
                offscreen.drawImage(Settings.Textures.get("black"), j*25, i*25, 25, 25, null);
            }
        }
        offscreen.drawImage(Settings.Textures.get("logo"), 200+320, 10, 256, 256, null);
        drawString(words.get("title").get(language), 110f, 60, 170, Color.white, Color.black);
        drawString(words.get("sfx").get(language), 70f, 130, 600, Color.white, Color.black);

        int start = 435;
        if(language == 1) {
            start+=50;
        }

        offscreen.setColor(Color.RED);
        int base = (int) (320*1.2);
        if(volumeSfx<=0f) {
            volumeSfx+=0.05f;
        }
        int distanceSelecS = (int) (base*(volumeSfx-0.05f));
        if(distanceSelecS > base) {
            distanceSelecS = base;
        }
        int lenghtRectS = (int) (base*(volumeSfx-0.01f));
        offscreen.fillRect(start, 540, lenghtRectS, (int) (48*1.5));

        offscreen.drawImage(Settings.Textures.get("barre"), start, 540, (int) (320*1.2), (int) (48*1.5), null);
        offscreen.drawImage(Settings.Textures.get("barreSelection"), start+distanceSelecS, 535, 19, 84, null);

        if(volumeMusic<=0f) {
            volumeMusic+=0.05f;
        }
        int distanceSelecM = (int) (base*(volumeMusic-0.05f));
        if(distanceSelecS > base) {
            distanceSelecS = base;
        }
        int lenghtRectM = (int) (base*(volumeMusic-0.01f));
        offscreen.fillRect(start, 660, lenghtRectM, (int) (48*1.5));
        offscreen.drawImage(Settings.Textures.get("barre"), start, 660, (int) (320*1.2), (int) (48*1.5), null);
        offscreen.drawImage(Settings.Textures.get("barreSelection"), start+distanceSelecM, 655, 19, 84, null);

        drawString(words.get("music").get(language), 70f, 130, 720, Color.white, Color.black);

        drawString(words.get("exit").get(language), 70f, 130, 840, Color.white, Color.black);
        offscreen.drawImage(Settings.Textures.get("arrow"), 20, 520+(selection*120), 90, 90, null);
        renderPieceMoved();
        rafraichir();
    }

    private void renderPieceMoved() {
        AffineTransform trans = new AffineTransform();

        if(transVersion) {
            trans.rotate(Math.toRadians(d_Trans%360), 1200, 450);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("trans"), 1000, 250, 400, 400, null);
            trans.rotate(Math.toRadians(-(d_Trans%360)), 1200, 450);
            offscreen.setTransform(trans);
        } else {
            trans.rotate(Math.toRadians(d_Z%360), 1000+112, 500+75);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_Z"), 1000, 500, 225, 150, null);
            trans.rotate(Math.toRadians(-d_Z%360), 1000+112, 500+75);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_L%360), 1400+50, 700+75);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_L"), 1400, 700, 100, 150, null);
            trans.rotate(Math.toRadians(-d_L%360), 1400+50, 700+75);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_J%360), 900+50, 725+75);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_J"), 900, 725, 100, 150, null);
            trans.rotate(Math.toRadians(-d_J%360), 900+50, 725+75);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_O%360), 950+62, 200+62);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_O"), 950, 200, 125, 125, null);
            trans.rotate(Math.toRadians(-d_O%360), 950+62, 200+62);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_S%360), 1350+75, 450+50);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_S"), 1350, 450, 150, 100, null);
            trans.rotate(Math.toRadians(-d_S%360), 1350+75, 450+50);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_T%360), 1200+75, 250+50);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_T"), 1200, 250, 150, 100, null);
            trans.rotate(Math.toRadians(-d_T%360), 1200+75, 250+50);
            offscreen.setTransform(trans);

            trans.rotate(Math.toRadians(d_I%360), 1325+125, 110+31);
            offscreen.setTransform(trans);
            offscreen.drawImage(Settings.Textures.get("T_I"), 1325, 110, 250, 62, null);
            trans.rotate(Math.toRadians(-d_I%360), 1325+125, 110+31);
            offscreen.setTransform(trans);
        }
        d_Trans+=0.1;
        d_I+=0.075;
        d_J+=0.1;
        d_L+=0.04;
        d_O+=0.025;
        d_S+=0.08;
        d_T+=0.06;
        d_Z+=0.05;
    }

    private void renderGame(int[][] is, Tetriminos tetriminos, int x, int y, int score, int nbTetris, int difficulty, boolean gameover, boolean nextlevel, boolean pause, boolean classiqueScoreboard) {

        nettoyer();

        int policeSize[][] = {
            {35, 28},
            {80, 50},
            {45, 38},
            {65, 36},
            {55, 42},
            {50, 35},
            {65, 65},
            {35, 35},
            {35, 35}
        };
        int position[][] = {
            {830, 815},
            {120, 120}
        };
        //background
        for(int i = 0; i<25; i++) {
            for(int j = 0; j<44; j++) {
                if(!((i>4 && j>=1) && (i < 24 && j < 8)) && !((i>=1 && j>8) && (i < 23 && j < 21)) && !((i>=1 && j>21) && (i < 8 && j < 29)) && !((i>8 && j>21) && (i < 11 && j < 29)) && !((i>11 && j>21) && (i < 24 && j < 29)) && !((i>0 && j>29) && (i < 24 && j < 43))) {
                    offscreen.drawImage(Settings.Textures.get("black"), 8+(j*36), i*36, 36, 36, null);
                } else {
                    offscreen.drawImage(Settings.Textures.get("gray"), 8+(j*36), i*36, 36, 36, null);
                }
            }
        }

        for(int i = 0; i<25; i++) {
            for(int j = 0; j<44; j++) {
                if(i == 1 && j > 8 && j < 21) {
                    offscreen.drawImage(Settings.Textures.get("dark"), 8+(j*36), i*36, 36, 36, null);
                } else if(i == 22 && j > 8 && j < 21) {
                    offscreen.drawImage(Settings.Textures.get("dark"), 8+(j*36), i*36, 36, 36, null);
                } else if(j == 9 && i > 0 && i < 23) {
                    offscreen.drawImage(Settings.Textures.get("dark"), 8+(j*36), i*36, 36, 36, null);
                } else if(j == 20 && i > 0 && i < 23) {
                    offscreen.drawImage(Settings.Textures.get("dark"), 8+(j*36), i*36, 36, 36, null);
                }
            }
        }

        //offscreen.drawImage(Settings.Textures.get("title"), 20, 40, 289, 89, null);
        drawString(words.get("title").get(language), 65f, 30, 120, Color.white, Color.black);
        // stat
        drawString(words.get("stat").get(language), policeSize[0][language], 55, 240, Color.white, Color.black);
        //offscreen.drawImage(Settings.Textures.get("stat"), 10+(1*36), 5*36+10, (int) (432*0.57), (int) (89*0.57), null);
        int nb_tetris[] = grille.getStatPiece();
        Image imagepiece[] = {
            Settings.Textures.get("T_I"),
            Settings.Textures.get("T_J"),
            Settings.Textures.get("T_L"),
            Settings.Textures.get("T_O"),
            Settings.Textures.get("T_T"),
            Settings.Textures.get("T_S"),
            Settings.Textures.get("T_Z")
        };
        int sizeImagePiece[][] = {
            {64, 16},
            {32, 48},
            {32, 48},
            {32, 32},
            {48, 32},
            {48, 32},
            {48, 32},
        };
        AffineTransform trans = new AffineTransform();
        for(int i = 0; i<nb_tetris.length; i++) {
            int digit[] = extractDigit(nb_tetris[i]);

            if(i == 0) {
                offscreen.drawImage(imagepiece[i], 55, 280+i*75, (int) (sizeImagePiece[i][0]*1.5), (int) (sizeImagePiece[i][1]*1.5), null);
            } else if(i == 1 || i == 2) {
                trans.rotate(Math.toRadians(-90), 16+75, 24+270+i*75);
                offscreen.setTransform(trans);
                offscreen.drawImage(imagepiece[i], 75, 270+i*75, (int) (sizeImagePiece[i][0]*1.5), (int) (sizeImagePiece[i][1]*1.5), null);
                trans.rotate(Math.toRadians(90), 16+75, 24+270+i*75);
                offscreen.setTransform(trans);
            } else if(i == 3) {
                offscreen.drawImage(imagepiece[i], 75, 270+i*75, (int) (sizeImagePiece[i][0]*1.5), (int) (sizeImagePiece[i][1]*1.5), null);
            } else {
                offscreen.drawImage(imagepiece[i], 65, 270+i*75, (int) (sizeImagePiece[i][0]*1.5), (int) (sizeImagePiece[i][1]*1.5), null);
            }

            for(int j = 0; j<digit.length; j++) {
                offscreen.drawImage(Settings.Textures.get(""+digit[j]), 160+j*40, 270+i*75, (int) (73*0.6), (int) (89*0.6), null);

            }
        }
        offscreen.drawImage(Settings.Textures.get("total"), 50, 800, (int) (244*0.5), (int) (89*0.5), null);
        int total_digit[] = extractDigit(grille.getnbTetris());
        for(int j = 0; j<total_digit.length; j++) {
            offscreen.drawImage(Settings.Textures.get(""+total_digit[j]), 170+j*35, 800, (int) (73*0.5), (int) (89*0.5), null);
        }


        // grille
        for(int i = 2, a = 2*36; i<22; i++, a+=36) {
            for(int j = 10, b = 10*36+8; j<20; j++, b+=36) {
                if(is[i-2][j-10] != 0) {
                    switch (is[i-2][j-10]) {
                        case 1:
                            offscreen.drawImage(Settings.Textures.get("pink"), b, a, 36, 36, null);
                            break;
                        case 2:
                            offscreen.drawImage(Settings.Textures.get("yellow"), b, a, 36, 36, null);
                            break;
                        case 3:
                            offscreen.drawImage(Settings.Textures.get("cyan"), b, a, 36, 36, null);
                            break;
                        case 4:
                            offscreen.drawImage(Settings.Textures.get("red"), b, a, 36, 36, null);
                            break;
                        case 5:
                            offscreen.drawImage(Settings.Textures.get("green"), b, a, 36, 36, null);
                            break;
                        case 6:
                            offscreen.drawImage(Settings.Textures.get("orange"), b, a, 36, 36, null);
                            break;
                        case 7:
                            offscreen.drawImage(Settings.Textures.get("blue"), b, a, 36, 36, null);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // next
        int grid[][] = tetriminos.getGrid();
        //offscreen.drawImage(Settings.Textures.get("next"), 22*36+30, 36+10, 217, 89, null);
        drawString(words.get("next").get(language), policeSize[1][language], position[0][language], position[1][language], Color.white, Color.black);
        for(int i = 0, a = 36+150; i<grid.length; i++, a+=36) {
            for(int j = 0, b = 24*36; j<grid[i].length; j++, b+=36) {
                if(grid[i][j] != 0) {
                    switch (grid[i][j]) {
                        case 1:
                            offscreen.drawImage(Settings.Textures.get("pink"), b, a, 36, 36, null);
                            break;
                        case 2:
                            offscreen.drawImage(Settings.Textures.get("yellow"), b, a, 36, 36, null);
                            break;
                        case 3:
                            offscreen.drawImage(Settings.Textures.get("cyan"), b, a, 36, 36, null);
                            break;
                        case 4:
                            offscreen.drawImage(Settings.Textures.get("red"), b, a, 36, 36, null);
                            break;
                        case 5:
                            offscreen.drawImage(Settings.Textures.get("green"), b, a, 36, 36, null);
                            break;
                        case 6:
                            offscreen.drawImage(Settings.Textures.get("orange"), b, a, 36, 36, null);
                            break;
                        case 7:
                            offscreen.drawImage(Settings.Textures.get("blue"), b, a, 36, 36, null);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // level
        //offscreen.drawImage(Settings.Textures.get("level"), 5+8+(22*36), 9*36+10, (int) (262*0.6), (int) (89*0.6), null);
        if(classiqueScoreboard) {
            drawString(words.get("level").get(language), policeSize[2][language], 810, 380, Color.white, Color.black);
            int level_actual = grille.getDifficulty();
            int digit[] = extractDigit(level_actual);
            for(int i = 1; i<digit.length; i++) {
                offscreen.drawImage(Settings.Textures.get(""+digit[i]), 5+170+8+(22*36)-10+(i*40)-40, 9*36+12, (int) (73*0.55), (int) (89*0.55), null);
            }
        } else {
            drawString(words.get("infini").get(language), policeSize[2][language], 810, 380, Color.white, Color.black);
        }

        // stat other
        offscreen.drawImage(Settings.Textures.get("score"), 810, 440, (int) (289*0.65), (int) (89*0.65), null);
        offscreen.drawImage(Settings.Textures.get(":"), 1000, 445, (int) (37*0.65), (int) (80*0.65), null);
        int score_digit[] = extractDigitExtendPlus(score);
        for(int i = 1; i<score_digit.length; i++) {
            offscreen.drawImage(Settings.Textures.get(""+score_digit[i]), 775+i*30, 505, (int) (73*0.45), (int) (89*0.45), null);
        }
        // scoreboard
        //offscreen.drawImage(Settings.Textures.get("scoreboard"), 50+(29*36), 1*36+10, (int) (559*0.8), (int) (89*0.8), null);
        drawString(words.get("scoreboard").get(language), policeSize[3][language], 1110, 110, Color.white, Color.black);

        offscreen.setFont(monocraft.deriveFont(35f));

        if(scoreboardClassique.length > 0) {
            if(classiqueScoreboard) {
                for(int i = 0; i<scoreboardClassique.length; i++) {
                    drawString(scoreboardClassique.getlevel(i) + " - " + scoreboardClassique.getpseudo(i) + " " + scoreboardClassique.getscore(i),35f, 1100, (i*40)+160, Color.white, Color.black);
                }
            } else {
                for(int i = 0; i<scoreboardInfini.length; i++) {
                    drawString(scoreboardInfini.getpseudo(i) + " - " + scoreboardInfini.getscore(i),35f, 1100, (i*40)+160, Color.white, Color.black);
                }
            }
        } else {
            drawString(words.get("nosave").get(language), 35f, 1100, 160, Color.white, Color.black);
        }
        if(gameover) {
            offscreen.drawImage(Settings.Textures.get("gameovercolor"), 8+10*36, 2*36, 10*36, 20*36, null);
            drawString(words.get("gameover").get(language), policeSize[4][language], 380, 400, Color.white, Color.black);
            //offscreen.drawImage(Settings.Textures.get("gameover"), 375, 400, (int) (487*0.7), (int) (89*0.7), null);
        }
        if(nextlevel) {
            offscreen.drawImage(Settings.Textures.get("gameovercolor"), 8+10*36, 2*36, 10*36, 20*36, null);
            drawString(words.get("nextlevel").get(language), policeSize[5][language], 390, 400, Color.white, Color.black);
            //offscreen.drawImage(Settings.Textures.get("nextlevel"), 375, 400, (int) (479*0.7), (int) (89*0.7), null);
        }
        if(pause) {
            offscreen.drawImage(Settings.Textures.get("gameovercolor"), 8+10*36, 2*36, 10*36, 20*36, null);
            drawString(words.get("pause").get(language), policeSize[6][language], 450, 350, Color.white, Color.black);
            drawString(words.get("backtogame").get(language), policeSize[7][language], 380, 430, Color.white, Color.black);
            drawString(words.get("k_A").get(language), policeSize[8][language], 690, 430, Color.white, Color.black);
            drawString(words.get("save").get(language), policeSize[8][language], 400, 480, Color.white, Color.black);
            drawString(words.get("k_E").get(language), policeSize[8][language], 670, 480, Color.white, Color.black);
            drawString(words.get("exit").get(language), 35, 400, 520, Color.white, Color.black);
            drawString(words.get("k_X").get(language), 35, 670, 520, Color.white, Color.black);
            //offscreen.drawImage(Settings.Textures.get("menu"), 375, 400, (int) (479*0.7), (int) (89*0.7), null);
        }
        rafraichir();

    }

    public void drawString(String s, float size, int x, int y, Color write, Color glow) {
        offscreen.setFont(monocraft.deriveFont(size));
        offscreen.setColor(glow);
        int d = (int) (size/8);
        int delta = 4;
        if(size<46f) {
            delta = 2;
        }
        for (int dx = -d; dx <= d; dx+=delta) {
            for (int dy = -d; dy <= d; dy+=delta) {
                if (dx != 0 || dy != 0) {
                    offscreen.drawString(s, x+dx, y+dy);
                }
            }
        }
        offscreen.setColor(write);
        offscreen.drawString(s, x, y);
    }

    private void save(boolean classique) {
        Data d = new Data(grille.getPseudo(), grille.getDifficulty(), grille.getScore(), grille.getStatPiece(), grille.getOriginalGrid(), grille.getTetriminos().getType(), grille.getNextTetriminos().getType(), grille.x, grille.y, classique);
        Data ds[] = function.exportData();
        boolean alreadySave = false;
        int index = 0;
        int j = 0;
        for (Data data : ds) {
            if(data.getPseudo().equals(grille.getPseudo())) {
                alreadySave = true;
                index = j;
            }
            j++;
        }
        if(alreadySave) {
            ds[index] = d;
            function.writeData(ds);
        } else {
            Data newDs[] = new Data[ds.length+1];
            for(int i = 0; i<ds.length; i++) {
                newDs[i] = ds[i];
            }
            newDs[ds.length] = d;
            function.writeData(newDs);
        }
    }

    private void setupWords() {
        words = function.extractWord();
    }

    private void checkHold() {
        if(isHold) {
            long current = System.currentTimeMillis();
            if(current - firstTake > 120) {
                pressedHold = true;
            }
        }
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static Window newInstance() {
        return new Window();
    }


    /**
     * Met à jour le contenu de la fenêtre avec ce qui a été affiché depuis le
     * dernier appel de
     * rafraichir.
     */
    private void rafraichir() {
        onscreen.drawImage(offscreenImage, 0, 0, null);
        this.repaint();
    }

    /**
     * Nettoie le canvas
     */
    private void nettoyer() {
        offscreen.setColor(Color.BLACK);
        offscreen.fillRect(0, 0, width, height);
        // this.rafraichir();
    }

    private static int[] extractDigit(int n) {
        if(n>999) {
            return extractDigitExtend(n);
        } else {
            return new int[]{n/100, (n%100)/10, n%10};
        }
    }

    private static int[] extractDigitExtend(int n) {
        if(n > 999999) {
            return extractDigitExtendPlus(n);
        } else {
            return new int[]{
                n/100000,
                (n/10000)%10,
                (n/1000)%10,
                (n/100)%10,
                (n/10)%10,
                n%10
            };
        }
    }

    private static int[] extractDigitExtendPlus(int n) {
        return new int[]{
            (n/100000000)%10,
            (n/10000000)%10,
            (n/1000000)%10,
            (n/100000)%10,
            (n/10000)%10,
            (n/1000)%10,
            (n/100)%10,
            (n/10)%10,
            n%10
        };
    }

}