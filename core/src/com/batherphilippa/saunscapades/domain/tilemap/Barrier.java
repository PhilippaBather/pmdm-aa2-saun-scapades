package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.OBJECT_BIT;

/**
 * Barrier - define un objeto de barrera de tipo Tile; extiende TileObject.
 */
public class Barrier extends TileObject {

    private final ResourceManager resManager;
    public Rectangle bounds;

    public Barrier(World world, TiledMap map, Rectangle bounds, ResourceManager resManager) {
        super(world, map, bounds);
        this.resManager = resManager;
        this.bounds = bounds;
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        // establece un bit para que un enemigo pueda chocar con una barrera
        setCategoryFilter(OBJECT_BIT);
    }

    @Override
    public void onContact() {
    }

}
