package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if(!isSolid(x, y, lvlData))
            if(!isSolid(x + width, y + height, lvlData))
                if(!isSolid(x + width, y, lvlData))
                    if(!isSolid(x, y + height, lvlData))
                        return true;

        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x <= 0 || x >= maxWidth) {
            return true;
        }
        if (y <= 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        int xIndex = (int) (x / Game.TILES_SIZE);
        int yIndex = (int) (y / Game.TILES_SIZE);

        int spriteIndex = lvlData[yIndex][xIndex];
        if (spriteIndex != 11) {
            return true;
        }
        return false;
    }

    public static float GetCollisionGround(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if(xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset -1;
        } else {
            // Left
            return  currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetCollisionRoof(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int)hitbox.y / Game.TILES_SIZE;
        if(airSpeed > 0) {
            // Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos+ yOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottomleft and bottomright
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;

    }
}