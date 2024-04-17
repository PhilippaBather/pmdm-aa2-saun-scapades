package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.util.Constants.PPM;

public abstract class TileObject {

    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public TileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(((bounds.getX() + bounds.getWidth() / 2) / PPM), ((bounds.getY() + bounds.getHeight() / 2) / PPM));
        body = world.createBody(bodyDef);
        createFixture();
    }

    /**
     * Fixture que rodea el objeto.
     */
    private void createFixture() {
        PolygonShape shape = new PolygonShape();
        FixtureDef fixDef = new FixtureDef();
        // dividir por 2 porque el 'fixture' empieza en el centro
        shape.setAsBox((bounds.getWidth() / 2) / PPM, (bounds.getHeight() / 2) / PPM);
        fixDef.shape = shape;
        this.fixture = body.createFixture(fixDef);
        shape.dispose();
    }

    protected void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    protected TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2); // graphic_ornaments layer
        // scale up as previously scaled down so looks identical to the tiled map
        // then divide by tile size (16) to get x/y value for cell's location
        return layer.getCell(
                (int) (body.getPosition().x * PPM / 16),
                (int) (body.getPosition().y * PPM / 16));
    }

    public abstract void onContact(ResourceManager resourceManager);

}


