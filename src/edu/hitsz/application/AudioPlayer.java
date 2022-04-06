package edu.hitsz.application;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author JerryYang
 */
public class AudioPlayer {
    private Player player;
    private BufferedInputStream name;

    public AudioPlayer(String path) throws FileNotFoundException {
        name = new BufferedInputStream((new FileInputStream(System.getProperty("user.dir") + path)));
    }

    public static void main(String[] args) throws FileNotFoundException, JavaLayerException {
        AudioPlayer audioPlayer = new AudioPlayer("/src/audio/bgm.mp3");
        audioPlayer.playAudio();
    }

    public void playAudio() throws JavaLayerException {
        player = new Player(name);
        player.play();
    }
}
