package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.Map;

import static com.batherphilippa.saunscapades.manager.constants.SoundResources.*;

public class ResourceManager implements Disposable {

    private final TextureAtlas atlas;
    private final Map<String, Music> musicMap;
    private final Map<String, Sound> soundMap;

    public ResourceManager() {
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/shaun_friends_enemies_2.atlas"));
        this.musicMap = new HashMap<>();
        this.soundMap = new HashMap<>();
        loadSoundFX();
    }

    private void loadSoundFX() {
        this.musicMap.put(MUSIC_COUNTRYSIDE, Gdx.audio.newMusic(Gdx.files.internal("sound/music/countryside.wav")));
        this.musicMap.put(MUSIC_LEVEL_END, Gdx.audio.newMusic(Gdx.files.internal("sound/music/level_end.mp3")));
        this.musicMap.put(MUSIC_SPACE, Gdx.audio.newMusic(Gdx.files.internal("sound/music/outerspace.wav")));

        this.soundMap.put(SOUND_BRICK_BREAK, Gdx.audio.newSound(Gdx.files.internal("sound/sound/brick_break.wav")));
        this.soundMap.put(SOUND_COIN, Gdx.audio.newSound(Gdx.files.internal("sound/sound/coin.wav")));
        this.soundMap.put(SOUND_ENEMY_DEATH, Gdx.audio.newSound(Gdx.files.internal("sound/sound/enemy-death.wav")));
        this.soundMap.put(SOUND_EXPLOSION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/explosion.wav")));
        this.soundMap.put(SOUND_GAME_OVER, Gdx.audio.newSound(Gdx.files.internal("sound/sound/game_over.wav")));
        this.soundMap.put(SOUND_SHAUN_CELEBRATION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-celebration.wav")));
        this.soundMap.put(SOUND_SHAUN_DEATH_NOO, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-death-noo.wav")));
        this.soundMap.put(SOUND_SHAUN_PARALYSED, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-paralysed.wav")));
        this.soundMap.put(SOUND_SHAUN_JUMP, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-jump.wav")));
        this.soundMap.put(SOUND_SHIRLEY_CELEBRATION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shirley-celebration.wav")));
        this.soundMap.put(SOUND_SHIRLEY_DEATH_NOO, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shirley-death-noo.wav")));
        this.soundMap.put(SOUND_SPLASH, Gdx.audio.newSound(Gdx.files.internal("sound/sound/splash.wav")));
        this.soundMap.put(SOUND_TELEPORT_DOWN, Gdx.audio.newSound(Gdx.files.internal("sound/sound/teleport-down.wav")));
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

    public void playSound(String name, int delay) {
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                playSound(name);
            }
        };

        Timer.instance().scheduleTask(task, delay);
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