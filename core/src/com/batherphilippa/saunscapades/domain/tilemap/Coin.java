package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.COIN_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.DESTROYED_BIT;
import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.SOUND_COIN;
import static com.batherphilippa.saunscapades.manager.constants.TileLayers.GRAPHICS_ORNAMENTS;

/**
 * Coin - define el un objeto de moneda de tipo Tile; extiende TileObject.
 */
public class Coin extends TileObject {
    private final ResourceManager resManager;

    public Coin(World world, TiledMap map, Rectangle bounds, ResourceManager resManager) {
        super(world, map, bounds);
        this.resManager = resManager;

        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(COIN_BIT);
    }

    @Override
    public void onContact() {
        // cambia el bit para prevenir choques de futuro
        setCategoryFilter(DESTROYED_BIT);
        // elimina el azulejo de una moneda desde la mapa
        getCell(GRAPHICS_ORNAMENTS).setTile(null);// graphic_ornaments layer
        resManager.playSound(SOUND_COIN);
    }
}
