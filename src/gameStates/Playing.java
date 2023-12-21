package gameStates;

import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;
import main.GameWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Game game;
    public Playing (Game game){
        super (game);
        gameInitialize();
    }
    public void gameInitialize() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (Game.SCALE*64), (int) (Game.SCALE*40));
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());

        gamePanel = new GamePanel(game);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }
    @Override
    public void update(){
        levelManager.update();
        player.update();
    }
    @Override
    public void draw (Graphics g){
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;
            case KeyEvent.VK_DOWN:
                player.setDown(true);
                break;
            case KeyEvent.VK_UP:
                player.setUp(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;
            case KeyEvent.VK_DOWN:
                player.setDown(false);
                break;
            case KeyEvent.VK_UP:
                player.setUp(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public Player getPlayer() {
        return player;
    }
    public void windowFocusLost() {
        player.resetDirBooleans();
    }

}
