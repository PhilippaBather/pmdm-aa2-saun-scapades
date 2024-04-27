package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.BLOCK_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.DESTROYED_BIT;
import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.SOUND_BRICK_BREAK;

/**
 * Block - define un objeto de bloque de tipo Tile; ; extiende TileObject.
 */
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
        // cambia el bit para prevenir choques de futuro
        setCategoryFilter(DESTROYED_BIT);
        getCell(1).setTile(null);  // graphics layer
        resManager.playSound(SOUND_BRICK_BREAK);
    }

}
