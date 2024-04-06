package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

public class Barrier extends TileObject {

    public Rectangle bounds;

    public Barrier(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        this.bounds = bounds;
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
    }

    @Override
    public void onContact(ResourceManager resourceManager, Hud hud) {
        Gdx.app.log("Barrier", "Barrier collision");
    }

}
