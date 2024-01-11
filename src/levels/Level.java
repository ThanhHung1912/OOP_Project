package levels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import object.*;
import utilz.HelpMethods;

import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private  ArrayList<Crabby> crabs;
    private  ArrayList<GameContainer> containers;
    private  ArrayList<Potion> potions;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;


    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        calcLvlOffset();
        calcPlayerSpawn();
    }

    private void createContainers() {
        containers = HelpMethods.GetContainer(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
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
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions(){ return potions;}
    public ArrayList<GameContainer> getContainers(){ return containers;}
}
