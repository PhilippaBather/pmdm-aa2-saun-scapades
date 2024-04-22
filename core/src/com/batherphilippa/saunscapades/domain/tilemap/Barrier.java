package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.OBJECT_BIT;

public class Barrier extends TileObject {

    public Rectangle bounds;

    public Barrier(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        this.bounds = bounds;
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        // establece un bit para que un enemigo pueda chocar con una barrera
        setCategoryFilter(OBJECT_BIT);
    }

    @Override
    public void onContact() {
        Gdx.app.log("Barrier", "Barrier collision");
    }

}
