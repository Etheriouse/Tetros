public class Level {

    public static int HEIGHT = 20;
    public static int WIDTH = 10;
    private int speed;
    private int[][] content = new int[HEIGHT][WIDTH];
    private Tetriminos pieceMoved;
    private Tetriminos NextTetriminos;
    public int x = -1;
    public int y = -1;
    private int nb_ms = 0;
    private int score = 0;
    private int difficulty = 1;
    private int[] nbTetris = new int[7];
    private boolean isDeleteLayer = false;

    private String pseudo = "";

    public Level() {
        this.difficulty = 1;
        this.speed = 1;
        this.pieceMoved = Tetriminos.getRandomTetriminos();
        this.NextTetriminos = Tetriminos.getRandomTetriminos();
        this.x = 4;
        this.y = 0;
    }

    public Level(Data d) {
        this.difficulty = d.getLevel();
        this.speed = (int) (0.9*Math.exp(((double) this.difficulty)/7.0));
        this.pieceMoved = new Tetriminos(d.getActual());
        this.NextTetriminos = new Tetriminos(d.getNext());
        this.x = d.getX();
        this.y = d.getY();
        this.nbTetris = d.getTetriminos();
        this.pseudo = d.getPseudo();
        this.score = d.getScore();
        int temp[][] = d.getGrid();
        for(int i = 0; i<HEIGHT; i++) {
            for(int j = 0; j<WIDTH; j++) {
                this.content[i][j] = temp[i][j];
            }
        }
    }

    public Level(int difficulty) {
        this.difficulty = difficulty;
        this.speed = (int) (0.9*Math.exp(((double) difficulty)/7.0));
        this.pieceMoved = Tetriminos.getRandomTetriminos();
        this.NextTetriminos = Tetriminos.getRandomTetriminos();
        this.x = 4;
        this.y = 0;
    }

    public void addTetriminos(Tetriminos t) {
        this.pieceMoved = t;
        this.x = 4;
        this.y = 0;
    }

    public Tetriminos getTetriminos() {
        return this.pieceMoved;
    }

    public void refresh(double delta) {
        isDeleteLayer = false;
        if(checkBottom()) {
            switch (pieceMoved.getType()) {
                case "I":
                    score+=(5*(difficulty));
                    nbTetris[0]+=1;
                    break;
                case "J":
                    score+=(3*(difficulty));
                    nbTetris[1]+=1;
                    break;
                case "L":
                    score+=(3*(difficulty));
                    nbTetris[2]+=1;
                    break;
                case "O":
                    score+=(1*(difficulty));
                    nbTetris[3]+=1;
                    break;
                case "T":
                    score+=(4*(difficulty));
                    nbTetris[4]+=1;
                    break;
                case "S":
                    score+=(2*(difficulty));
                    nbTetris[5]+=1;
                    break;
                case "Z":
                    score+=(2*(difficulty));
                    nbTetris[6]+=1;
                    break;
                default:
                    break;
            }
            freezePiece();
        }
        deleteLayers();
        nb_ms+=delta;
        if(nb_ms>1000/speed) {
            this.y++;
            nb_ms=0;
        }
    }

    private int[][] mergeElement() {
        int piece[][] = new int[1][1];
        if(this.pieceMoved != null) {
            piece = pieceMoved.getGrid();
        }
        int result[][] = new int[HEIGHT][WIDTH];
        //System.out.println(x);
        //System.out.println(y);
        //System.out.println(pieceMoved);

        for(int a = 0; a<content.length; a++) {

            for(int b = 0; b<content[0].length; b++) {

                if(x<=b && y<=a && (x+piece[0].length)>b && (y+piece.length)>a) {
                    //System.out.print(piece[a-y][b-x]);
                    if(piece[a-y][b-x] != 0) {
                        result[a][b] = piece[a-y][b-x];
                    } else {
                        result[a][b] = content[a][b];
                    }
                } else {
                    //System.out.print(content[a][b]);
                    result[a][b] = content[a][b];
                }
            }
//            System.out.println();
        }
  //      System.out.println();
        return result;
    }


    public void freezePiece() {

        if(this.pieceMoved != null) {

            for(int i = 0; i<pieceMoved.getGrid().length; i++) {
                for(int j = 0; j<pieceMoved.getGrid()[i].length; j++) {
                    if(pieceMoved.getGrid()[i][j] != 0) {
                        this.content[y+i][x+j] = pieceMoved.getGrid()[i][j];
                    }
                }
            }

            this.pieceMoved = NextTetriminos;
            this.NextTetriminos = Tetriminos.getRandomTetriminos();
            x = 4;
            y = 0;

        }

    }

    public boolean gameOver() {
        for(int i = 0; i<content[0].length; i++) {
            if(content[0][i] != 0) {
                return true;
            }
        }
        return false;
    }

    public void levelUp() {
        this.difficulty++;
        this.content = new int[HEIGHT][WIDTH];
        this.pieceMoved = Tetriminos.getRandomTetriminos();
        this.NextTetriminos = Tetriminos.getRandomTetriminos();
        this.score = 0;
        this.x = 4;
        this.y = 0;
        this.speed = (int) (0.9*Math.exp(((double) this.difficulty)/7.0));
    }

    public void checkPlacement() {
        if(pieceMoved != null) {
            if(x+pieceMoved.getGrid()[0].length > WIDTH) {
                this.x-=(Math.abs(x+pieceMoved.getGrid()[0].length-WIDTH));
            }
            if(y+pieceMoved.getGrid().length > HEIGHT) {
                this.y-=(Math.abs(y+pieceMoved.getGrid().length-HEIGHT));
            }
        }
    }

    private boolean SameLine(int i) {
        for(int j = 0; j<WIDTH; j++) {
            if(content[i][j] == 0) {
                return false;
            }
        }
        return true;
    }

    private void moveBottom(int n) {
        for(int i = HEIGHT-1; i>0; i--) {
            for(int j = 0; j<WIDTH; j++) {
                if(i<n) {
                    content[i+1][j] = content[i][j];
                }
            }
        }
    }

    private void deleteLayers() {
        for(int i = 0; i<HEIGHT; i++) {
            if(SameLine(i)) {
                score+=(10*(difficulty));
                content[i] = new int[WIDTH];
                moveBottom(i);
                isDeleteLayer = true;
            }
        }
    }



    public boolean checkBottom() {
        if(pieceMoved != null) {
            int t[][] = pieceMoved.getGrid();
            int goodIndex = t.length;
            if(y+goodIndex >= HEIGHT) {
                return true;
            } else {
                for(int j = 0; j<t[0].length; j+=1) {
                    goodIndex = t.length;
                    for(int i = t.length-1; t[i][j] == 0; i--) {
                        goodIndex = i;
                    }
                    if(content[y+goodIndex][x+j] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkCollisionLeft() {
        for(int i = 0; i<pieceMoved.getGrid().length; i++) {
                if(pieceMoved.getGrid()[i][0] != 0) {
                    if(x == 0 || content[y+i][x-1] != 0) {
                        return false;
                    }
                }
        }
        return true;
    }

    public boolean checkCollisionRight() {
        for(int i = 0; i<pieceMoved.getGrid().length; i++) {
                if(pieceMoved.getGrid()[i][pieceMoved.getGrid()[0].length-1] != 0) {
                    if(x+pieceMoved.getGrid()[0].length == WIDTH || content[y+i][x+pieceMoved.getGrid()[0].length] != 0) {
                            return false;
                    }
                }
        }
        return true;
    }

    public int[][] getGrid() {
        return mergeElement();
    }

    public int[][] getOriginalGrid() {
        return this.content;
    }

    public Tetriminos getNextTetriminos() {
        return this.NextTetriminos;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getScore() {
        return score;
    }

    public int getnbTetris() {
        int sum = 0;
        for (int is : nbTetris) {
            sum+=is;
        }
        return sum;
    }

    public int[] getStatPiece() {
        return this.nbTetris;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int n) {
        this.difficulty = n;
    }

    public boolean isSoundDeleteLayer() {
        return this.isDeleteLayer;
    }

    public void setPseudo(String s) {
        this.pseudo = s;
    }

    public String getPseudo() {
        return this.pseudo;
    }
}
