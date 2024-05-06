package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.*;

/**
 * ResourceManager - la clase que es responsable de manejar los recursos del juego: la música, los sonidos, los Sprites;
 * implementa Disposable.
 */
public class ResourceManager implements Disposable {

    private final TextureAtlas atlas;
    private final Map<String, Music> musicMap;
    private final Map<String, Sound> soundMap;
    private final Map<String, FileHandle> uiMap;

    public ResourceManager() {
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/shauns_friends_enemies_3.atlas"));
        this.musicMap = new HashMap<>();
        this.soundMap = new HashMap<>();
        this.uiMap = new HashMap<>();
        loadSoundFX();
        loadSplashImg();
    }

    private void loadSoundFX() {
        this.musicMap.put(MUSIC_COUNTRYSIDE, Gdx.audio.newMusic(Gdx.files.internal("sound/music/countryside.wav")));
        this.musicMap.put(MUSIC_LEVEL_END, Gdx.audio.newMusic(Gdx.files.internal("sound/music/level_end.mp3")));
        this.musicMap.put(MUSIC_SPACE, Gdx.audio.newMusic(Gdx.files.internal("sound/music/outerspace.wav")));
        this.musicMap.put(MUSIC_GAME_FINISHED, Gdx.audio.newMusic(Gdx.files.internal("sound/music/369252__funwithsound__victory-celebration-movie-score.wav")));

        this.soundMap.put(SOUND_BRICK_BREAK, Gdx.audio.newSound(Gdx.files.internal("sound/sound/brick_break.wav")));
        this.soundMap.put(SOUND_COIN, Gdx.audio.newSound(Gdx.files.internal("sound/sound/coin.wav")));
        this.soundMap.put(SOUND_ENEMY_DEATH, Gdx.audio.newSound(Gdx.files.internal("sound/sound/enemy-death.wav")));
        this.soundMap.put(SOUND_EXPLOSION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/explosion.wav")));
        this.soundMap.put(SOUND_FALLING_SHEEP_DEATH, Gdx.audio.newSound(Gdx.files.internal("sound/sound/falling-sheep-death.wav")));
        this.soundMap.put(SOUND_GAME_OVER, Gdx.audio.newSound(Gdx.files.internal("sound/sound/game_over.wav")));
        this.soundMap.put(SOUND_SHAUN_CELEBRATION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-celebration.wav")));
        this.soundMap.put(SOUND_SHAUN_DEATH_NOO, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-death-noo.wav")));
        this.soundMap.put(SOUND_SHAUN_PARALYSED, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-paralysed.wav")));
        this.soundMap.put(SOUND_SHAUN_JUMP, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shaun-jump.wav")));
        this.soundMap.put(SOUND_SHIRLEY_CELEBRATION, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shirley-celebration.wav")));
        this.soundMap.put(SOUND_SHIRLEY_DEATH_NOO, Gdx.audio.newSound(Gdx.files.internal("sound/sound/shirley-death-noo.wav")));
        this.soundMap.put(SOUND_SPLASH, Gdx.audio.newSound(Gdx.files.internal("sound/sound/splash.wav")));
        this.soundMap.put(SOUND_TIMMY_TRAMPOLINE, Gdx.audio.newSound(Gdx.files.internal("sound/sound/timmy-trampoline.wav")));
    }

    public void loadSplashImg() {
        this.uiMap.put(IMAGE_SPLASH, Gdx.files.internal("image/shaun_scapades_splash_screen.jpg"));
    }

    public FileHandle getSplashImg(String img) {
        return uiMap.get(img);
    }

    public TextureAtlas.AtlasRegion loadRegion(String name, int index) {
        return atlas.findRegion(name, index);
    }

    public void playMusic(String name, boolean looping) {
        if (ConfigManager.getGameSoundPref().equals("ON")) {
            this.musicMap.get(name).setLooping(looping);
            this.musicMap.get(name).play();
        }
    }

    public void playSound(String name) {
        if (ConfigManager.getGameSoundPref().equals("ON")) {
            this.soundMap.get(name).play();
        }
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