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
├── src/
│   ├── Image.java          # Création et sauvegarde d'images PNG
│   ├── Point.java          # Coordonnées et gestion des couleurs
│   ├── Quadtree.java       # Structure de l'arbre et opérations principales
│   ├── Main.java           # Programme principal de démonstration
│   └── TestQuadtree.java   # Variante de test du programme principal
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
javac -d out src/*.java
```

Les deux points d'entrée ont maintenant des noms distincts (`Main` et `TestQuadtree`), ce qui évite le conflit de classe qui existait auparavant.

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
java -cp out Main
```

Le programme génère notamment :

- une première image issue du quadtree
- une représentation textuelle de l'arbre
- une image après recoloriage
- une image après compression

## Exemple de workflow

```bash
javac -d out src/*.java
java -cp out Main
```

La variante de test peut être lancée avec :

```bash
java -cp out TestQuadtree
```

Les fichiers `file1`, `fileRecolor` et `fileCompress` sont des images PNG générées par le programme. Ils peuvent être ouverts comme images même s'ils n'ont pas encore l'extension `.png`.

## Notes

Ce projet met surtout l'accent sur la compréhension de la structure quadtree et des opérations récursives associées. La partie Java est désormais regroupée dans `src/`; les fichiers Julia du dépôt restent des exercices annexes.

## Prochaines améliorations

- Renommer les images générées avec l'extension `.png`.
- Ajouter un fichier `fich.txt` d'exemple documenté.
- Ajouter une petite galerie de résultats dans le README.
- Ajouter des tests unitaires simples sur la division, la recherche et la compression.
