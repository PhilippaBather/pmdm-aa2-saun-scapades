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
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.GRAVITY;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * B2WorldManager - maneja el mundo y la física de Box2D; implementa Disposable.
 */
public class B2WorldManager implements Disposable {
    private final ResourceManager resManager;
    private final World world;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer; // pinta la mapa a la pantalla

//    private final Box2DDebugRenderer b2dr;

    public B2WorldManager(GameScreen gameScreen, ResourceManager resManager, Hud hud) {
        this.resManager = resManager;

        this.world = new World(new Vector2(0, GRAVITY), true);
        this.world.setContactListener(new WorldContactListener(resManager, hud, gameScreen)); // para identificar colisiones

        // inicializa la mapa y cargar el primer nivel automáticamente
        this.map = loadMap();

        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
//        this.b2dr = new Box2DDebugRenderer();

        renderInteractiveObjects();
        renderUninterativeObjcts();
    }

    /**
     * Carga la TiledMap depende del nivel.
     * @return TiledMap
     */
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

    /**
     * Crea los objetos interactivos del mundo Box2D.
     */
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

    /**
     * Crea los objetos no interactivos del mundo Box2D.
     */
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

    /**
     * Crea el array de objetos de AngrySheep.
     * @param region - la región de la textura
     * @param spriteManager
     * @return un array de objetos AngrySheep
     */
    public Array<AngrySheep> renderAngrySheep(TextureRegion region, SpriteManager spriteManager) {
        Array<AngrySheep> angrySheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(ANGRY_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            angrySheepArr.add(new AngrySheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return angrySheepArr;
    }

    /**
     * Crea el array de objetos de Bombs.
     * @param region - la región de la textura
     * @param spriteManager
     * @return un array de objetos Bombs
     */
    public Array<Bomb> renderBombs(TextureRegion region, SpriteManager spriteManager) {
        Array<Bomb> bombArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(BOMBS_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bombArr.add(new Bomb(region, world, rect.getX(), rect.getY(), 6, spriteManager));
        }
        return bombArr;
    }

    /**
     * Crea el objeto Balloons para marcar el fin del nivel.
     * @param region - la región de la textura
     * @param spriteManager
     * @return Balloons
     */
    public Balloons renderLevelEndBalloons(TextureRegion region, SpriteManager spriteManager) {
        Array<RectangleMapObject> objArray = map.getLayers().get(SHIRLEY_SHEEP_TL).getObjects().getByType(RectangleMapObject.class);
        Rectangle rect = objArray.get(0).getRectangle();
        return new Balloons(region, world, rect.getX(), rect.getY(), 8, spriteManager);
    }

    /**
     * Crea el array de objetos de KamikazeSheep.
     * @param region - la región de la textura
     * @param spriteManager
     * @return un array de objetos KamikazeSheep
     */
    public Array<KamikazeSheep> renderFallingSheep(TextureRegion region, SpriteManager spriteManager) {
        Array<KamikazeSheep> fallingSheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(FALLING_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            fallingSheepArr.add(new KamikazeSheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return fallingSheepArr;
    }

    /**
     * Crea el array de objetos de TrappedSheep.
     * @param region - la región de la textura
     * @param spriteManager
     * @return un array de objetos TrappedSheep
     */
    public Array<TrappedSheep> renderTrappedSheep(TextureRegion region, SpriteManager spriteManager) {
        Array<TrappedSheep> trappedSheepArr = new Array<>();
        for (RectangleMapObject object : map.getLayers().get(TRAPPED_SHEEP_TL).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            trappedSheepArr.add(new TrappedSheep(region, world, rect.getX(), rect.getY(), 9, spriteManager));
        }
        return trappedSheepArr;
    }

    /**
     * Renderiza el Tiled Map
     * @param combined
     */
    public void renderTiledMap(Matrix4 combined) {
        renderer.render();
        // render our Box2DDebugLines; camera.combined = projection matrix for the game
//        b2dr.render(world, combined);
    }

    /**
     * Actualiza el mundo de Box2D y el TiledMap
     * @param camera
     */
    public void update(OrthographicCamera camera) {
        // indica a Box2D cuántas veces calcular por segundo para ejecutar la simulación física
        // las iteraciones de velocidad afectan cómo reaccionan dos cuerpos durante una colisión
        // los números altos son los que más tiempo lleva hacer los cálculos, pero más precisos
        this.world.step(1 / 60f, 6, 2);   // time stamp: 1/60f = 60 times a second
        renderTiledMap(camera.combined);
        setTiledMapCameraView(camera);
    }

    /**
     * Establece la vista de la camera del TiledMap
     * @param camera
     */
    private void setTiledMapCameraView(OrthographicCamera camera) {
        renderer.setView(camera);
    }

    @Override
    public void dispose() {
//        b2dr.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
    }
}
