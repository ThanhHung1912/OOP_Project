package utilz;

public class Constant {
    public static class Directions {
        public static final int UP = 0;
        public static final int LEFT = 1;
        public static final int DOWN = 2;
        public static final int RIGHT = 3;
    }
    public static class PlayerConstants {
        public static final int ATTACK = 0;
        public static final int DEAD = 1;
        public static final int DOOR_IN = 2;
        public static final int DOOR_OUT = 3;
        public static final int FALL = 4;

        public static final int GROUND = 5;
        public static final int HIT = 6;
        public static final int IDLE = 7;
        public static final int JUMP = 8;
        public static final int RUN = 9;

        public static int GetSpriteAmount(int player_action) {
            return switch (player_action) {
                case IDLE -> 11;
                case DOOR_IN, DOOR_OUT, RUN -> 8;
                case ATTACK -> 3;
                case FALL, GROUND, JUMP -> 1;
                case DEAD -> 4;
                case HIT -> 2;
                default -> 1;
            };
        }
    }

}
