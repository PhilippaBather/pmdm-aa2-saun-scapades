package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.BLOCK_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.DESTROYED_BIT;
import static com.batherphilippa.saunscapades.manager.constants.SoundResources.SOUND_BRICK_BREAK;

public class Block extends TileObject {
    private final ResourceManager resManager;


    public Block(World world, TiledMap map, Rectangle bounds, ResourceManager resManager) {
        super(world, map, bounds);
        this.resManager = resManager;

        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(BLOCK_BIT);

    }

    @Override
    public void onContact() {
        // prevent future collisions
        setCategoryFilter(DESTROYED_BIT);
        // remove coin tile from the map
        getCell(1).setTile(null);  // graphics layer
        resManager.playSound(SOUND_BRICK_BREAK);
    }

}
