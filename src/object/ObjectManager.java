package object;

import Observer.ObjectObserver;
import Observer.PlayerObserver;
import entities.Player;
import gameStates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constant.Directions.LEFT;
import static utilz.Constant.Directions.RIGHT;
import static utilz.Constant.ObjectConstant.*;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;
import static utilz.Constant.Projectiles.*;

public class ObjectManager implements PlayerObserver {
    private Playing playing;
    private ArrayList<ObjectObserver> observers = new ArrayList<>();
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage[] chestImgs;
    private BufferedImage[] keyImgs;
    private BufferedImage[] flagImgs;
    private BufferedImage spikeImg, cannonBallImg, padlockImg, platformImg;

    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Chest> chests;
    private ArrayList<Key> keys;
    private ArrayList<Padlock> padlocks = new ArrayList<>();
    private ArrayList<FlagPlatform> platforms;
    private ArrayList<Flag> flags;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();

    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.getSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];
        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.getSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.getSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage cannonSprites = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = cannonSprites.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImg = LoadSave.getSpriteAtlas(LoadSave.CANNON_BALL);

        chestImgs = new BufferedImage[8];
        BufferedImage chestSprites = LoadSave.getSpriteAtlas(LoadSave.TREASURE_CHEST);
        for (int i = 0; i < chestImgs.length; i++) {
            chestImgs[i] = chestSprites.getSubimage(i * 32, 0, 32, 32);
        }

        keyImgs = new BufferedImage[8];
        BufferedImage keySprites = LoadSave.getSpriteAtlas(LoadSave.KEY);
        for (int i = 0; i < keyImgs.length; i++) {
            keyImgs[i] = keySprites.getSubimage(i * 24, 0, 24, 24);
        }

        padlockImg = LoadSave.getSpriteAtlas(LoadSave.PADLOCK);

        platformImg = LoadSave.getSpriteAtlas(LoadSave.FLAG_PLATFORM);

        BufferedImage flagSprite = LoadSave.getSpriteAtlas(LoadSave.FLAG);
        flagImgs = new BufferedImage[9];
        for (int i = 0; i < flagImgs.length; i++)
                flagImgs[i] = flagSprite.getSubimage(34 * i, 0, 34, 93);

    }

    public void update(int[][] lvlData, Player player) {
        checkObjectTouched(player);
        for (Potion p : potions) {
            if (p.isActive())
                p.update();
        }
        for (GameContainer gc : containers) {
            if (gc.isActive())
                gc.update();
        }
        for (Chest c : chests) {
            if (c.isActive()) {
                c.update();
                if (c.getAniIndex() == 1 && c.getAniTick() == 0) {
                    padlocks.add(new Padlock((int) (c.getHitbox().x + 8 * Game.SCALE), (int) (c.getHitbox().y - 10 * Game.SCALE), PADLOCK));
                }
            }
        }
        for (Key k : keys) {
            if (k.isActive()) {
                k.update();
            }
        }
        for (Padlock p : padlocks) {
            if (p.isActive()) {
                p.update(lvlData);
            }
        }
        for (Flag f : flags) {
            if (f.isActive())
                f.update();
        }
        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawChests(g, xLvlOffset);
        drawKeys(g, xLvlOffset);
        drawPadlocks(g, xLvlOffset);
        drawPlatforms(g, xLvlOffset);
        drawFlags(g, xLvlOffset);
    }

    private void drawFlags(Graphics g, int xLvlOffset) {
        for (Flag f : flags) {
            if (f.isActive()) {
                g.drawImage(flagImgs[f.getAniIndex()], (int) (f.getHitbox().x - f.getxDrawOffSet() - xLvlOffset), (int) (f.getHitbox().y - f.getyDrawOffSet()), FLAG_WIDTH, FLAG_HEIGHT, null);
            }
        }
    }

    private void drawPlatforms(Graphics g, int xLvlOffset) {
        for (FlagPlatform p : platforms) {
            if (p.isActive()) {
                g.drawImage(platformImg, (int) (p.getHitbox().x - p.getxDrawOffSet() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffSet()), PLATFORM_WIDTH, PLATFORM_HEIGHT, null);
            }
        }
    }

    private void drawPadlocks(Graphics g, int xLvlOffset) {
        for (Padlock p : padlocks) {
            if (p.isActive()) {
                g.drawImage(padlockImg, (int) (p.getHitbox().x - p.getxDrawOffSet() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffSet()), PADLOCK_WIDTH, PADLOCK_HEIGHT, null);
            }
        }
    }

    private void drawKeys(Graphics g, int xLvlOffset) {
        for (Key k : keys) {
            if (k.isActive()) {
                g.drawImage(keyImgs[k.getAniIndex()], (int) (k.getHitbox().x - k.getxDrawOffSet() - xLvlOffset), (int) (k.getHitbox().y - k.getyDrawOffSet()), KEY_WIDTH, KEY_HEIGHT, null);
            }
        }
    }

    private void drawChests(Graphics g, int xLvlOffset) {
        for (Chest c : chests) {
            if (c.isActive())
                g.drawImage(chestImgs[c.getAniIndex()], (int) (c.getHitbox().x - c.getxDrawOffSet() - xLvlOffset), (int) (c.getHitbox().y - c.getyDrawOffSet()), TREASURE_CHEST_WIDTH, TREASURE_CHEST_HEIGHT, null);
        }
    }
    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjectType() == BARREL) {
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffSet() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffSet()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);

            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0;// blue
                if (p.getObjectType() == RED_POTION)
                    type = 1; //red
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffSet() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffSet()), POTION_WIDTH, POTION_HEIGHT, null);

            }
        }
    }
    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffSet()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitBox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjectType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitBox().x)
                return true;

        } else if (c.getHitbox().x < player.getHitBox().x)
            return true;
        return false;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitBox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }
    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private void shootCannon(Cannon c) {
        int dir = RIGHT;
        if (c.getObjectType() == CANNON_LEFT)
            dir = LEFT;

        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, CANNON_BALL, dir));

    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }

    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);

    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        chests = newLevel.getChests();
        keys = newLevel.getKeys();
        platforms = newLevel.getFlagPlatforms();
        flags = newLevel.getFlags();
        projectiles.clear();
        padlocks.clear();
    }
    public void checkObjectTouched(Player player){
        for (Potion p : potions)
            if (p.isActive()){
                if (player.getHitBox().intersects(p.getHitbox())){
                    p.setActive(false);
                    notifyObserver(p.objectType);
                }
            }
        for (Spike s : spikes)
            if (s.getHitbox().intersects(player.getHitBox())) {
                notifyObserver(SPIKE);

            }
        for (Chest c : chests) {
            if (c.getHitbox().intersects(player.getHitBox())) {
                if (player.getKey() > 0) {
                    notifyObserver(TREASURE_CHEST);
                    c.doAnimation = true;
                }
            }
        }
        for (Key k : keys) {
            if (k.isActive()) {
                if (player.getHitBox().intersects(k.getHitbox())) {
                    k.setActive(false);
                    notifyObserver(KEY);
                }
            }
        }
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                if (p.getHitbox().intersects(player.getHitBox())) {
                    int dir = p.getDir();
                    notifyObserver(CANNON_BALL, dir);
                    p.setActive(false);
                }
            }
        }
    }
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type = 0;
                    if (gc.getObjectType() == BARREL){
                        type = 1;
                    }
                    potions.add (new Potion((int) (gc.getHitbox().x / Game.TILES_SIZE) * Game.TILES_SIZE,
                            (int)(gc.getHitbox().y / Game.TILES_SIZE) * Game.TILES_SIZE, type ));
                    return;
                }
            }
    }

    public void resetAllObject(){
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p: potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Cannon c : cannons)
            c.reset();
        for (Chest c : chests) {
            c.reset();
            c.setUnlocked(false);
        }
        for (Key k : keys) {
            k.reset();
        }
        for (FlagPlatform p : platforms) {
            p.reset();
        }
        for (Flag f : flags) {
            f.reset();
        }
    }
    public ArrayList<Chest> getChests() {
        return chests;
    }
    public void attachObserver(ObjectObserver o) {
        observers.add(o);
    }
    public void notifyObserver(int dir) {
        for (ObjectObserver o : observers) {
            o.updateObjectEffect(dir);
        }
    }
    public void notifyObserver(int objectType, int dir) {
        for (ObjectObserver o : observers) {
            o.updateObjectEffect(objectType, dir);
        }
    }

    @Override
    public void playerHasAttacked(Rectangle2D.Float attackBox) {
        checkObjectHit(attackBox);
    }
}