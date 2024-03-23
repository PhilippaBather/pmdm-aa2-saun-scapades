package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.util.Constants.PPM;

public abstract class Character extends Sprite implements Disposable {

    protected World world;
    protected Body b2Body;
    protected TextureRegion region;

    public Character(TextureRegion region, World world, float x, float y, float radius) {
        this.world = world;
        this.region = region;
        setPosition(x / PPM, y / PPM);
        initB2Body(x, y, radius);
    }

    private void initB2Body(float x, float y, float radius) {
        createBody(x, y);
        createFixture(radius);
//        createHead();
    }

    public Body getB2Body() {
        return b2Body;
    }

    /**
     * Define el 'B2 Body' y crealo en el mundo.
     */
    private void createBody(float x, float y) {
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
        fixDef.shape = shape;
        this.b2Body.createFixture(fixDef);
        shape.dispose();
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

    public abstract void move(UserInput input);

}
