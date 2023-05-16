package mines;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

// Source: http://zetcode.com/tutorials/javagamestutorial/minesweeper/

public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;

    // Les dimensions de la fenêtre
    private final int WIDTH = 250;
    private final int HEIGHT = 290;

    private JLabel statusBar;

    // Le constructeur de la classe qui initialise la fenêtre, le titre, le statut
    // et le panneau de jeu.
    public Mines() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Ferme le programme lorsque la fenêtre est fermée
        setSize(WIDTH, HEIGHT);
        // Définit les dimensions de la fenêtre
        setLocationRelativeTo(null);
        // Centre la fenêtre sur l'écran
        setTitle("Minesweeper");
        // Définit le titre de la fenêtre
        statusBar = new JLabel("");
        // Initialise le statut
        add(statusBar, BorderLayout.SOUTH);
        // Ajoute le statut en bas de la fenêtre
        add(new Board(statusBar));
        // Ajoute le panneau de jeu
        setResizable(false);
        // Empêche l'utilisateur de redimensionner la fenêtre
        setVisible(true);
        // Affiche la fenêtre
    }

    // La méthode principale qui crée une instance de la classe Mines pour lancer le
    // jeu.
    public static void main(String[] args) {
        new Mines();
    }

}