package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager implements Disposable {

    private final TextureAtlas atlas;
    private final Map<String, Music> musicMap;
    private final Map<String, Sound> soundMap;

    public ResourceManager() {
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/shaun_friends_enemies.atlas"));
        this.musicMap = new HashMap<>();
        this.soundMap = new HashMap<>();
        loadSoundFX();
    }

    private void loadSoundFX() {
        this.musicMap.put("countryside", Gdx.audio.newMusic(Gdx.files.internal("sound/music/countryside.wav")));
        this.soundMap.put("shaun_jump", Gdx.audio.newSound(Gdx.files.internal("sound/sound/jump-shaun.wav")));
        this.soundMap.put("coin", Gdx.audio.newSound(Gdx.files.internal("sound/sound/coin.wav")));
        this.soundMap.put("sheep_death", Gdx.audio.newSound(Gdx.files.internal("sound/sound/sheep-death.wav")));
        this.soundMap.put("splash", Gdx.audio.newSound(Gdx.files.internal("sound/sound/splash.wav")));
        this.soundMap.put("teleportdown", Gdx.audio.newSound(Gdx.files.internal("sound/sound/teleportdown.wav")));
    }

    public TextureAtlas.AtlasRegion loadRegion(String name, int index) {
        return atlas.findRegion(name, index);
    }

    public void playMusic(String name) {
        this.musicMap.get(name).setLooping(true);
        this.musicMap.get(name).play();
    }

    public void playMusic(String name, int delay) {
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                playMusic(name);
            }
        };

        Timer.instance().scheduleTask(task, delay);
    }
    public void playSound(String name) {
        this.soundMap.get(name).play();
    }

    public void stopMusic(String name) {
        this.musicMap.get(name).stop();
    }

    @Override
    public void dispose() {
        atlas.dispose();
        soundMap.clear();
    }
}