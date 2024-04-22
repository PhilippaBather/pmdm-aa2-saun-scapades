package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.domain.sprite.*;
import com.batherphilippa.saunscapades.domain.tilemap.*;
import com.batherphilippa.saunscapades.listener.WorldContactListener;
import com.batherphilippa.saunscapades.screen.GameLevel;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.ShaunScapades.currGameLevel;
import static com.batherphilippa.saunscapades.manager.constants.TileLayers.*;
import static com.batherphilippa.saunscapades.util.Constants.GRAVITY;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class B2WorldManager implements Disposable {
    private final ResourceManager resManager;
    private final World world;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer; // renders map to screen

    private final Box2DDebugRenderer b2dr;

    public B2WorldManager(GameScreen gameScreen, ResourceManager resManager, Hud hud) {
        this.resManager = resManager;

        this.world = new World(new Vector2(0, GRAVITY), true);
        this.world.setContactListener(new WorldContactListener(resManager, hud, gameScreen)); // para identificar colisiones

        // inicializar la mapa y cargar el primer nivel automáticamente
        this.map = loadMap();

        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        this.b2dr = new Box2DDebugRenderer();

        renderInteractiveObjects();
        renderUninterativeObjcts();
    }

    private TiledMap loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();

        String level = switch (currGameLevel) {
            case LEVEL_1 -> "tileset/map_level_1.tmx";
            case LEVEL_2 -> "tileset/map_level_2.tmx";
        };
        return mapLoader.load(level);
    }

    public World getWorld() {
        return world;
    }

    private void renderInteractiveObjects() {
        for (RectangleMapObject object : map.getLayers().get(COINS_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Coin(world, map, rect, resManager);
        }

        for (RectangleMapObject object : map.getLayers().get(WATER_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Water(world, map, rect, resManager);
        }

        if (currGameLevel == GameLevel.LEVEL_2) {
            for (RectangleMapObject object : map.getLayers().get(BLOCKS_TL).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = object.getRectangle();

                new Block(world, map, rect, resManager);
            }
        }
    }

    private void renderUninterativeObjcts() {
        for (RectangleMapObject object : map.getLayers().get(GROUND_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new Ground(world, map, rect, resManager);
        }

        for (RectangleMapObject object : map.getLayers().get(BARRIERS_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new Barrier(world, map, rect, resManager);

        }
    }

    public Array<AngrySheep> renderAngrySheep(TextureRegion region, SpriteManager spriteManager) {
        Array<AngrySheep> angrySheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(ANGRY_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            angrySheepArr.add(new AngrySheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return angrySheepArr;
    }

    public Array<Bomb> renderBombs(TextureRegion region, SpriteManager spriteManager) {
        Array<Bomb> bombArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(BOMBS_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bombArr.add(new Bomb(region, world, rect.getX(), rect.getY(), 6, spriteManager));
        }
        return bombArr;
    }

    public ShirleySheep renderLevelEndSheep(TextureRegion region, SpriteManager spriteManager) {
        Array<RectangleMapObject> objArray = map.getLayers().get(SHIRLEY_SHEEP_TL).getObjects().getByType(RectangleMapObject.class);
        Rectangle rect = objArray.get(0).getRectangle();
        return new ShirleySheep(region, world, rect.getX(), rect.getY(), 8, spriteManager);
    }

    public Array<FallingSheep> renderFallingSheep(TextureRegion region, SpriteManager spriteManager) {
        Array<FallingSheep> fallingSheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(FALLING_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            fallingSheepArr.add(new FallingSheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return fallingSheepArr;
    }
    public Array<TrappedSheep> renderTrappedSheep(TextureRegion region, SpriteManager spriteManager) {
        Array<TrappedSheep> trappedSheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(TRAPPED_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            trappedSheepArr.add(new TrappedSheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return trappedSheepArr;
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

    @Override
    public void dispose() {
        b2dr.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
    }
}
