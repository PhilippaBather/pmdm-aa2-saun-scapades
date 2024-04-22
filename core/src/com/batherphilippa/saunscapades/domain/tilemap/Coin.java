package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.COIN_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.DESTROYED_BIT;

public class Coin extends TileObject {

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(COIN_BIT);
    }

    @Override
    public void onContact() {
        setCategoryFilter(DESTROYED_BIT);
        getCell().setTile(null);  // remove coin tile from the map
    }
}
