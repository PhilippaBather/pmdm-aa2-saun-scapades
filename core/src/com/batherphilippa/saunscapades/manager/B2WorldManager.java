package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.tilemap.Barrier;
import com.batherphilippa.saunscapades.domain.tilemap.Coin;
import com.batherphilippa.saunscapades.domain.tilemap.Ground;

import static com.batherphilippa.saunscapades.util.Constants.GRAVITY;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class B2WorldManager implements Disposable {
    private final SaunScapades game;
    private final World world;
    private final TmxMapLoader mapLoader;  // TODO - don't convert to local var - to be used in setMap function for second level
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer; // renders map to screen

    private final Box2DDebugRenderer b2dr;

    public B2WorldManager(SaunScapades game) {
        this.game = game;

        this.world = new World(new Vector2(0, GRAVITY), true);

        // inicializar la mapa y cargar el primer nivel automáticamente
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("tileset/map_level_1.tmx");

        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        this.b2dr = new Box2DDebugRenderer();

        renderInteractiveObjects();
        renderUninterativeObjcts();

    }

    public World getWorld() {
        return world;
    }

    private void renderInteractiveObjects() {
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new Coin(world, map, rect);
        }
    }

    private void renderUninterativeObjcts() {
        for(RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = object.getRectangle();

            new Ground(world, map, rect);
        }

        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new Barrier(world, map, rect);
        }

    }

    public void renderTiledMap(Matrix4 combined) {
        renderer.render();
        // render our Box2DDebugLines; camera.combined = projection matrix for the game
        b2dr.render(world, combined);
    }
    public void update(OrthographicCamera camera) {
        // tell Box2D how many times to calc per second in order to execute physics simulation
        // velocity iterations affects how two bodies react during a collision,
        // high numbers are the longer it takes to do the calculations but the more precise
        this.world.step(1 / 60f, 6, 2);   // time stamp: 1/60f = 60 times a second
        renderTiledMap(camera.combined);
        setTiledMapCameraView(camera);
    }

    private void setTiledMapCameraView(OrthographicCamera camera) {
        renderer.setView(camera);
    }

    // TODO - method to set other tiled maps for other levels
    public void setTiledMap(TiledMap map) {}

    @Override
    public void dispose() {
        b2dr.dispose();
        game.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
    }
}
