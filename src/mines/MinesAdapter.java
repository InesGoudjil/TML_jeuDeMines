package mines;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesAdapter extends MouseAdapter {

    private Board board;

    public MinesAdapter(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        int cCol = x / board.CELL_SIZE;
        int cRow = y / board.CELL_SIZE;

        boolean rep = false;

        if (!board.inGame) {
            board.newGame();
            board.repaint();
        }

        if ((x < board.cols * board.CELL_SIZE) && (y < board.rows * board.CELL_SIZE)) {

            if (e.getButton() == MouseEvent.BUTTON3) {

                if (board.field[(cRow * board.cols) + cCol] > board.MINE_CELL) {
                    rep = true;

                    if (board.field[(cRow * board.cols) + cCol] <= board.COVERED_MINE_CELL) {
                        if (board.mines_left > 0) {
                            board.field[(cRow * board.cols) + cCol] += board.MARK_FOR_CELL;
                            board.mines_left--;
                            board.statusbar.setText(Integer.toString(board.mines_left));
                        } else
                            board.statusbar.setText("No marks left");
                    } else {

                        board.field[(cRow * board.cols) + cCol] -= board.MARK_FOR_CELL;
                        board.mines_left++;
                        board.statusbar.setText(Integer.toString(board.mines_left));
                    }
                }

            } else {

                if (board.field[(cRow * board.cols) + cCol] > board.COVERED_MINE_CELL) {
                    return;
                }

                if ((board.field[(cRow * board.cols) + cCol] > board.MINE_CELL) &&
                        (board.field[(cRow * board.cols) + cCol] < board.MARKED_MINE_CELL)) {

                    board.field[(cRow * board.cols) + cCol] -= board.COVER_FOR_CELL;
                    rep = true;

                    if (board.field[(cRow * board.cols) + cCol] == board.MINE_CELL)
                        board.inGame = false;
                    if (board.field[(cRow * board.cols) + cCol] == board.EMPTY_CELL)
                        board.find_empty_cells((cRow * board.cols) + cCol);
                }
            }

            if (rep)
                board.repaint();

        }
    }
}
