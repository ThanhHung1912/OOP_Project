package object;

import entities.Player;
import gameStates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constant.ObjectConstant.*;
import static utilz.HelpMethods.CanCannonSeePlayer;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private BufferedImage spikeImg;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();

    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitBox()))
                p.dead();
    }
    public void applyEffectToPlayer(Potion p) {
        if (p.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
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
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }


    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions) {
            if (p.isActive())
                p.update();
        }
        for (GameContainer gc : containers) {
            if (gc.isActive())
                gc.update();
        }
        updateCannons(lvlData, player);

    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjectType() == BARREL) {
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffSet() - xLvlOffset), (int) (gc.getHitbox().y - gc.getxDrawOffSet()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);

            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0;// blue
                if (p.getObjectType() == RED_POTION)
                    type = 1; //red
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffSet() - xLvlOffset), (int) (p.getHitbox().y - p.getxDrawOffSet()), POTION_WIDTH, POTION_HEIGHT, null);

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
                            if (CanCannonSeePlayer(lvlData, player.getHitBox(), c.getHitbox(), c.getTileY())) {
                                shootCannon(c);
                            }

            c.update();
        }
    }

    private void shootCannon(Cannon c) {
        c.setAnimation(true);

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

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
    }
    public void checkObjectTouched(Rectangle2D.Float hitbox){
        for (Potion p: potions)
            if (p.isActive()){
                if (hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type =0;
                    if (gc.getObjectType() == BARREL){
                        type = 1;
                    }
                    potions.add (new Potion((int)(gc.getHitbox().x+gc.getHitbox().width/2),
                            (int)(gc.getHitbox().y- gc.getHitbox().height), type ));
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
    }
}