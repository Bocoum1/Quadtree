import java.io.*;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Scanner;

public class Quadtree{ 
    private Quadtree parent;
    private double xmin,ymin, xmax, ymax; String color;
    private Quadtree nordouest;
    private Quadtree nordest;
    private Quadtree sudouest;
    private Quadtree sudest;
    private boolean divisible;

    public Quadtree(double xmin,double ymin, double xmax,double ymax, String color,Quadtree parent){
        this.xmin=xmin;
        this.ymin=ymin;
        this.xmax =xmax;
        this.ymax=ymax;
        this.color=color;
        
        this.parent=parent;
        this.nordouest=null;
        this.nordest=null;
        this.sudouest=null;
        this.sudest=null;
        this.divisible=true;
    }
    
    /**Methode permettant de diviser une region 
    * @param p
    * Complexité O(1)
    * La méthode effectue quelques vérifications et crée quatre nouveaux nœuds. 
    * Les opérations sont constantes, indépendamment de la taille du Quadtree.
    */
    public void divide(Point p) {
        if (!divisible()) {
            //System.out.println("Cette région n'est plus divisible.");
            // Si le nœud est une feuille ou null, on ne fait rien
            return;
        }

        ArrayList<String> colors = p.getcolors();
        if (colors == null || colors.size() != 4) {
            System.out.println("Erreur : Le point doit fournir exactement 4 couleurs.");
            return;
        }

        double px = p.getX();
        double py = p.getY();

        // Vérification : Le point pivot doit être à l'intérieur des limites
        if (px <= xmin || px >= xmax || py <= ymin || py >= ymax) {
            System.out.println("Erreur : Le point est en dehors des limites de la région.");
            return;
        }
        this.nordouest = new Quadtree(xmin, py, px, ymax,colors.get(0),this); // Nord-Ouest
        this.nordest = new Quadtree(px, py, xmax, ymax,colors.get(1),this);   // Nord-Est
        this.sudouest = new Quadtree(xmin, ymin, px, py,colors.get(3),this); // Sud-Ouest
        this.sudest = new Quadtree(px, ymin, xmax, py,colors.get(2),this);   // Sud-Est

        // Marquer cette région comme non divisible

        this.divisible = false;

    }

    /**cherche dans le quadtree la région divisible R à laquelle P appartient 
     * @param Point 
     * Complexité O(logN), 
     *              N= nbr de nœuds.
     */
  public Quadtree searchQTree(Point p) {
        if (this==null || divisible()) {
            return this;
        }
        else if (!isLeaf() && p.getX() >= this.xmin && p.getX() <= this.xmax && p.getX()>= this.ymin && p.getY()<=this.ymax){
            System.out.println("p");
        }

        // Vérifier les quadrants
        if (p.getX() < nordouest.Xmax() && p.getY() >= nordouest.Ymin()) {
            return nordouest.searchQTree(p);
        }else if (p.getX() >= nordest.Xmin() && p.getY() >= nordest.Ymin()) {
            return nordest.searchQTree(p);
        }else if (p.getX() < sudouest.Xmax() && p.getY() < sudouest.Ymax()) {
            return sudouest.searchQTree(p);
        }else  {
            return sudest.searchQTree(p);
        }
    }
    
    /** Ajoute un point au quadtree
     * @param Point
     * Complexité O(logN)
     * Cette méthode appelle searchQTree,
     * qui est O(logN), 
     * suivie d'une opération divide qui est O(1).
     */
    public void addQTree(Point point){ 
        searchQTree(point).divide(point);// Rechercher la région contenant le point puis Diviser cette région
    }

    /** 
     * Construit le quadtree entier en utilisant les fonctions précédentes.
     * @param ArrayList<Point>
     * Complexité O(MlogN), 
     *              M = nbr de points 
     *              N = le nbr de nœuds du Quadtree.
    */
    public void buildQTree(ArrayList<Point> points){
        for(Point point: points){
        addQTree(point);// Ajouter chaque point dans le quadtree à partir de la racine
        }
    }

    /** 
     * Méthode toImage pour dessiner l'image du quadtree
     * @param img, filePath, imagesize, eps
     * Complexité  O(N),
     *              N est le nombre de nœuds du Quadtree.
     * 
     */
    public void toImage(Image img,String filePath, int imageSize,int eps) {
        int epsr = (eps-1)/2;
        if (isLeaf()) {
        // Rotation de ymax et ymin pour avoir l'image comme voulu
        int rotmin = imageSize- (int)ymax;
        int rotmax = imageSize- (int)ymin;
            Color nodeColor = Point.couleur(this.color);
            img.setRectangle((int) xmin, (int) (xmax), rotmin, rotmax, nodeColor);
            
            Color border= Color.BLACK;
            img.setRectangle((int) xmin, (int) (xmax), rotmin, rotmin+epsr,border);//Bordure superieur
            img.setRectangle((int) xmin, (int) (xmax), rotmax-epsr,rotmax,border);//Bordure inferieur
            img.setRectangle((int) xmin, (int) (xmin)+eps,rotmin, rotmax,border);//Bordure gauche
            img.setRectangle((int) xmax-epsr, (int) (xmax),rotmin, rotmax,border);//Bordure droite
            } else {
                if (nordouest != null) nordouest.toImage(img, filePath, imageSize,eps);        
                if (nordest != null) nordest.toImage(img, filePath, imageSize,eps);  
                if (sudouest != null) sudouest.toImage(img, filePath, imageSize,eps);      
                if (sudest != null) sudest.toImage(img, filePath, imageSize,eps);
            }
            try { 
            img.save(filePath);
            } catch(IOException e){ System.err.println( e.getMessage());
            }
    }

