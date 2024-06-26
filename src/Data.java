public class Data {

    private String pseudo;
    private int level;
    private int score;
    private int[] tetriminos = new int[7];
    private int[][] grid = new int[20][10];
    private int x;
    private int y;
    private String next;
    private String actual;
    private boolean classique;

    public Data(String pseudo, int level, int score, int[] tetriminos, int[][] grid, String actual, String next, int x, int y, boolean classique) {
        this.pseudo = pseudo;
        this.level = level;
        this.score = score;
        this.tetriminos = tetriminos;
        for(int i = 0; i<20; i++) {
            for(int j = 0; j<10; j++) {
                this.grid[i][j] = grid[i][j];
            }
        }
        this.x = x;
        this.y = y;
        this.next = next;
        this.actual = actual;
        this.classique = classique;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int[] getTetriminos() {
        return tetriminos;
    }

    public int[][] getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        String txt = "";
        txt+="Pseudo: " + pseudo+"\n";
        txt+="Level: " + level+"\n";
        txt+="Score: " + score+"\n";
        txt+="Tetriminos: I:"+tetriminos[0]+ " J:"+tetriminos[1]+ " L:"+tetriminos[2]+ " O:"+tetriminos[3]+ " T:"+tetriminos[4]+ " S:"+tetriminos[5]+ " Z:"+tetriminos[6]+ "\n";
        for (int[] is : grid) {
            for (int i : is) {
                if(i != 0) {
                    txt+="# ";
                } else {
                    txt+="  ";
                }
            }
            txt+="\n";
        }
        txt+="\n";
        return txt;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNext() {
        return next;
    }

    public String getActual() {
        return actual;
    }

    public boolean getClassique() {
        return this.classique;
    }
}
