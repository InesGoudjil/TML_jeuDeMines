package mines;

import java.awt.Graphics;
import java.awt.Image;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;

    public final int NUM_IMAGES = 13;
    // nombre total d'images différentes utilisées dans le jeu
    public final int CELL_SIZE = 15;
    // taille de chaque cellule
    public final int COVER_FOR_CELL = 10;
    // valeur ajoutée pour les cellules couvertes
    public final int MARK_FOR_CELL = 10;
    // valeur ajoutée pour les cellules marquées
    public final int EMPTY_CELL = 0;
    // identifiant pour une cellule vide
    public final int MINE_CELL = 9;
    // identifiant pour une cellule contenant une mine
    public final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    // identifiant pour une cellule contenant une mine et couverte
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    // identifiant pour une cellule contenant une mine marquée
    public final int DRAW_MINE = 9;
    // identifiant pour une image de mine
    public final int DRAW_COVER = 10;
    // identifiant pour une image de cellule couverte
    public final int DRAW_MARK = 11;
    // identifiant pour une image de cellule marquée
    public final int DRAW_WRONG_MARK = 12;
    // identifiant pour une image de marqueur de mine incorrect

    public int[] field;
    // tableau contenant les identifiants des cellules
    public boolean inGame;
    // booléen qui indique si le jeu est en cours
    public int minesLeft;
    // nombre de mines restantes à trouver
    public Image[] img;
    // tableau contenant les images utilisées pour les cellules et les mines
    public int mines = 40;
    // nombre total de mines dans le jeu
    public int rows = 16;
    // nombre de rangées sur le plateau
    public int cols = 16;
    // nombre de colonnes sur le plateau
    public int allCells;
    // nombre total de cellules sur le plateau
    public JLabel statusBar;
    // état actuel du jeu affiché à l'utilisateur

    public Board(JLabel statusBar) {

        // Définit la variable d'instance statusBar de l'objet Board sur le paramètre
        // passé dans le constructeur
        this.statusBar = statusBar;

        // Initialise le tableau d'images img avec une taille NUM_IMAGES
        img = new Image[NUM_IMAGES];

        // Boucle for pour charger les images dans le tableau img
        for (int i = 0; i < NUM_IMAGES; i++) {
            // Charge chaque image avec un chemin relatif et les ajoute dans le tableau img
            img[i] = (new ImageIcon(getClass().getClassLoader().getResource((i)
                    + ".gif"))).getImage();
        }
        // Active le double tampon pour améliorer les performances d'affichage
        setDoubleBuffered(true);

        // Ajoute un écouteur de souris à l'objet MinesAdapter et l'associe à l'objet
        // Board actuel

        addMouseListener(new MinesAdapter(this));
        // Démarre une nouvelle partie
        newGame();
    }

    public void newGame() {
        // On utilise un objet de type Random pour générer des nombres aléatoires
        Random random = new Random();
        int currentCol, i, position, cell;

        // On initialise les variables pour une nouvelle partie
        inGame = true; // le jeu est en cours
        minesLeft = mines; // nombre de mines restantes à découvrir est égal au nombre de mines
        allCells = rows * cols; // le nombre de cellules dans le champ de jeu
        field = new int[allCells]; // initialisation du tableau représentant le champ de jeu

        // On met toutes les cellules en mode couvert
        for (i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        // On affiche le nombre de mines restantes dans la barre d'état
        statusBar.setText(Integer.toString(minesLeft));

        // On place les mines aléatoirement
        i = 0;
        while (i < mines) {
            // On génère une position aléatoire pour la mine
            position = (int) (allCells * random.nextDouble());

            // On vérifie si la cellule ne contient pas déjà une mine
            if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) {
                currentCol = position % cols;
                field[position] = COVERED_MINE_CELL;
                i++;

                // On met à jour les cellules voisines pour indiquer le nombre de mines
                // adjacentes
                if (currentCol > 0) {
                    cell = position - 1 - cols;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }

                    cell = position - 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }

                    cell = position + cols - 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position - cols;
                if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                    field[cell] += 1;
                }

                cell = position + cols;
                if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                    field[cell] += 1;
                }

                if (currentCol < (cols - 1)) {
                    cell = position - cols + 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }

                    cell = position + cols + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }

                    cell = position + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }
            }
        }
    }

    public void findEmptyCells(int j) {

        // Détermine la colonne actuelle
        int currentCol = j % cols;
        int cell;

        // Si la cellule actuelle n'est pas sur la première colonne, vérifie les
        // cellules à gauche
        if (currentCol > 0) {
            // Cellule en haut à gauche
            cell = j - cols - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
            // Cellule à gauche
            cell = j - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
            // Cellule en bas à gauche
            cell = j + cols - 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
        }

        // Cellule en haut
        cell = j - cols;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

        // Cellule en bas
        cell = j + cols;
        if (cell < allCells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

        // Si la cellule actuelle n'est pas sur la dernière colonne, vérifie les
        // cellules à droite
        if (currentCol < (cols - 1)) {
            // Cellule en haut à droite
            cell = j - cols + 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
            // Cellule à droite
            cell = j + cols + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
            // Cellule en bas à droite
            cell = j + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
        }

    }

    public void paint(Graphics g) {

        int cell = 0;
        int uncover = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // Obtient la valeur de la cellule actuelle
                cell = field[(i * cols) + j];

                // Si la cellule est une mine et que le jeu est en cours, alors la partie est
                // perdue
                if (inGame && cell == MINE_CELL)
                    inGame = false;

                // Si la partie est terminée, affiche la bonne image pour chaque cellule
                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }
                } else {
                    // Si la partie est en cours, affiche la bonne image pour chaque cellule
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                // Dessine l'image de la cellule actuelle à l'emplacement correspondant sur le
                // plateau
                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        // Si toutes les cellules ont été découvertes et que la partie est en cours,
        // alors la partie est gagnée
        if (uncover == 0 && inGame) {
            inGame = false;
            statusBar.setText("Game won");
        } else if (!inGame)
            statusBar.setText("Game lost");
    }

}