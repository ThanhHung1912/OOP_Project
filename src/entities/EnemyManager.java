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
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }
    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    public void update(int[][] lvlData, Player player) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                c.update(lvlData, player);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies)
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()], (int) (c.getHitBox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX()),
                        (int) (c.getHitBox().y - CRABBY_DRAWOFFSET_Y),
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
//                c.drawHitbox(g, xLvlOffset);
//                c.drawAttackBox(g, xLvlOffset);
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
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++)
            for (int i = 0; i < crabbyArr[j].length; i++)
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for (Crabby c : crabbies) {
            c.resetEnemy();
        }
    }
    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }

    @Override
    public void playerHasAttacked(Rectangle2D.Float attackBox) {
        checkEnemyHit(attackBox);
    }
}
