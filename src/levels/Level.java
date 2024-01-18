package levels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import entities.Pinkstar;
import main.Game;
import object.*;
import utilz.HelpMethods;

import static main.Game.TILES_SIZE;
import static utilz.Constant.ObjectConstant.FLAG;
import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Pinkstar> pinkstars;
    private ArrayList<GameContainer> containers;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Chest> chests;
    private ArrayList<Key> keys;
    private ArrayList<FlagPlatform> platforms;
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
        createSpikes();
        createCannons();
        createChests();
        createKeys();
        createPlatforms();
        calcLvlOffset();
        calcPlayerSpawn();
    }

    private void createPlatforms() {
        platforms = HelpMethods.GetPlatforms(img);
    }

    private void createKeys() {
        keys = HelpMethods.GetKeys(img);
    }

    private void createCannons() {
        cannons = HelpMethods.GetCannons(img);
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
        maxLvlOffsetX = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
        pinkstars = GetPinkstars(img);
    }

    private void createSpikes() {
        spikes = HelpMethods.GetSpikes(img);
    }
    private void createChests() {
        chests = HelpMethods.GetChests(img);
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
    public ArrayList<Pinkstar> getPinkstars() {
        return pinkstars;
    }
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions(){ return potions;}
    public ArrayList<GameContainer> getContainers(){ return containers;}
    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
    public ArrayList<Cannon> getCannons(){
        return cannons;
    }
    public ArrayList<Chest> getChests() {
        return chests;
    }
    public ArrayList<Key> getKeys() {
        return keys;
    }
    public ArrayList<FlagPlatform> getFlagPlatforms() {
        return platforms;
    }
    public ArrayList<Flag> getFlags() {
        ArrayList<Flag> list = new ArrayList<>();
        for (FlagPlatform p : platforms) {
            list.add(new Flag(p.getX(), p.getY(), FLAG));
        }
        return list;
    }
}
