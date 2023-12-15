package entities;

import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static main.Game.UPS;
import static utilz.Constant.PlayerConstants.*;

public class Player extends Enity{
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int width = 78;
    private int height = 58;
    private final int SCALE = 2;
    private boolean up, left, down, right;
    private int player_action = IDLE;
    private float playerSpeed = 1.0f;
    private boolean isMoving = false;
    private int aniTick = 0, aniIndex = 0, aniPerSecond = 10;

    public Player(int x, int y) {
        super(x, y);
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
        g.drawImage(animations[player_action][aniIndex], (int) x, (int) y, SCALE*78, SCALE*58, null);
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
            player_action = RUN;
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
        animations = new BufferedImage[10][11];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * width, i * height, width, height);
            }
        }
    }
}
