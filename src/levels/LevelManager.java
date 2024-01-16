package levels;

import gameStates.Gamestate;
import main.Game;
import utilz.LoadSave;
import static main.Game.TILES_SIZE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprites;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;


    public LevelManager(Game game) {
        this.game = game;
        loadTerrain();
        levels = new ArrayList<>();
        buildAllLevels();
    }
    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels! Game completed");
            Gamestate.state = Gamestate.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Level(img));
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < levels.get(lvlIndex).getLvlData()[0].length; j++) {
                int index = levels.get(lvlIndex).getSpriteIndex(j, i);
                g.drawImage(levelSprites[index],Game.TILES_SIZE * j - lvlOffset, Game.TILES_SIZE * i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void loadTerrain() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprites = new BufferedImage[64];
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                levelSprites[count] = img.getSubimage(j * 32, i * 32, 32, 32);
                count++;
            }
        }
    }
    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public int getLvlIndex(){
        return lvlIndex;
    }
}
