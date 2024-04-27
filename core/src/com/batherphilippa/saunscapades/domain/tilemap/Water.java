package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.WATER_BIT;
import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.SOUND_SPLASH;

/**
 * Block - define un objeto de agua de tipo Tile; extiende TileObject.
 */
public class Water extends TileObject {
    private final ResourceManager resManager;
    public Water(World world, TiledMap map, Rectangle bounds, ResourceManager resManager) {
        super(world, map, bounds);
        this.resManager = resManager;
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(WATER_BIT);
    }

    @Override
    public void onContact() {
        resManager.playSound(SOUND_SPLASH);
    }
}