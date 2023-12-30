package main;

import entities.Player;
import gameStates.Gamestate;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
    private static GamePanel gamePanel;
    private Game game;
    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(new MouseInputs(this));
        addMouseMotionListener(new MouseInputs(this));
    }

//    public static GamePanel getGamePanel(Game game) {
//        if (gamePanel == null) {
//            gamePanel = new GamePanel(game);
//        }
//        return gamePanel;
//    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }


    public Game getGame() {
        return game;
    }

}