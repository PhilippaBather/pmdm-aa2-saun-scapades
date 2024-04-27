package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

/**
 * Ground - define un objeto de suelo de tipo Tile; extiende TileObject.
 */
public class Ground extends TileObject {
    private final ResourceManager resManager;
    public Ground(World world, TiledMap map, Rectangle bounds, ResourceManager resManager) {
        super(world, map, bounds);
        this.resManager = resManager;
    }

    @Override
    public void onContact() {

    }
}
