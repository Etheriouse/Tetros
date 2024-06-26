import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Files {

    /**
     * Lis le contenu d'un fichier
     *
     * @param file le chemin du dit fichier
     * @return Une liste chainer de toute les ligne de se fichier
     */
    public static LinkedList<String> Linkedread(String file) {
        LinkedList<String> fileContent = new LinkedList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return fileContent;
    }

    /**
     * Lis le contenu d'un fichier
     *
     * @param file le chemin du dit fichier
     * @return Une string se fichier
     */
    public static String[] Arrayread(String file) {
        LinkedList<String> linked_file = Linkedread(file);
        String file_contenu[] = new String[linked_file.size()];
        for(int i = 0; i<file_contenu.length; i++) {
            file_contenu[i] = linked_file.get(i);
        }
        return file_contenu;
    }

    /**
     * Lis le contenu d'un fichier
     *
     * @param file le chemin du dit fichier
     * @return Une string se fichier
     */
    public static String read(String file) {
        String fileContent = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                fileContent+=("\n"+line);
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return fileContent;
    }

    /**
     * Ecris dans un fichier
     *
     * @param o    L'objet a ecrir dans le fichier mis en string par sont toString()
     * @param file le chemin vers le dit fichier
     * @return Si il y a eu une erreur durant le processus
     *
     * @note Le contenu deja present dans le fichier avant l'ecriture sera
     *       entierement effacer
     */
    public static boolean write(Object o, String file) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(o.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
}