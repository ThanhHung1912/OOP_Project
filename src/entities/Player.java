package entities;

import audio.AudioPlayer;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import gameStates.Playing;

import static main.Game.SCALE;
import static utilz.Constant.*;
import static utilz.Constant.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int[][] lvlData;
    private boolean left, right, jump;
    private boolean isMoving = false;
    private boolean attacking = false;

    private float xDrawOffset = 21 * SCALE;
    private float yDrawOffset = 4 * SCALE;


    // Applying Gravity
    private float jumpSpeed = -2.25f * SCALE;

    // Health Bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    protected Playing playing;

    private int tileY = 0;
    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;


    public Player(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.state = IDLE;
        this.playing = playing;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * SCALE;
        initHitBox(20, 28);
        loadAnimation();
        initAttackBox();
    }
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x + hitBox.width + (int) (Game.SCALE * 10), y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    //set direction
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void update() {
        updateHealthBar();
        updatePowerBar();

        if(currentHealth <= 0) {
            if(state != DEAD){
                state = DEAD;
                //Reset animation
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= TICKS_PER_ANI - 1){
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimationTick();
            }

            return;
        }

        updateAttackBox();

        updatePos();
        if (isMoving) {
            checkObjectTouched();
            tileY = (int) (hitBox.y / Game.TILES_SIZE);
            if(powerAttackActive){
                powerAttackTick++;
                if(powerAttackTick >= 35){
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }

        if (attacking || powerAttackActive) {
            checkAttack();
        }
        updateAnimationTick();
        updateAnimation();
    }

    private void checkObjectTouched() {
        playing.checkObjectTouched(this);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;

        if(powerAttackActive){
            attackChecked = false;
        }

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();

    }
    private void updateAttackBox() {
        if (right && left){
            if(flipW == 1){
                attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
            } else {
                attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
            }
        } else if (right || (powerAttackActive && flipW == 1))
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        else if (left || (powerAttackActive && flipW == -1))
            attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar(){
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if(powerGrowTick >= powerGrowSpeed){
            powerGrowTick = 0;
            changePower(1);
        }
    }


    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex],
                (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitBox.y - yDrawOffset), width * flipW, height, null);
        drawUI(g);
//        drawHitbox(g, lvlOffset);
//        drawAttackBox(g, lvlOffset);
    }
    private void drawUI(Graphics g) {
        //Background UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        //Health bar
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        //Power bar
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updatePos() {
        isMoving = false;

        if (jump)
            jump();

        if (!inAir)
            if(!powerAttackActive) {
                if ((!left && !right) || (right && left))
                    return;
            }
        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(powerAttackActive){
            if((!left && !right) || (left && right)){
                if(flipW == -1){
                    xSpeed = -walkSpeed;
                } else {
                    xSpeed = walkSpeed;
                }
            }
            xSpeed *= 3;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitBox, lvlData))
                inAir = true;

        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitBox.y = GetCollisionRoof(hitBox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = GRAVITY;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        isMoving = true;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetCollisionGround(hitBox, xSpeed);
            if(powerAttackActive){
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    private void jump() {
        if (inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void updateAnimation() {
        int last_action = state;
        if (isMoving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if(powerAttackActive){
            state = ATTACK;
            aniIndex = 1;
            aniTick = 0;
            return;
        }

        if (attacking) {
            state = ATTACK;
            if (last_action != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }

        }
        if (last_action != state) {
            aniIndex = 0;
            aniTick = 0;
        }
    }

    public void updateAnimationTick() {
        aniTick++;
        if (aniTick >= TICKS_PER_ANI) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    public void loadAnimation () {
        img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
    }
    public void loadLvlData (int[][] lvlData){
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }
    public void dead() {
        currentHealth = 0;

    }


    public void changeHealth (int value){
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    public void changePower(int value) {
        powerValue += value;
        if(powerValue >= powerMaxValue){
            powerValue = powerMaxValue;
        } else if(powerValue <= 0){
            powerValue = 0;
        }
    }


    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        isMoving = false;
        airSpeed = 0f;
        state = IDLE;
        currentHealth = maxHealth;

        hitBox.x = x;
        hitBox.y = y;

        resetAttackBox();

        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }

    public void resetAttackBox(){
        if(flipW == 1){
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        } else {
            attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
        }
    }
    public int getTileY() {
        return tileY;
    }


    public void powerAttack() {
        if(powerAttackActive){
            return;
        }
        if(powerValue >= 60){
            powerAttackActive = true;
            changePower(-60);
        }
    }
}
