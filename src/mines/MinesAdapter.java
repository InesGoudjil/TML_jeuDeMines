package mines;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesAdapter extends MouseAdapter {

    // La classe MinesAdapter étend la classe MouseAdapter pour gérer les événements
    // de souris
    // pour le jeu de démineur.

    private Board board;
    // La classe MinesAdapter utilise une instance de la classe Board pour accéder
    // aux propriétés et méthodes
    // du jeu de démineur.

    public MinesAdapter(Board board) {
        this.board = board;
        // Le constructeur prend une instance de la classe Board et l'initialise en tant
        // que variable de classe.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // La méthode mousePressed() est appelée chaque fois que l'utilisateur clique
        // sur la souris.

        int x = e.getX();
        int y = e.getY();

        int cCol = x / board.CELL_SIZE;
        int cRow = y / board.CELL_SIZE;
        // Les variables cCol et cRow représentent la colonne et la ligne correspondant
        // à l'endroit où
        // l'utilisateur a cliqué sur la grille de démineur.

        boolean rep = false;
        // La variable "rep" est utilisée pour savoir si le jeu doit être redessiné
        // après un clic.

        if (!board.inGame) {
            board.newGame();
            board.repaint();
        }
        // Si le jeu n'est pas encore commencé, un nouveau jeu est créé et le plateau
        // est redessiné.

        if ((x < board.cols * board.CELL_SIZE) && (y < board.rows * board.CELL_SIZE)) {
            // Si le clic est à l'intérieur des limites de la grille de démineur, la suite
            // est exécutée.

            if (e.getButton() == MouseEvent.BUTTON3) {
                // Si le clic est le clic droit de la souris :

                if (board.field[(cRow * board.cols) + cCol] > board.MINE_CELL) {
                    // Si la cellule cliquée est une cellule non marquée :

                    rep = true;

                    if (board.field[(cRow * board.cols) + cCol] <= board.COVERED_MINE_CELL) {
                        if (board.minesLeft > 0) {
                            board.field[(cRow * board.cols) + cCol] += board.MARK_FOR_CELL;
                            board.minesLeft--;
                            board.statusBar.setText(Integer.toString(board.minesLeft));
                        } else
                            board.statusBar.setText("No marks left");
                    } else {

                        board.field[(cRow * board.cols) + cCol] -= board.MARK_FOR_CELL;
                        board.minesLeft++;
                        board.statusBar.setText(Integer.toString(board.minesLeft));
                    }
                    // Si la cellule est non marquée, la marquer. Si elle est marquée, la démarquer.

                }

            } else {
                // Si le clic est le clic gauche de la souris

                if (board.field[(cRow * board.cols) + cCol] > board.COVERED_MINE_CELL) {
                    return;
                }
                // Si la cellule cliquée est une cellule marquée, ne rien faire.

                if ((board.field[(cRow * board.cols) + cCol] > board.MINE_CELL) &&
                        (board.field[(cRow * board.cols) + cCol] < board.MARKED_MINE_CELL)) {

                    board.field[(cRow * board.cols) + cCol] -= board.COVER_FOR_CELL;
                    rep = true;

                    if (board.field[(cRow * board.cols) + cCol] == board.MINE_CELL)
                        board.inGame = false;
                    if (board.field[(cRow * board.cols) + cCol] == board.EMPTY_CELL)
                        board.findEmptyCells((cRow * board.cols) + cCol);
                }
            }

            if (rep)
                board.repaint();

        }
    }
}
