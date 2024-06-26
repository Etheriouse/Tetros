import java.util.Random;

public class Tetriminos {

    private int content[][];
    private String type;

    private static Tetriminos[] allTetriminos = Settings.loadTetriminos();

    public Tetriminos(int t[][], String type) {
        this.content = t;
        this.type = type;
    }

    public Tetriminos(String type) {
        int index = 0;
        for(int i = 0; i<allTetriminos.length; i++) {
            if(allTetriminos[i].getType().equals(type)) {
                index = i;
            }
        }

        this.content = new int[allTetriminos[index].getGrid().length][allTetriminos[index].getGrid()[0].length];
        for(int i = 0; i<allTetriminos[index].getGrid().length; i++) {
            for(int j = 0; j<allTetriminos[index].getGrid()[0].length; j++) {
                this.content[i][j] = allTetriminos[index].getGrid()[i][j];
            }
        }
        this.type = allTetriminos[index].getType();
    }

    public Tetriminos(Tetriminos t) {
        this.content = new int[t.getGrid().length][t.getGrid()[0].length];
        for(int i = 0; i<t.getGrid().length; i++) {
            for(int j = 0; j<t.getGrid()[0].length; j++) {
                this.content[i][j] = t.getGrid()[i][j];
            }
        }
        this.type = t.getType();
    }

    public void RtoRight() {
        this.content = function.rotationArrayToRight(content);
    }

    public void RtoLeft() {
        this.content = function.rotationArrayToLeft(content);
    }

    public int[][] getGrid() {
        return this.content;
    }

    public String getType() {
        return type;
    }

    public void show() {
        for (int[] is : content) {
            for (int i : is) {
                if(i != 0) {
                    System.out.print("# ");
                } else {
                    System.out.print("  ");
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    public static Tetriminos getRandomTetriminos() {
        Random r = new Random();
        return new Tetriminos(allTetriminos[r.nextInt(allTetriminos.length)]).randomColor();
    }

    private Tetriminos randomColor() {
        Random r = new Random();
        int color = r.nextInt(7)+1;
        for(int i = 0; i<content.length; i++) {
            for(int j = 0; j<content[0].length; j++) {
                if(content[i][j] != 0) {
                    content[i][j] = color;
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        String contenu = "";
        for (int[] is : content) {
            for (int i : is) {
                if(i != 0) {
                    contenu+="# ";
                } else {
                    contenu+="  ";
                }
            }
            contenu+="\n";
        }
        return contenu;
    }

    public boolean equal(Tetriminos t) {
        return t.getType().equals(this.type);
    }

}
