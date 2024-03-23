package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.util.UserInput;

import javax.print.attribute.standard.PagesPerMinute;

import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class Shaun extends Character {

    private final TextureRegion shaunIdle;

    public Shaun(TextureAtlas.AtlasRegion region, World world, float x, float y, float radius) {
        super(region, world, x, y, radius);

        this.shaunIdle = region;

        // establecer el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 /PPM );

        // asociar la región de textura con el sprite
        this.setRegion(shaunIdle);

    }

    @Override
    public void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float dt) {
        // as x and y coordinates are taken from the centre of the body
        this.setPosition((b2Body.getPosition().x - getWidth() / 2), b2Body.getPosition().y - getHeight() / 2);
    }

    public float getLinearVelocity() {
        return this.getB2Body().getLinearVelocity().x;
    }

    @Override
    public void move(UserInput input) {
        switch (input) {
            // use a force = gradual increase/decrease in speed or use an impulse, which is an immediate change
            // 0 on x as jumping up; get world centre is where on the body you want to apply the force
            // if off centre, they'll be a torque to the body, which would change the angle
            // wake object: yes (if body is asleep)
            case UP -> this.getB2Body().applyLinearImpulse(new Vector2(0, 4f),
                    this.getB2Body().getWorldCenter(), true);
            case RIGHT ->
                    this.getB2Body().applyLinearImpulse(new Vector2(0.1f, 0),
                            this.getB2Body().getWorldCenter(), true);
            case LEFT ->
                    this.getB2Body().applyLinearImpulse(new Vector2(-0.1f, 0),
                            this.getB2Body().getWorldCenter(), true);

        }
    }

    @Override
    public void dispose() {

    }
}
