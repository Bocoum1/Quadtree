import java.util.ArrayList;
import java.awt.Color ;

public class Point{
    private double x,y;
    private ArrayList<String> colors;
   
    public Point(double x, double y,ArrayList<String> colors){
      this.x=x;
      this.y=y;
      if(colors==null){
        this.colors=null;
      }
      else{
        this.colors=new ArrayList<String>(colors);
      }
      
    }
      
    public double getX(){ return x;}
    public double getY(){ return y;}
    public void remplir(String couleur){ colors.add(couleur);}
    public ArrayList<String> getcolors(){ return colors;}
  
  //la en méthode static afin qu'elle puisse être utilisée sans avoir à instancier un objet Point
  public static Color couleur(String color) {
      switch (color) {
        case "R": return Color.RED;     // Rouge
        case "G": return Color.GRAY;   // Vert
        case "B": return Color.BLUE;    // Bleu
        case "J": return Color.YELLOW;  // Jaune
        case "N": return Color.BLACK;   // Noir
        case "W": return Color.WHITE;   // Blanc
        default: return Color.BLACK;    // Noir par défaut
    }
  } 

}
