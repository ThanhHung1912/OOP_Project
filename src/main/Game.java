package main;

import entities.Player;
import gameStates.Gamestate;
import gameStates.Playing;
import levels.LevelManager;

import java.awt.*;
import gameStates.Menu;
import utilz.LoadSave;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread thread;
    private Menu menu;
    private Playing playing;
    private static Game game;


    public static final int FPS = 120;
    public static final int UPS = 200;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (SCALE * TILES_DEFAULT_SIZE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    private Game() {
        gameInitialize();
        startGameLoop();
    }

    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return  game;
    }

    private void gameInitialize() {
        menu = new gameStates.Menu(this);
        playing = new Playing (this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }


    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        switch (Gamestate.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:

            case QUIT:
                System.exit(0);
                break;
            default:
                break;
        }

    }
    public void render(Graphics g) {
        switch (Gamestate.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        double nanoSPerFrame = 1000000000.0 / FPS;
        double nanoSPerUpdate =  1000000000.0 / UPS;

        double deltaF = 0;
        double deltaU = 0;
        double previousTime = System.nanoTime();
        double lastCheck = System.nanoTime();

        int framesCount = 0;
        int updatesCount = 0;

        while(true) {
            double currentTime = System.nanoTime();
            deltaF += (currentTime - previousTime) / nanoSPerFrame;
            deltaU += (currentTime - previousTime) / nanoSPerUpdate;
            previousTime = currentTime;
            if (deltaF >= 1) {
                gamePanel.repaint();
                framesCount++;
                deltaF--;
            }
            if (deltaU >= 1) {
                update();
                updatesCount++;
                deltaU--;
            }

            if (currentTime - lastCheck >= 1000000000) {
                System.out.printf("FPS: %d || UPS: %d%n", framesCount, updatesCount);
                framesCount = 0;
                updatesCount = 0;
                lastCheck = currentTime;
            }
        }
    }
    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Game getGame() {
        return game;
    }
}
