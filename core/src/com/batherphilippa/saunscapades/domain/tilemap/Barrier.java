package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Barrier extends TileObject {

    public Rectangle bounds;

    public Barrier(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        this.bounds = bounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
