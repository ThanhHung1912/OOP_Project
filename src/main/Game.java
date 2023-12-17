package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private Player player;
    private LevelManager levelManager;

    private Thread thread;


    public static final int FPS = 120;
    public static final int UPS = 200;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) SCALE * TILES_DEFAULT_SIZE;
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    public Game() {
        gameInitialize();
        startGameLoop();
    }

    public void gameInitialize() {
        player = new Player(30, 30, (int) (SCALE*64), (int) (SCALE*40));
        levelManager = new LevelManager(this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }

    public Player getPlayer() {
        return player;
    }

    public void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        levelManager.update();
        player.update();

    }
    public void render(Graphics g) {
        levelManager.draw(g);
        player.render(g);

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
}
