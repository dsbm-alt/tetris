import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class TetrisPro extends JPanel implements ActionListener {

    final int ROWS = 20, COLS = 10, CELL = 30;
    int[][] board = new int[ROWS][COLS];

    Timer timer;
    Random rand = new Random();

    int[][] current, next, hold;
    int pieceRow = 0, pieceCol = 3;

    boolean canHold = true;
    boolean gameOver = false;
    boolean paused = false;

    int score = 0;
    int level = 1;
    int lines = 0;

    int[][][] SHAPES = {
        {{1,1,1,1}}, {{1,1},{1,1}}, {{0,1,0},{1,1,1}},
        {{1,0,0},{1,1,1}}, {{0,0,1},{1,1,1}},
        {{1,1,0},{0,1,1}}, {{0,1,1},{1,1,0}}
    };

    Color[] COLORS = {
        Color.CYAN, Color.YELLOW, Color.MAGENTA,
        Color.ORANGE, Color.BLUE, Color.GREEN, Color.RED
    };

    int colorIndex;

    public TetrisPro() {
        setPreferredSize(new Dimension(450, ROWS * CELL));
        setBackground(Color.BLACK);

        timer = new Timer(500, this);
        timer.start();

        next = randomPiece();
        spawnPiece();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!collision(pieceRow, pieceCol - 1)) pieceCol--;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!collision(pieceRow, pieceCol + 1)) pieceCol++;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!collision(pieceRow + 1, pieceCol)) pieceRow++;
                        break;
                    case KeyEvent.VK_UP:
                        rotate();
                        break;
                    case KeyEvent.VK_SPACE:
                        hardDrop();
                        break;
                    case KeyEvent.VK_C:
                        holdPiece();
                        break;
                    case KeyEvent.VK_P:
                        paused = !paused;
                        if (paused) timer.stop(); else timer.start();
                        break;
                }
                repaint();
            }
        });
    }

    int[][] randomPiece() {
        int i = rand.nextInt(SHAPES.length);
        colorIndex = i;
        return SHAPES[i];
    }

    void spawnPiece() {
        current = next;
        next = randomPiece();
        pieceRow = 0;
        pieceCol = 3;
        canHold = true;

        if (collision(pieceRow, pieceCol)) {
            gameOver = true;
            timer.stop();
        }
    }

    void holdPiece() {
        if (!canHold) return;

        if (hold == null) {
            hold = current;
            spawnPiece();
        } else {
            int[][] temp = current;
            current = hold;
            hold = temp;
            pieceRow = 0;
            pieceCol = 3;
        }
        canHold = false;
    }

    void hardDrop() {
        while (!collision(pieceRow + 1, pieceCol)) {
            pieceRow++;
            score += 2;
        }
        lockPiece();
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        if (paused) return;

        pieceRow++;

        if (collision(pieceRow, pieceCol)) {
            pieceRow--;
            lockPiece();
        }

        repaint();
    }

    void lockPiece() {
        merge();
        int cleared = clearLines();
        updateScore(cleared);
        spawnPiece();

        int speed = Math.max(100, 500 - (level * 40));
        timer.setDelay(speed);
    }

    void updateScore(int cleared) {
        if (cleared == 1) score += 100;
        if (cleared == 2) score += 300;
        if (cleared == 3) score += 500;
        if (cleared == 4) score += 800;

        lines += cleared;
        level = lines / 10 + 1;
    }

    boolean collision(int r, int c) {
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                if (current[i][j] == 1) {
                    int nr = r + i, nc = c + j;
                    if (nr >= ROWS || nc < 0 || nc >= COLS) return true;
                    if (nr >= 0 && board[nr][nc] != 0) return true;
                }
            }
        }
        return false;
    }

    void merge() {
        for (int i = 0; i < current.length; i++)
            for (int j = 0; j < current[i].length; j++)
                if (current[i][j] == 1)
                    board[pieceRow + i][pieceCol + j] = colorIndex + 1;
    }

    int clearLines() {
        int count = 0;
        for (int r = 0; r < ROWS; r++) {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == 0) full = false;

            if (full) {
                for (int i = r; i > 0; i--)
                    board[i] = board[i - 1];
                board[0] = new int[COLS];
                count++;
            }
        }
        return count;
    }

    void rotate() {
        int r = current.length, c = current[0].length;
        int[][] rot = new int[c][r];

        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                rot[j][r - 1 - i] = current[i][j];

        int[][] backup = current;
        current = rot;

        if (collision(pieceRow, pieceCol))
            current = backup;
    }

    int ghostRow() {
        int r = pieceRow;
        while (!collision(r + 1, pieceCol)) r++;
        return r;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // tabuleiro
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != 0) {
                    g.setColor(COLORS[board[r][c] - 1]);
                    g.fillRect(c * CELL, r * CELL, CELL, CELL);
                }
                g.setColor(Color.DARK_GRAY);
                g.drawRect(c * CELL, r * CELL, CELL, CELL);
            }
        }

        // ghost
        int gr = ghostRow();
        g.setColor(new Color(255,255,255,50));
        drawPiece(g, current, gr, pieceCol);

        // peça atual
        g.setColor(COLORS[colorIndex]);
        drawPiece(g, current, pieceRow, pieceCol);

        int offsetX = COLS * CELL + 20;

        // textos
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, offsetX, 40);
        g.drawString("Level: " + level, offsetX, 60);

        // próxima peça
        g.drawString("Next:", offsetX, 100);
        drawMini(g, next, offsetX, 110);

        // hold
        g.drawString("Hold:", offsetX, 200);
        if (hold != null)
            drawMini(g, hold, offsetX, 210);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", 40, 300);
        } else if (paused) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("PAUSED", 40, 300);
        }
    }

    void drawPiece(Graphics g, int[][] piece, int r, int c) {
        for (int i = 0; i < piece.length; i++)
            for (int j = 0; j < piece[i].length; j++)
                if (piece[i][j] == 1)
                    g.fillRect((c + j) * CELL, (r + i) * CELL, CELL, CELL);
    }

    void drawMini(Graphics g, int[][] piece, int x, int y) {
        for (int i = 0; i < piece.length; i++)
            for (int j = 0; j < piece[i].length; j++)
                if (piece[i][j] == 1)
                    g.fillRect(x + j * 15, y + i * 15, 15, 15);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris PRO");
        f.add(new TetrisPro());
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}