package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager implements Disposable {

    private TextureAtlas atlas;
    private Map<String, Sound> soundMap;

    public ResourceManager() {
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/saun_and_enemies.atlas"));
        this.soundMap = new HashMap<>();
        loadSoundFX();
    }

    private void loadSoundFX() {
        this.soundMap.put("shaun_jump", Gdx.audio.newSound(Gdx.files.internal("sound/sound/jump-shaun.wav")));
    }

    public TextureAtlas.AtlasRegion loadRegion(String name, int index) {
        return atlas.findRegion(name, index);
    }

    public void playSound(String name) {
        this.soundMap.get(name).play();
    }

    @Override
    public void dispose() {
        atlas.dispose();
        soundMap.clear();
    }
}
