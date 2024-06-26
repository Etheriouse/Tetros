import java.util.TreeMap;
import java.util.ArrayList;

public class function {

    /**
     * Rotation du tableau vers la gauche
     * @return un tableau
     */
    public static int[][] rotationArrayToLeft(int t[][]) {
        return rotationArray(3, t);
    }

    /**
     * Rotation du tableau vers la droite
     * @return un tableau
     */
    public static int[][] rotationArrayToRight(int t[][]) {
        return rotationArray(1, t);
    }

    /**
     * Rotation du tableau vers la droite number fois
     * @param number le nombre de rotation
     * @return un tableau
     */
    private static int[][] rotationArray(int number, int x[][]) {
        if(number <= 1) {
            return rotationArray(x);
        } else {
            return rotationArray(number-1, rotationArray(x));
        }
    }

    /**
     * Rotation du tableau vers a la droite
     * @return le tableau
     */
    private static int[][] rotationArray(int t[][]) {
        int newTab[][] = new int[t[0].length][t.length];
        for(int i = 0; i <newTab.length; i++) {
            for(int y = 0; y<newTab[0].length; y++) {
                newTab[i][y] = t[newTab[0].length-y-1][i];
            }
        }
        return newTab;
    }

    public static void writeData(Data[] d) {
        String toWrite = "";
        for (Data data : d) {
            toWrite+=data.getPseudo()+" "+data.getLevel()+" "+data.getScore()+" ";
            for (int tetri : data.getTetriminos()) {
                toWrite+=tetri+" ";
            }
            for (int[] gridrow : data.getGrid()) {
                String s = "";
                for (int gridElement : gridrow) {
                    s+=gridElement;
                }
                toWrite+=s+" ";
            }
            toWrite+=data.getX()+" "+data.getY()+" "+data.getActual()+" "+data.getNext()+" ";
            if(data.getClassique()) {
                toWrite+="1";
            } else {
                toWrite+="0";
            }
            toWrite+="\n";
        }
        Files.write(toWrite, "Saves.txt");
    }

    public static Data[] exportData() {
        String file[] = Files.Arrayread("Saves.txt");
        Data data[] = new Data[file.length];

        for(int i = 0; i<file.length; i++) {
            String datas[] = file[i].split(" ");
            int grid[][] = new int[20][10];
            for(int j = 10; j<30; j++) {
                String s = datas[j];
                for(int y = 0; y<s.length(); y++) {
                    grid[j-10][y] = Integer.parseInt(String.valueOf(s.charAt(y)));
                }
            }
            data[i] = new Data(
                datas[0], // pseudo
                Integer.parseInt(datas[1]), // level
                Integer.parseInt(datas[2]), // score
                new int[]{
                    Integer.parseInt(datas[3]), // nb type tetriminos
                    Integer.parseInt(datas[4]),// nb type tetriminos
                    Integer.parseInt(datas[5]),// nb type tetriminos
                    Integer.parseInt(datas[6]),// nb type tetriminos
                    Integer.parseInt(datas[7]),// nb type tetriminos
                    Integer.parseInt(datas[8]),// nb type tetriminos
                    Integer.parseInt(datas[9]),// nb type tetriminos
                }, grid, //grille
                datas[32], // actual
                datas[33], // next
                Integer.parseInt(datas[30]), // y
                Integer.parseInt(datas[31]), // x
                Integer.parseInt(datas[34])%2!=0 // classique = vrai 1 infine = faux 0
                );
        }
        return cleardatas(data);
    }

    private static Data[] cleardatas(Data saves[]) {
        int lenght = saves.length;
        for (Data data : saves) {
            if(data == null) {
                lenght--;
            }
        }
        if(lenght != saves.length) {
            Data newSaves[] = new Data[lenght];
            for (int i = 0, a = 0; i<saves.length; i++) {
                if(saves[i] != null) {
                    newSaves[a] = saves[i];
                    a++;
                }
            }
            return newSaves;
        } else {
            return saves;
        }
    }

    public static ScoreBoard exportScoreboard(boolean classique) {
        String file[] = Files.Arrayread("ScoreBoard.txt");
        if(!classique) {
            file = Files.Arrayread("ScoreBoardI.txt");
        }
        ScoreBoard scoreboard = new ScoreBoard();

        for (String string : file) {
            String datas[] = string.split(";");
            scoreboard.addPlayer(Integer.parseInt(datas[0]), Integer.parseInt(datas[2]), datas[1]);
        }
        sortScoreboard(scoreboard);
        return scoreboard;
    }

    public static void writeScoreboard(ScoreBoard sb, boolean classique) {
        String toWrite = "";
        for(int i = 0; i<sb.length; i++) {
            toWrite+=sb.getlevel(i)+";"+sb.getpseudo(i)+";"+sb.getscore(i)+"\n";
        }
        if(classique) {
            Files.write(toWrite, "ScoreBoard.txt");
        } else {
            Files.write(toWrite, "ScoreBoardI.txt");
        }
    }

    private static void sortScoreboard(ScoreBoard s) {

        for (int i = 0; i < s.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < s.length; j++) {
                if(s.getlevel(j) > s.getlevel(index)) {
                    index = j;
                } else if(s.getlevel(j) == s.getlevel(index)) {
                    if(s.getscore(j) > s.getscore(index)) {index = j;}
                }
            }

            int minlevel = s.getlevel(index);
            int minscore = s.getscore(index);
            String minpseudo = s.getpseudo(index);

            s.setlevel(index, s.getlevel(i));
            s.setscore(index, s.getscore(i));
            s.setpseudo(index, s.getpseudo(i));

            s.setlevel(i, minlevel);
            s.setscore(i, minscore);
            s.setpseudo(i, minpseudo);
        }
    }

    public static void deleteSave(String pseudo) {
        Data[] saves = exportData();

        int index = 0;

        for(int i = 0; i<saves.length; i++) {
            if(saves[i].getPseudo().equals(pseudo)) {
                index = i;
            }
        }

        Data newSaves[] = new Data[saves.length-1];

        for(int i = 0; i<newSaves.length; i++) {
            if(i < index) {
                newSaves[i] = saves[i];
            } else {
                newSaves[i] = saves[i+1];
            }
        }
        writeData(newSaves);
    }

    public static TreeMap<String, ArrayList<String>> extractWord() {
        TreeMap<String, ArrayList<String>> mapWordLanguage = new TreeMap<>();
        String file[] = Files.Arrayread("words.txt");
        for (String string : file) {
            String content[] = string.split(",");
            mapWordLanguage.put(content[0], new ArrayList<>());
            for(int i = 1; i<content.length; i++) {
                mapWordLanguage.get(content[0]).add(content[i]);
            }
        }
        return mapWordLanguage;
    }
}
