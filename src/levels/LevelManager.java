package levels;

import main.Game;
import utilz.LoadSave;
import static main.Game.TILES_SIZE;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprites;
    private Level level;

    public LevelManager(Game game) {
        this.game = game;
        level = new Level(LoadSave.GetLevelData());
        loadTerrain();
    }

    public void update() {

    }
    public void draw(Graphics g) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < Game.TILES_IN_WIDTH; j++) {
                int index = level.getSpriteIndex(j, i);
                g.drawImage(levelSprites[index], j*TILES_SIZE, i*TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
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
}
