package entities;

import java.awt.Graphics;

import Observer.PlayerObserver;
import gameStates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import static utilz.Constant.EnemyConstant.*;

public class EnemyManager implements PlayerObserver {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private BufferedImage[][] pinkstarArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();
    private ArrayList<Pinkstar> pinkstars = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }
    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        pinkstars = level.getPinkstars();
    }

    public void update(int[][] lvlData, Player player) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                c.update(lvlData, player);
            }
        }
        for (Pinkstar p : pinkstars) {
            if (p.isActive()) {
                p.update(lvlData, player);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawPinkstar(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies)
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()], (int) (c.getHitBox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX()),
                        (int) (c.getHitBox().y - CRABBY_DRAWOFFSET_Y),
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
            }

    }
    private void drawPinkstar(Graphics g, int xLvlOffset) {
        for (Pinkstar p : pinkstars)
            if (p.isActive()) {
                g.drawImage(pinkstarArr[p.getState()][p.getAniIndex()], (int) (p.getHitBox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX()),
                        (int) (p.getHitBox().y - PINKSTAR_DRAWOFFSET_Y),
                        PINKSTAR_WIDTH * p.flipW(), PINKSTAR_HEIGHT, null);
            }

    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies)
            if (c.isActive())
                if(c.getCurrentHealth() > 0) {
                    if (attackBox.intersects(c.getHitBox())) {
                        c.hurt(10);
                        return;
                    }
                }
        for (Pinkstar p : pinkstars)
            if (p.isActive())
                if(p.getCurrentHealth() > 0) {
                    if (attackBox.intersects(p.getHitBox())) {
                        p.hurt(10);
                        return;
                    }
                }

    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage crabbyAtlas = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++)
            for (int i = 0; i < crabbyArr[j].length; i++)
                crabbyArr[j][i] = crabbyAtlas.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
        pinkstarArr = new BufferedImage[5][8];
        BufferedImage pinkstarAtlas = LoadSave.getSpriteAtlas(LoadSave.PINKSTAR_SPRITE);
        for (int j = 0; j < pinkstarArr.length; j++)
            for (int i = 0; i < pinkstarArr[j].length; i++)
                pinkstarArr[j][i] = pinkstarAtlas.getSubimage(i * PINKSTAR_WIDTH_DEFAULT, j * PINKSTAR_HEIGHT_DEFAULT,
                        PINKSTAR_WIDTH_DEFAULT, PINKSTAR_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for (Crabby c : crabbies) {
            c.resetEnemy();
        }
        for (Pinkstar p : pinkstars) {
            p.resetEnemy();
        }
    }
    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }
    public ArrayList<Pinkstar> getPinkstars() {
        return pinkstars;
    }

    @Override
    public void playerHasAttacked(Rectangle2D.Float attackBox) {
        checkEnemyHit(attackBox);
    }
}
