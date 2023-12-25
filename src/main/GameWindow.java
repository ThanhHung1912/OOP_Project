package main;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame jframe;
    private static GameWindow gameWindow;

    private GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        jframe.add(gamePanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();            }
        });
    }

    public static GameWindow getGameWindow(GamePanel gamePanel) {
        if (gameWindow == null) {
            gameWindow = new GameWindow(gamePanel);
        }
        return gameWindow;
    }
}
