package main;

public class MainClass {
    public static void main(String[] args) {
        Game game = new Game();
        GamePanel gamePanel = GamePanel.getGamePanel(game);
        GameWindow gameWindow = GameWindow.getGameWindow(gamePanel);
    }
}
