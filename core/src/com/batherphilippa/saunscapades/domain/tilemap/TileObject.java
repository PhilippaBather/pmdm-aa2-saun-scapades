package com.batherphilippa.saunscapades.domain.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * TileObject - define un objeto de azulejo en el TiledMap.
 */
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

    /**
     * Crea el B2Body.
     */
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

    /**
     * Establece el bit de categoría.
     * @param filterBit
     */
    protected void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    /**
     * Devuelve la celda de la mapa asociado con el objeto.
     * @param layerLevel - el nivel de la mapa
     * @return
     */
    protected TiledMapTileLayer.Cell getCell(int layerLevel) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerLevel);
        // aumenta a escala como se redujo anteriormente para que parezca idéntico al TiledMap
        //luego divida por el tamaño del mosaico (16) para obtener el valor x/y para la ubicación de la celda
        return layer.getCell(
                (int) (body.getPosition().x * PPM / 16),
                (int) (body.getPosition().y * PPM / 16));
    }

    public abstract void onContact();

}


