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
    public final int CELL_SIZE = 15;
    public final int COVER_FOR_CELL = 10;
    public final int MARK_FOR_CELL = 10;    
    public final int EMPTY_CELL = 0;
    public final int MINE_CELL = 9;
    public final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    public final int DRAW_MINE = 9;
    public final int DRAW_COVER = 10;
    public final int DRAW_MARK = 11;
    public final int DRAW_WRONG_MARK = 12;

    public int[] field;
    public boolean inGame;
    public  int mines_left;
    public Image[] img;
    public int mines = 40;
    public int rows = 16;
    public int cols = 16;
    public int all_cells;
    public JLabel statusbar;

    public Board(JLabel statusbar) {

        this.statusbar = statusbar;
		
        img = new Image[NUM_IMAGES];
		
        for (int i = 0; i < NUM_IMAGES; i++) {
			img[i] =
                    (new ImageIcon(getClass().getClassLoader().getResource((i)
            			    + ".gif"))).getImage();
        }
        setDoubleBuffered(true);
        addMouseListener(new MinesAdapter(this));
        newGame();
    }

    public void newGame() {
        Random random = new Random();
        int currentCol, i = 0, position = 0, cell = 0;
    
        inGame = true;
        mines_left = mines;
        all_cells = rows * cols;
        field = new int[all_cells];
    
        // initialize the cells
        for (i = 0; i < all_cells; i++) {
            field[i] = COVER_FOR_CELL;
        }
    
        // update the status bar
        statusbar.setText(Integer.toString(mines_left));
    
        // randomly place the mines
        i = 0;
        while (i < mines) {
            position = (int) (all_cells * random.nextDouble());
    
            if ((position < all_cells) && (field[position] != COVERED_MINE_CELL)) {
                currentCol = position % cols;
                field[position] = COVERED_MINE_CELL;
                i++;
    
                // update the cells surrounding the mine
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
                    if (cell < all_cells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }
    
                cell = position - cols;
                if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                    field[cell] += 1;
                }
    
                cell = position + cols;
                if (cell < all_cells && field[cell] != COVERED_MINE_CELL) {
                    field[cell] += 1;
                }
    
                if (currentCol < (cols - 1)) {
                    cell = position - cols + 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
    
                    cell = position + cols + 1;
                    if (cell < all_cells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
    
                    cell = position + 1;
                    if (cell < all_cells && field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }
            }
        }
    }
    

    public void find_empty_cells(int j) {

        int current_col = j % cols;
        int cell;

        if (current_col > 0) { 
            cell = j - cols - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
            cell = j - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
            cell = j + cols - 1;
            if (cell < all_cells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
        }
        cell = j - cols;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }
        cell = j + cols;
        if (cell < all_cells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }
        if (current_col < (cols - 1)) {
            cell = j - cols + 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
            cell = j + cols + 1;
            if (cell < all_cells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
            cell = j + 1;
            if (cell < all_cells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
        }
    }

    public void paint(Graphics g) {

        int cell = 0;
        int uncover = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                cell = field[(i * cols) + j];

                if (inGame && cell == MINE_CELL)
                    inGame = false;

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
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * CELL_SIZE),
                    (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame)
            statusbar.setText("Game lost");
    }

    
}