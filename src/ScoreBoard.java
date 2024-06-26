
import java.util.ArrayList;

public class ScoreBoard {
    public int length;
    private ArrayList<String> pseudo;
    private ArrayList<Integer> score;
    private ArrayList<Integer> level;

    public ScoreBoard() {
        this.length = 0;
        this.score = new ArrayList<>();
        this.pseudo = new ArrayList<>();
        this.level = new ArrayList<>();
    }

    public void addPlayer(int level, int score, String pseudo) {
        boolean pseudoExist = false;
        String add = "";
        if(pseudo.length()<10) {
            int b = 10-pseudo.length();
            for(int i = 0; i<b; i++) {
                add+=" ";
            }
        }
        pseudo+=add;
        for(int i = 0; i<this.length; i++) {
            pseudoExist = pseudo.equals(this.pseudo.get(i));
        }



        if(!pseudoExist) {
            this.pseudo.add(pseudo);
            this.level.add(level);
            this.score.add(score);
            this.length++;
        } else {
            modifyProfil(level, score, pseudo);
        }
    }

    public void modifyProfil(int level, int score, String pseudo) {
        int index = 0;
        for(int i = 0; i<this.length; i++) {
            if(pseudo.equals(this.pseudo.get(i))) {
                index = i;
            }
        }
        this.level.set(index, level);
        this.score.set(index, score);
    }

    public String getpseudo(int i) {
        return this.pseudo.get(i);
    }

    public int getscore(int i) {
        return this.score.get(i);
    }

    public int getlevel(int i) {
        return this.level.get(i);
    }

    public void setpseudo(int i, String pseudo) {
        this.pseudo.set(i, pseudo);
    }

    public void setscore(int i, int score) {
        this.score.set(i, score);
    }

    public void setlevel(int i, int level) {
        this.level.set(i, level);
    }

    @Override
    public String toString() {
        String txt = "";
        for(int i = 0; i<this.length; i++) {
            txt+=level.get(i) + " - " + pseudo.get(i) + " " + score.get(i) + " \n";
        }
        return txt;
    }

    public void sort() {
        for (int i = 0; i < this.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < this.length; j++) {
                if(this.getlevel(j) > this.getlevel(index)) {
                    index = j;
                } else if(this.getlevel(j) == this.getlevel(index)) {
                    if(this.getscore(j) > this.getscore(index)) {index = j;}
                }
            }

            int minlevel = this.getlevel(index);
            int minscore = this.getscore(index);
            String minpseudo = this.getpseudo(index);

            this.setlevel(index, this.getlevel(i));
            this.setscore(index, this.getscore(i));
            this.setpseudo(index, this.getpseudo(i));

            this.setlevel(i, minlevel);
            this.setscore(i, minscore);
            this.setpseudo(i, minpseudo);
        }
    }
}

