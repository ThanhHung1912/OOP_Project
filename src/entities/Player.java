package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static main.Game.UPS;
import static utilz.Constant.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int[][] lvlData;
    private boolean up, left, down, right, jump;
    private int player_action = IDLE;
    private float playerSpeed = 1.0f;
    private boolean isMoving = false;
    private int aniTick = 0, aniIndex = 0, aniPerSecond = 10;

    private float xDrawOffset = 22 * SCALE;
    private float yDrawOffset = 4 * SCALE;


    // Applying Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * SCALE;
    private float jumpSpeed = -2.25f * SCALE;
    private float gravityAcceleration = 0.5f * SCALE;
    private boolean inAir = false;


    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        initHitBox(x, y, 20*SCALE, 28*SCALE);
        loadAnimation();
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirBooleans() {
        up = false;
        left = false;
        down = false;
        right = false;
    }
    public void setPlayer_action(int player_action) {
        this.player_action = player_action;
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        updateAnimation();
    }
    public void render(Graphics g) {
        g.drawImage(animations[player_action][aniIndex], (int) (hitBox.x - xDrawOffset), (int) (hitBox.y - yDrawOffset), width, height, null);
        drawHitBox(g);
    }

    public void updatePos() {
        isMoving = false;
        int xSpeed = 0;

        if(jump) {
            jump();
        }
        if (!left&&!right&&!inAir)
            return;
        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;


        if(!inAir) // Player can jump if not in air
            if (!IsEntityOnFloor(hitBox, lvlData))
                inAir = true;

        if(inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else{
                hitBox.y = GetCollisionRoof(hitBox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else {
                    airSpeed = gravityAcceleration;
                }
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
        }
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void updateAnimation() {
        int last_action = player_action;
        if (isMoving) {
            player_action = RUNNING;
        }
        else {
            player_action = IDLE;
        }

        if (inAir) {
            if(airSpeed <0) {
                player_action = JUMP;
            } else {
                player_action = FALLING;
            }
        }

        if (last_action != player_action) {
            aniTick = 0;
            aniIndex = 0;
        }
    }

    public void updateAnimationTick() {
        aniTick++;
        if (aniTick >= UPS / aniPerSecond) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex >= GetSpriteAmount(player_action)) {
                aniIndex = 0;
            }
        }
    }

    public void loadAnimation() {
        img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
            }
        }
    }
    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
    }
}
