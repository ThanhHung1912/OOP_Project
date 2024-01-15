package main;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        Game game = Game.getInstance();
    }
}
