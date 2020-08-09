import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// creates the UI
class GameWorld extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    private int width, height, size, boxNumber;
    private int animationSpeed;
    private int xOffset, yOffset;
    private int offsetStep;
    private int boxMultiplier;
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
        this.boxMultiplier = 3;
        this.currKey = 'a';
        this.animationSpeed = 100;
        this.isAnimationRunning = false;
        this.xOffset = boxMultiplier * boxNumber / 3;
        this.yOffset = boxMultiplier * boxNumber / 3;
        this.offsetStep = 2;
        this.timer = new Timer(animationSpeed, this);
        this.simulator = new Simulator(boxMultiplier * boxNumber, this);
        setSize(this.width, this.height);
        this.setFocusable(true);
        this.requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        menuHandler = new MenuHandler(this);
    }

    // draws all UI components, called every frame
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMenu();
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

    // draws Menu
    public void drawMenu() {
        menuHandler.position();
        menuHandler.addToFrame();
    }

    // Starts Simulation
    private void performRun() {
        isAnimationRunning = true;
        System.out.println("Starting animation...");
        repaint();
    }

    // Pauses Simulation
    private void performPause() {
        isAnimationRunning = false;
        timer.stop();
        System.out.println("Animation stopped...");
    }

    // Resets the world
    private void performReset() {
        System.out.println("Performing Reset...");
        simulator.resetStates();
        menuHandler.resetSpeed();
        resetVariables();
        repaint();
    }

    // Centre aligns the world to default view space
    private void performCentreAlign() {
        System.out.println("Centre Aligning...");
        xOffset = boxMultiplier * boxNumber / 3;
        yOffset = boxMultiplier * boxNumber / 3;
        if (!isAnimationRunning) repaint();
    }

    // Scrolls Up in the world
    private void goUp() {
        System.out.println("Up...");
        yOffset = Math.max(0, yOffset - offsetStep);
        if (!isAnimationRunning) repaint();
    }

    // Scrolls down in the world
    private void goDown() {
        System.out.println("Down...");
        yOffset = Math.min((boxMultiplier - 1) * boxNumber, yOffset + offsetStep);
        if (!isAnimationRunning) repaint();
    }

    // Scrolls left in the world
    private void goLeft() {
        System.out.println("Left....");
        xOffset = Math.max(0, xOffset - offsetStep);
        if (!isAnimationRunning) repaint();
    }

    // Scrolls right in the world
    private void goRight() {
        System.out.println("Right...");
        xOffset = Math.min((boxMultiplier - 1) * boxNumber, xOffset + offsetStep);
        if (!isAnimationRunning) repaint();
    }

    // handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() != null) {
            String actionCommand = e.getActionCommand();
            switch (actionCommand) {
                case "Run" -> performRun();
                case "Pause" -> performPause();
                case "Reset" -> performReset();
                case "Centre" -> performCentreAlign();
                case "\u2191" -> goUp();
                case "\u2193" -> goDown();
                case "\u2190" -> goLeft();
                case "\u2192" -> goRight();
            }
        } else {
            repaint();
        }
    }

    // handles keyboard events
    @Override
    public void keyTyped(KeyEvent e) {

    }

    // handles keyboard events
    @Override
    public void keyPressed(KeyEvent e) {
        currKey = e.getKeyChar();
        System.out.println(currKey);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            isAnimationRunning = !isAnimationRunning;
            if (isAnimationRunning) {
                performRun();
            }
            else {
                performPause();
            }
        }
        if (currKey == 'r') {
            performReset();
        }
        if (currKey == 'c') {
            performCentreAlign();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // handles mouse click events
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

    // performs required computation to map mouse clicks
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

    // changes animation speed
    public void setAnimationSpeed(int speed) {
        double change = ((50 - speed) / 50.0) * 100;
        animationSpeed = 100 + (int)change;
    }

    // resets the world variables
    private void resetVariables() {
        isAnimationRunning = false;
        animationSpeed = 100;
        xOffset = boxMultiplier * boxNumber / 3;
        yOffset = boxMultiplier * boxNumber / 3;
    }
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
        System.out.println("Packed...");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
//        main.validate();
    }
}
