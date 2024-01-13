package main;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow implements GameFacade {
    private JFrame jframe;
    public void createWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        jframe.add(gamePanel);
        jframe.setTitle("Pirate King");
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // Handle window focus lost event
                gamePanel.getGame().getPlaying().windowFocusLost();
            }
        });

        // Add your game panel or other components to the JFrame here

        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);


    }

}