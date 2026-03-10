import java.io.*;
import java.awt.Color ;
import java.util.ArrayList;
class main{
public static void main(String [] args){
  ArrayList<Point> listepoints = new ArrayList<>();
  try (BufferedReader br = new BufferedReader(new FileReader("fich.txt"))) {
   //la taille de l'image
  double imageSize = Double.parseDouble(br.readLine().trim());
   Quadtree node = new Quadtree(0, 0, imageSize, imageSize, null,null);
        // le nombre de points
   int nbp = Integer.parseInt(br.readLine().trim());
        //  ajouter les points  à la liste
   for (int i = 0; i < nbp; i++) {
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
           System.out.println("Ligne vide ou nulle détectée !");
            continue;
        }
         String[] chaine = line.split(",");

         // Valider la ligne (il nous faut forcement x,y et les 4 couleurs).
         if (chaine.length != 6) {
            System.out.println("Format incorrect pour le point : " + line);
             continue;
         }

  // Prendre les coordonnées et les couleurs
          double x = Double.parseDouble(chaine[0].trim());
          double y = Double.parseDouble(chaine[1].trim());
          ArrayList<String> colors = new ArrayList<>();
          colors.add(chaine[2]); // Couleur NO
           colors.add(chaine[3]); // Couleur NE
           colors.add(chaine[4]); // Couleur SO
           colors.add(chaine[5]); // Couleur SE
           
             Point p = new Point(x, y, colors);
             listepoints.add(p);
     }

     // Construire le quadtree après la lecture des points
     node.buildQTree(listepoints); 

     // lecture de l'épaisseur des bordures
     String line = br.readLine();
     int epaisseur = Integer.parseInt(line.trim());
      //premiere image
       int n = (int) imageSize;
       Image image = new Image(n, n);
       node.toImage(image, "file1", n, epaisseur);   
      node.toText("file.txt");
      // Lire le nombre de recoloriages
      int nbRecoloriages = Integer.parseInt(br.readLine().trim());
      ArrayList<String> recolors = new ArrayList<>();

      // Lire les paires de recoloriages
       for (int i = 0; i < nbRecoloriages; i++) {
          String recolorLine = br.readLine();
          if (recolorLine == null || recolorLine.trim().isEmpty()) {
            System.out.println("Ligne de recoloriage vide détectée !");
            continue;
          }
          recolors.add(recolorLine);
        }

       // Appliquer les recoloriages
       for (String recolorLine : recolors) {
         String[] parts = recolorLine.split(",");
          if (parts.length != 3) {
           System.out.println("Format incorrect pour la paire de recoloriage : " + recolorLine);
            continue;
          }

      // les coordonnées et la couleur
           double x = Double.parseDouble(parts[0].trim());
           double y = Double.parseDouble(parts[1].trim());
            String newcolor = parts[2].trim().toUpperCase();

            // Appliquer le recoloriage
            Point p = new Point(x, y, null);
            node.recolor(p,newcolor);
            node.toText("filecolor.txt");
         }

        // Générer l'image finale
         node.toImage(image, "fileRecolor", n, epaisseur);
         //node.compressQTree();
         node.toImage(image,"fileCompress",n,epaisseur); 
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : " + e.getMessage());
        }
}
}

