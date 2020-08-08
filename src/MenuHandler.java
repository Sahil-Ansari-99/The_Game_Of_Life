import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MenuHandler {
    private GameWorld gameWorld;
    private JSlider animationSpeedSlider;
    private JButton run, reset, pause, up, down, left, right, centre;
    private JLabel animationSpeedLabel;
    private int gameWorldWidth, gameWorldHeight,menuHeight;

    public MenuHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.gameWorldWidth = gameWorld.getWidth();
        this.gameWorldHeight = gameWorld.getHeight();
        this.menuHeight = gameWorldHeight - gameWorldWidth;

        animationSpeedLabel = new JLabel("Speed:");
        animationSpeedLabel.setVisible(true);

        run = new JButton("Run");
        run.addActionListener(gameWorld);

        pause = new JButton("Pause");
        pause.addActionListener(gameWorld);

        reset = new JButton("Reset");
        reset.addActionListener(gameWorld);

        up = new JButton("\u2191");
        up.addActionListener(gameWorld);

        down = new JButton("\u2193");
        down.addActionListener(gameWorld);

        left = new JButton("\u2190");
        left.addActionListener(gameWorld);

        right = new JButton("\u2192");
        right.addActionListener(gameWorld);

        centre = new JButton("Centre");
        centre.addActionListener(gameWorld);

        animationSpeedSlider = new JSlider();
        animationSpeedSlider.setVisible(true);
        animationSpeedSlider.setName("speed");
        animationSpeedSlider.setMajorTickSpacing(5);
        animationSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                animationSpeedSlider.setValue(source.getValue());
                gameWorld.setAnimationSpeed(animationSpeedSlider.getValue());
            }
        });
    }

    public void position() {
        int runBtnX = 10;
        run.setBounds(runBtnX, gameWorld.getHeight() - 40, run.getWidth(), run.getHeight());

        int pauseBtnX = runBtnX + run.getWidth() + 20;
        pause.setBounds(pauseBtnX, gameWorld.getHeight() - 40, pause.getWidth(), pause.getHeight());

        int resetBtnX = pauseBtnX + pause.getWidth() + 20;
        reset.setBounds(resetBtnX, gameWorld.getHeight() - 40, reset.getWidth(), reset.getHeight());

        centre.setBounds(pauseBtnX, pause.getY() - 40, centre.getWidth(), centre.getHeight());

        int animationSpeedLabelX = 10;
        animationSpeedLabel.setBounds(animationSpeedLabelX, gameWorldWidth + 40, animationSpeedLabel.getWidth(), animationSpeedLabel.getHeight());
        animationSpeedSlider.setBounds(animationSpeedLabelX + animationSpeedLabel.getWidth(), gameWorldWidth + 40, animationSpeedSlider.getWidth(), animationSpeedSlider.getHeight());

        int leftBtnX = gameWorldWidth / 2 + 60;
        left.setBounds(leftBtnX, gameWorldWidth + (menuHeight - left.getWidth()) / 2, left.getWidth(), left.getHeight());

        int upBtnX = leftBtnX + left.getWidth();
        up.setBounds(upBtnX, left.getY() - left.getHeight() - 20, up.getWidth(), up.getHeight());

        int rightBtnX = upBtnX + up.getWidth();
        right.setBounds(rightBtnX, gameWorldWidth + (menuHeight - left.getWidth()) / 2, right.getWidth(), right.getHeight());

        down.setBounds(upBtnX, left.getY() + left.getHeight() + 20, down.getWidth(), down.getHeight());
    }

    public void addToFrame() {
        gameWorld.add(run);
        gameWorld.add(pause);
        gameWorld.add(reset);
        gameWorld.add(centre);
        gameWorld.add(animationSpeedLabel);
        gameWorld.add(animationSpeedSlider);
        gameWorld.add(left);
        gameWorld.add(up);
        gameWorld.add(right);
        gameWorld.add(down);
    }

    public void resetSpeed() {
        animationSpeedSlider.setValue(50);
    }
}
