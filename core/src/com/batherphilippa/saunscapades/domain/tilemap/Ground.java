package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


public class Ground extends TileObject {
    public Ground(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

    @Override
    public void onContact() {
        Gdx.app.log("Ground", "Ground collision");
    }
}
