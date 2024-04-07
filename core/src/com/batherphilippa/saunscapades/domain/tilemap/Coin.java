package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.COIN_BIT;

public class Coin extends TileObject {

    private final int BLANK_COIN = 28;

    private final TiledMapTileSet tileset;

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

        tileset = map.getTileSets().getTileSet("tileset_mbros");
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(COIN_BIT);
    }

    @Override
    public void onContact(ResourceManager resourceManager, Hud hud) {
        getCell().setTile(null);  // remove coin tile from the map
        hud.updateScore(250);
        resourceManager.playSound("coin");
    }
}
