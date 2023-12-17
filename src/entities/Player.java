package entities;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.UPS;
import static utilz.Constant.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage img;
    private BufferedImage[][] animations;
    private boolean up, left, down, right;
    private int player_action = IDLE;
    private float playerSpeed = 1.0f;
    private boolean isMoving = false;
    private int aniTick = 0, aniIndex = 0, aniPerSecond = 6;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
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
        g.drawImage(animations[player_action][aniIndex], (int) x, (int) y, width, height, null);
    }

    public void updatePos() {
        isMoving = false;
        if (left && !right) {
            x -= playerSpeed;
            isMoving = true;
        } else if (right && !left) {
            x += playerSpeed;
            isMoving = true;
        }
        if (up && !down) {
            y -= playerSpeed;
            isMoving = true;
        } else if (down && !up) {
            y += playerSpeed;
            isMoving = true;
        }
    }
    public void updateAnimation() {
        int last_action = player_action;
        if (isMoving) {
            player_action = RUNNING;
        }
        else {
            player_action = IDLE;
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
}
