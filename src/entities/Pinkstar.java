package entities;

import static utilz.Constant.EnemyConstant.*;

import static utilz.Constant.Directions.*;
import static utilz.HelpMethods.*;

import gameStates.Playing;

public class Pinkstar extends Enemy {

    private boolean preRoll = true;
    private int tickSinceLastDmgToPlayer;
    private int tickAfterRollInIdle;
    private int rollDurationTick, rollDuration = 300;

    public Pinkstar(float x, float y) {
        super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
        initHitBox(17, 21);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        update();
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    preRoll = true;
                    if (tickAfterRollInIdle >= 120) {
                        if (IsEntityOnFloor(hitBox, lvlData))
                            newState(RUNNING);
                        else
                            inAir = true;
                        tickAfterRollInIdle = 0;
                        tickSinceLastDmgToPlayer = 60;
                    } else
                        tickAfterRollInIdle++;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        newState(ATTACK);
                        setWalkDir(player);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (preRoll) {
                        if (aniIndex >= 3)
                            preRoll = false;
                    } else {
                        move(lvlData);
                        checkDmgToPlayer(player);
                        checkRollOver();
                    }
                    break;
                case HIT:
                    tickAfterRollInIdle = 120;

                    break;
            }
        }
    }

    private void checkDmgToPlayer(Player player) {
        if (hitBox.intersects(player.getHitBox()))
            if (tickSinceLastDmgToPlayer >= 60) {
                tickSinceLastDmgToPlayer = 0;
                player.changeHealth(-GetEnemyDmg(enemyType));
                if (this.hitBox.x > player.hitBox.x)
                    player.setHit(LEFT);
                else {
                    player.setHit(RIGHT);
                }
            } else
                tickSinceLastDmgToPlayer++;
    }

    private void setWalkDir(Player player) {
        if (player.getHitBox().x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }


    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (state == ATTACK)
            xSpeed *= 2;

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
            if (IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }

        if (state == ATTACK) {
            rollOver();
            rollDurationTick = 0;
        }

        changeWalkDir();

    }

    private void checkRollOver() {
        rollDurationTick++;
        if (rollDurationTick >= rollDuration) {
            rollDurationTick = 0;
        }
    }

    private void rollOver() {
        newState(IDLE);
    }
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;

    }
}