# Quadtree

Implémentation Java d'un quadtree pour représenter, découper, recolorer et compresser une image sous forme de régions colorées.

Le projet manipule une image comme une partition récursive de rectangles. Chaque division coupe une région en quatre sous-régions, chacune associée à une couleur. Le quadtree peut ensuite être exporté en image PNG ou sérialisé sous forme textuelle.

## Fonctionnalités

- Construire un quadtree à partir d'une liste de points de division.
- Rechercher la région contenant un point.
- Diviser une région en quatre quadrants.
- Recolorer une région.
- Compresser l'arbre lorsque quatre sous-régions voisines ont la même couleur.
- Exporter le résultat en image PNG.
- Exporter la structure du quadtree sous forme textuelle.

## Structure principale

```text
.
├── Image.java       # Création et sauvegarde d'images PNG
├── Point.java       # Coordonnées et gestion des couleurs
├── Quadtree.java    # Structure de l'arbre et opérations principales
├── main.java        # Programme principal de démonstration
├── test.java        # Variante de test du programme principal
├── file1            # Image générée initiale
├── fileRecolor      # Image générée après recoloriage
├── fileCompress     # Image générée après compression
└── filecolor.txt    # Représentation textuelle générée
```

Le dépôt contient aussi des fichiers Julia (`*.jl`) correspondant à des exercices annexes. Le cœur du projet Quadtree est la partie Java.

## Modèle de données

Un nœud du quadtree représente une région rectangulaire :

- limites : `xmin`, `ymin`, `xmax`, `ymax`
- couleur de la région
- référence vers le parent
- quatre enfants : nord-ouest, nord-est, sud-ouest, sud-est

Une feuille représente une région uniforme. Un nœud interne représente une région divisée en quatre sous-régions.

## Couleurs supportées

Les couleurs sont représentées par des lettres :

- `R` : rouge
- `G` : gris
- `B` : bleu
- `J` : jaune
- `N` : noir
- `W` : blanc

## Compilation

Compiler le cœur Java :

```bash
javac Image.java Point.java Quadtree.java main.java
```

Ne pas lancer directement `javac *.java` pour l'instant : `main.java` et `test.java` définissent tous les deux une classe `main`, ce qui crée un conflit de compilation.

## Exécution

Le programme principal lit un fichier d'entrée appelé `fich.txt`.

Format attendu :

```text
taille_image
nombre_points
x,y,couleur_NO,couleur_NE,couleur_SO,couleur_SE
...
epaisseur_bordure
nombre_recoloriages
x,y,nouvelle_couleur
...
```

Puis lancer :

```bash
java main
```

Le programme génère notamment :

- une première image issue du quadtree
- une représentation textuelle de l'arbre
- une image après recoloriage
- une image après compression

## Exemple de workflow

```bash
javac Image.java Point.java Quadtree.java main.java
java main
```

Les fichiers `file1`, `fileRecolor` et `fileCompress` sont des images PNG générées par le programme. Ils peuvent être ouverts comme images même s'ils n'ont pas encore l'extension `.png`.

## Notes

Ce projet met surtout l'accent sur la compréhension de la structure quadtree et des opérations récursives associées. Une prochaine passe pourrait séparer plus clairement les fichiers Java, les fichiers de démonstration et les exercices Julia annexes.

## Prochaines améliorations

- Renommer les images générées avec l'extension `.png`.
- Ajouter un fichier `fich.txt` d'exemple documenté.
- Séparer `main.java` et `test.java` pour éviter le conflit de classe `main`.
- Ajouter une petite galerie de résultats dans le README.
- Organiser le code Java dans un dossier `src/`.