    /**
     * modifie le quadtree pour donner à la région divisible contenant le point la couleur indiquée.
     * @param Point, couleur 
     * Complexité O(logN + K),
     *                  K est le nombre de nœuds fusionnés lors de la compression.
     */
    public void recolor(Point p, String newcolor){
        // Recherche du nœud correspondant à la région contenant le point
        Quadtree qdt = searchQTree(p) ;
        // Recolorie la région
        if(qdt != null){
            qdt.setcolor(newcolor);
            // Lance la compression du quadtree pour fusionner les sous-régions si possible
            compressQTree();
        }
    }

    /**
     * lors du changement de couleur d’une région R en utilisant la fonction précédente,
     * détecte si les quatre fils du père P de R dans le quadtree représentent tous des régions divisibles de la
     * même couleur, que l’on note C. Dans ce cas, la 4-division enregistrée dans P est inutile, et doit être
     * annulée. Pour cela, les quatre fils de P sont effacés de l’arbre et P devient une feuille de couleur C.
     * Complexité:
     *      O(1) meilleur des cas, si aucune fusion n'est possible.
     *      O(N) au pire des cas 
     * 
     */
    public void compressQTree() {
        // Si le nœud est une feuille, on ne fait rien
        if (isLeaf()) {
            return;
        }
        // Vérifier et compresser récursivement les sous-nœuds
        if (nordouest != null) nordouest.compressQTree();
        if (nordest != null) nordest.compressQTree();
        if (sudouest != null) sudouest.compressQTree();
        if (sudest != null) sudest.compressQTree();

        // Vérifier si tous les sous-nœuds sont des feuilles
        if (nordouest != null && nordest != null && sudouest != null && sudest != null &&
            nordouest.isLeaf() && nordest.isLeaf() &&
            sudouest.isLeaf() && sudest.isLeaf()) {

            // Vérifier si toutes les couleurs sont identiques
            String commonColor = nordouest.color;
            if (commonColor.equals(nordest.color) &&
                commonColor.equals(sudouest.color) &&
                commonColor.equals(sudest.color)) {

                // Fusionner les nœuds en une seule feuille
                this.color = commonColor;
                this.nordouest = null;
                this.nordest = null;
                this.sudouest = null;
                this.sudest = null;
            }
        }
    }


    /**
     * 
     * Complexité O(N)
     */
    public void toText(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            if (divisible()) {
                writer.write(color);
            } else { //  la racine est divisible, commencer le parcours
                writeText(writer);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier : " + e.getMessage());
        }
    }

    // Partie privée de la méthode pour gérer récursivement le quadtree
    public void writeText(BufferedWriter writer) throws IOException {
        if (isLeaf()) {
            // Écrire la couleur du nœud si c'est une feuille
            writer.write(color);
        } else {
            // Écrire une parenthèse ouvrante pour le nœud interne
            writer.write("(");

            // Appeler récursivement les sous-nœuds, si existants
            if (nordouest != null) nordouest.writeText(writer);
            if (nordest != null) nordest.writeText(writer);
            if (sudest != null) sudest.writeText(writer);
            if (sudouest != null) sudouest.writeText(writer);
            
            // Écrire une parenthèse fermante pour le nœud interne
            writer.write(")");
        }
    }

     public Quadtree parent(){
        return parent;
    }
    public double Xmin(){ 
        return xmin;
    }
    public double Ymin(){ 
        return ymin;
    }

    public double Xmax(){ 
        return xmax;
    }
    public double Ymax(){ 
        return ymax;
    }
    public String color(){ 
        return color;
    }
    public void setcolor(String color){ 
        this.color=color;
    }

    public boolean divisible(){ 
        return divisible;
    }
    public Quadtree nordouest(){ 
        return nordouest;
    }
    public Quadtree nordest(){ 
        return nordest;
    }
    public Quadtree sudouest(){ 
        return sudouest;
    }
    public Quadtree sudest(){ 
        return sudest;
    }

    public boolean isLeaf() {
        return nordouest == null && nordest == null && sudouest == null && sudest == null;
    }



}
