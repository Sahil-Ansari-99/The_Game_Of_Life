import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GameWorld extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    private int width, height, size, boxNumber;
    private int animationSpeed;
    private int xOffset, yOffset;
    private int offsetStep;
    private char currKey;
    private Timer timer;
    private boolean isAnimationRunning;
    private Simulator simulator;
    private MenuHandler menuHandler;

    public GameWorld(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = 10;
        this.boxNumber = width / size;
        this.currKey = 'a';
        this.animationSpeed = 100;
        this.isAnimationRunning = false;
        this.xOffset = boxNumber / 3;
        this.yOffset = boxNumber / 3;
        this.offsetStep = 2;
        this.timer = new Timer(animationSpeed, this);
        this.simulator = new Simulator(2 * boxNumber, this);
        setSize(width = this.width, height = this.height);
        this.setFocusable(true);
        this.requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        menuHandler = new MenuHandler(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        menuHandler.position();
        menuHandler.addToFrame();
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
            for (int j = 0; j < width; j += size) {
                g.drawRect(i, j, size, size);
                if (states[xOffset + currRow][yOffset + currCol] == 0) g.setColor(Color.WHITE);
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
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals("Run")) {
                isAnimationRunning = true;
                System.out.println("Starting animation...");
                repaint();
            }
            if (actionCommand.equals("Pause")) {
                isAnimationRunning = false;
                timer.stop();
                System.out.println("Animation stopped...");
            }
            if (actionCommand.equals("Reset")) {
                System.out.println("Performing Reset...");
                simulator.resetStates();
                menuHandler.resetSpeed();
                resetVariables();
                repaint();
            }
            if (actionCommand.equals("Centre")) {
                System.out.println("Centre Aligning...");
                xOffset = boxNumber / 3;
                yOffset = boxNumber / 3;
                if (!isAnimationRunning) repaint();
            }
            if (actionCommand.equals("\u2191")) {
                System.out.println("Up...");
                yOffset = Math.max(0, yOffset - offsetStep);
                if (!isAnimationRunning) repaint();
            }
            if (actionCommand.equals("\u2193")) {
                System.out.println("Down...");
                yOffset = Math.min(boxNumber, yOffset + offsetStep);
                if (!isAnimationRunning) repaint();
            }
            if (actionCommand.equals("\u2190")) {
                System.out.println("Left....");
                xOffset = Math.max(0, xOffset - offsetStep);
                if (!isAnimationRunning) repaint();
            }
            if (actionCommand.equals("\u2192")) {
                System.out.println("Right...");
                xOffset = Math.min(boxNumber, xOffset + offsetStep);
                if (!isAnimationRunning) repaint();
            }
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
            System.out.println("Performing Reset...");
            isAnimationRunning = false;
            simulator.resetStates();
            animationSpeed = 100;
            menuHandler.resetSpeed();
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
                simulator.setAlive(x / size + xOffset, y / size + yOffset);
            }
            else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                simulator.setDead(x / size + xOffset, y / size + yOffset);
            }
            repaint();
        }
    }

    public void setAnimationSpeed(int speed) {
        double change = ((50 - speed) / 50.0) * 100;
        animationSpeed = 100 + (int)change;
    }

    private void resetVariables() {
        isAnimationRunning = false;
        animationSpeed = 100;
        xOffset = boxNumber / 3;
        yOffset = boxNumber / 3;
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
        GameWorld gameWorld = new GameWorld(500, 700);
        this.setTitle("The Game Of Life");
        this.setSize(new Dimension(500, 700));
        this.setMinimumSize(new Dimension(500, 700));
        this.setResizable(false);
        add(gameWorld);
        pack();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
