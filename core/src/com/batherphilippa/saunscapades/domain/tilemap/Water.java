package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.ResourceManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.WATER_BIT;

public class Water extends TileObject {
    public Water(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        // vincula los datos del usuario con el objeto sí mismo
        this.fixture.setUserData(this);
        setCategoryFilter(WATER_BIT);
    }

    @Override
    public void onContact(ResourceManager resourceManager) {
        resourceManager.stopMusic("countryside");
        resourceManager.playSound("splash");
        resourceManager.playSound("sheep_death");
    }
}