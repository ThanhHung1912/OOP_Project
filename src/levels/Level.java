package levels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;

import static utilz.HelpMethods.GetCrabs;
import static utilz.HelpMethods.GetLevelData;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private  ArrayList<Crabby> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;



    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffset();
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int[][] getLvlData() {
        return lvlData;
    }
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }
    public int getLvlOffset() {
        return maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }
}
