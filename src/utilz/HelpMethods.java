package utilz;

import main.Game;

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
        if (x <= 0 || x >= Game.GAME_WIDTH) {
            return true;
        }
        if (y <= 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        int xIndex = (int) x / Game.TILES_SIZE;
        int yIndex = (int) y / Game.TILES_SIZE;

        int spriteIndex = lvlData[yIndex][xIndex];
        if (spriteIndex != 11) {
            return true;
        }
        return false;
    }
}
