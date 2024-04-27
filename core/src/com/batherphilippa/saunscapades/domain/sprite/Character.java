package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.*;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * Character - clase abstracta que extiende Sprite y implementa Disposable.
 * Proviene el modelo para los Sprites: la textura, las animaciones, el B2Body, y actualizaciones y control de estados.
 */
public abstract class Character extends Sprite implements Disposable {

    protected World world;
    protected Body b2Body;
    protected TextureRegion region;
    protected SpriteManager spriteManager;
    protected SpriteType spriteType;
    public Character(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager, SpriteType spriteType) {
        this.world = world;
        this.region = region;
        this.spriteManager = spriteManager;
        this.spriteType = spriteType;
        setPosition(x / PPM, y / PPM);
        initB2Body(x, y, radius);
    }

    /**
     * Inicializa el B2Body
     * @param x - coordinada x
     * @param y - coordinada y
     * @param radius - radio del cuerpo
     */
    private void initB2Body(float x, float y, float radius) {
        createBody(x, y);
        createFixture(radius);
    }

    /**
     * Devuelve el B2 Body.
     * @return - Body B2
     */
    public Body getB2Body() {
        return b2Body;
    }

    /**
     * Define el 'B2 Body' y crealo en el mundo B2.
     */
    public void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.b2Body = world.createBody(bodyDef);
    }

    /**
     * Añade una forma al B2 body para detectar choques.
     */
    private void createFixture(float radius) {
        FixtureDef fixDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        // establece los bits de catagoría
        fixDef.filter.categoryBits = getCategoryBit();

        // establece los filtros de choques
        createFilterCollisions(fixDef);

        // crea el 'fixture'
        fixDef.shape = shape;
        this.b2Body.createFixture(fixDef).setUserData(this);
        createHead(fixDef);
        shape.dispose();
    }

    /**
     * Define los filtros de choques de un B2Body
     * @param fixDef - fixture definition
     */
    private void createFilterCollisions(FixtureDef fixDef) {
        if (spriteType == SpriteType.PLAYER) {
            // con lo que shaun puede chocar
            fixDef.filter.maskBits = GROUND_BIT | COIN_BIT | WATER_BIT | ENEMY_BIT | ENEMY_HEAD_BIT | OBJECT_BIT | BOMB_BIT | BALLOON_BIT | TRAPPED_SHEEP_BIT | BLOCK_BIT | KAMIKAZE_SHEEP_BIT; // shaun
        } else {
            // con lo que un enemigo o otra sprite pueden chocar
            fixDef.filter.maskBits = GROUND_BIT | SHAUN_BIT | SHAUN_HEAD_BIT | COIN_BIT | ENEMY_BIT | OBJECT_BIT | BOMB_BIT | BLOCK_BIT | KAMIKAZE_SHEEP_BIT | TRAPPED_SHEEP_BIT;
        }
    }

    /**
     * Establece los fotogramas de la animación
     * @param regionName - nombre de región de la textura
     * @param regionStart - la empieza de la región
     * @param regionEnd - el fin de la región
     * @param frameDuration - la duración del fotograma
     * @return
     */
    protected Animation<TextureRegion> setAnimationFrames(String regionName, int regionStart, int regionEnd, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();

        for (int i = regionStart; i < regionEnd; i++) {
            frames.add(new TextureRegion(spriteManager.getTextureRegion(regionName, i)));
        }

        return new Animation<>(frameDuration, frames);
    }

    protected abstract void render(SpriteBatch batch);

    protected abstract void update(float delta);

    protected abstract void move(UserInput input);

    public abstract void resetState(SpriteState state);

    public abstract void createHead(FixtureDef fixDef);

    public abstract short getCategoryBit();

    @Override
    public void dispose() {
        world.dispose();
        spriteManager.dispose();
    }
}
