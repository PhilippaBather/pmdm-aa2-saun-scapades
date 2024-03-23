package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class ResourceManager implements Disposable {

    private TextureAtlas atlas;

    public ResourceManager() {
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/saun_and_enemies.atlas"));
    }

    public TextureAtlas.AtlasRegion loadRegion(String name) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(name);
        return region;
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
