import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GameWorld extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    private int width, height, size, boxNumber;
    private int animationSpeed;
    private char currKey;
    private Timer timer;
    private boolean isAnimationRunning;
    private Simulator simulator;

    public GameWorld(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = 10;
        this.boxNumber = width / size;
        this.currKey = 'a';
        this.animationSpeed = 100;
        this.isAnimationRunning = false;
        this.timer = new Timer(animationSpeed, this);
        this.simulator = new Simulator(2*boxNumber, this);
        setSize(width = this.width, height = this.height);
        this.setFocusable(true);
        this.requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] states;
        if (isAnimationRunning) {
            timer.setDelay(animationSpeed);
            timer.start();
            states = simulator.simulateOnce();
        } else {
            states = simulator.getStates();
        }
        g.setColor(Color.WHITE);
        int currRow = 0, currCol = 0;
        for (int i = 0; i < width; i += size) {
            currCol = 0;
            for (int j = 0; j < height; j += size) {
                g.drawRect(i, j, size, size);
                if (states[currRow][currCol] == 0) g.setColor(Color.WHITE);
                else g.setColor(Color.BLACK);
                g.fillRect(i, j, size, size);
                currCol++;
            }
            currRow++;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() != null) {
            //TODO
        } else {
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        currKey = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            isAnimationRunning = !isAnimationRunning;
            if (isAnimationRunning) {
                System.out.println("Starting animation...");
                repaint();
            }
            else {
                System.out.println("Animation stopped...");
                timer.stop();
            }
        }
        if (currKey == 'r') {
            simulator.resetStates();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        performMouseClick(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        performMouseClick(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void performMouseClick(MouseEvent mouseEvent) {
        int xRem = mouseEvent.getPoint().x % size;
        int yRem = mouseEvent.getPoint().y % size;
        int x = mouseEvent.getPoint().x - xRem;
        int y = mouseEvent.getPoint().y - yRem;
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                simulator.setAlive(x / size, y / size);
            }
            else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                simulator.setDead(x / size, y / size);
            }
            repaint();
        }
    }

//    private void startAnimation() {
//        if (isAnimationRunning) {
//            System.out.println("Animating...");
//            simulator.simulateOnce();
//            repaint();
//        }
//        timer.stop();
//    }
}

class Main extends JFrame {
    public Main() {
        GameWorld gameWorld = new GameWorld(500, 500);
        this.setTitle("The Game Of Life");
        this.setSize(new Dimension(500, 500));
        this.setMinimumSize(new Dimension(500, 500));
        this.setResizable(false);
        add(gameWorld);
        pack();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
