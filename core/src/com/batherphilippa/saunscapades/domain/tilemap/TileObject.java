package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

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
        bodyDef.position.set(((bounds.getX() + bounds.getWidth() / 2) / PPM),  ((bounds.getY() + bounds.getHeight() / 2) / PPM));
        body = world.createBody(bodyDef);
        createFixture();
    }

    private void createFixture() {
        PolygonShape shape = new PolygonShape();
        FixtureDef fixDef = new FixtureDef();
        // dividir por 2 porque el 'fixture' empieza en el centro
        shape.setAsBox((bounds.getWidth() / 2) / PPM, (bounds.getHeight() / 2) / PPM);
        fixDef.shape = shape;
        this.fixture = body.createFixture(fixDef);
        shape.dispose();
    }
}


