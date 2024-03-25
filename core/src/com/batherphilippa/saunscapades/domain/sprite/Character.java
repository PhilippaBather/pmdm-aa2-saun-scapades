package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

import static com.batherphilippa.saunscapades.util.Constants.PPM;

public abstract class Character extends Sprite implements Disposable {

    protected World world;
    protected Body b2Body;

    public Character(World world, float x, float y, float radius) {
        this.world = world;
        initB2Body(x, y, radius);
    }

    private void initB2Body(float x, float y, float radius) {
        createBody(x, y);
        createFixture(radius);
//        createHead();
    }

    /**
     * Define el 'B2 Body' y crealo en el mundo.
     */
    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / PPM, y / PPM);  // TODO - remove temporary
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.b2Body = world.createBody(bodyDef);
    }

    /**
     * Añade una forma al B2 body para detectar choques.
     */
    private void createFixture(float radius) {
        FixtureDef fixDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);  // TODO - remove temporary - add a float radius as param in the method definition
        fixDef.shape = shape;
        this.b2Body.createFixture(fixDef);
        shape.dispose();
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

    public abstract void move();

}
