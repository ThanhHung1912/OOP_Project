package gameStates;

import entities.*;
import levels.LevelManager;
import main.Game;
import static utilz.Constant.Environment.*;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import object.Chest;
import object.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
//    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
//    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudPos;
    private Random rnd = new Random();

    //Pause
    private boolean paused = false;
    // Game Over
    private boolean gameOver = false;
    //Game Completed
    private boolean lvlCompleted = false;
    //Player dying
    private boolean playerDying = false;

    public Playing (Game game){
        super(game);
        initPlaying();

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[8];
        for (int i = 0; i < 8; i++) {
            smallCloudPos[i] = (int) (90*Game.SCALE + rnd.nextInt((int) (100*Game.SCALE)));
        }

        calcLvlOffset();
        loadStartLevel();
    }
    public void loadNextLevel() {
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
    }
    private void loadStartLevel() {

        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    public void initPlaying() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (Game.SCALE*64), (int) (Game.SCALE*40), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);

        addObserver();
    }

    private void addObserver() {
        objectManager.attachObserver(player);
    }

    @Override
    public void update(){
        if (paused) {
            pauseOverlay.update();
        }
        else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver){
            gameOverOverlay.update();
        } else if (playerDying){
            player.update();
        }
        else if (!gameOver) {
            checkLevelCompleted();
            objectManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            checkCloseToBorder();
        }
    }

    private void checkLevelCompleted() {
        boolean isAnyActive = false;
        boolean isAnyLocked = false;
        for (Crabby c : enemyManager.getCrabbies()) {
            if (c.isActive()) {
                isAnyActive = true;
            }
        }
        for (Chest c : objectManager.getChests()) {
            if (!c.isUnlocked()) {
                isAnyLocked = true;
            }
        }
        if (!isAnyActive && !isAnyLocked) {
            setLevelCompleted(true);
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;

    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObject();
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw (g, xLvlOffset);
        player.render(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUDS_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUDS_WIDTH, BIG_CLOUDS_HEIGHT, null);
        }
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUDS_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudPos[i], SMALL_CLOUDS_WIDTH, SMALL_CLOUDS_HEIGHT, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if(e.getButton() == MouseEvent.BUTTON3){
                player.powerAttack();
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }  else {
            gameOverOverlay.mouseReleased(e);
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        }  else {
            gameOverOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_Z:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    Gamestate.state = Gamestate.MENU;
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }
    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }
    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if(levelCompleted){
            game.getAudioPlayer().LvlCompleted();
        }
    }
    public void unpauseGame() {
        paused = false;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }



    public Player getPlayer() {
        return player;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
    public void windowFocusLost() {
        player.resetDirBooleans();
    }
    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){
        objectManager.checkObjectHit(attackbox);
    }

    public void setPlayerDying(boolean playerDying){
        this.playerDying = playerDying;
    }
}
