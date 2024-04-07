package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.domain.sprite.SpriteType.PLAYER;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class Shaun extends Character {

    private SpritePositionState currState;
    private SpritePositionState prevState;
    private final Animation<TextureRegion> shaunMove;

    private final TextureRegion shaunJump;
    private final TextureRegion shaunIdle;
    private float stateTimer;
    private boolean isDirRight;

    public Shaun(TextureAtlas.AtlasRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, PLAYER);

        this.shaunIdle = region;

        // establecer el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // asociar la región de textura con el sprite
        this.setRegion(shaunIdle);

        setState();

        shaunJump = spriteManager.getTextureRegion("shaun_jump_up", 4);
        shaunMove = setAnimationFrames("shaun_walk", 0, 7, 0.1f);

    }

    private void setState() {
        currState = SpritePositionState.IDLE;
        prevState = SpritePositionState.IDLE;
        stateTimer = 0;
        isDirRight = true;
    }

    @Override
    protected void createHead() {
        FixtureDef fixDef = new FixtureDef();
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-4 / PPM, 8 / PPM), new Vector2(4 / PPM, 8 / PPM));
        fixDef.shape = head;
        // sensors provide info available for polling
        fixDef.isSensor = true;
        b2Body.createFixture(fixDef).setUserData("head"); // para identificar este 'fixture' como la cabeza de shaun
        head.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float dt) {
        // as x and y coordinates are taken from the centre of the body
        this.setPosition((b2Body.getPosition().x - getWidth() / 2), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currState = getSpritePositionState();
        TextureRegion region = getTextureRegion();

        if ((b2Body.getLinearVelocity().x < 0 || !isDirRight) && !region.isFlipX()) {
            region.flip(true, false);
            isDirRight = false;
        } else if ((b2Body.getLinearVelocity().x > 0 || isDirRight) && region.isFlipX()) {
            region.flip(true, false);
            isDirRight = true;
        }

        stateTimer = currState == prevState ? stateTimer + dt : 0;
        prevState = currState;

        return region;
    }

    /**
     * Coge el estado de la posición del Sprite basado en lo que el B2Body está haciendo.
     *
     * @return SpritePositionState - el estado de la actividad del Sprite
     */
    public SpritePositionState getSpritePositionState() {
        if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && prevState == SpritePositionState.JUMPING)) {
            return SpritePositionState.JUMPING;
        } else if (b2Body.getLinearVelocity().y < 0) {
            return SpritePositionState.FALLING;
        } else if (b2Body.getLinearVelocity().x != 0) {
            return SpritePositionState.MOVING;
        } else {
            return SpritePositionState.IDLE;
        }
    }

    private TextureRegion getTextureRegion() {
        return switch (currState) {
            case MOVING -> shaunMove.getKeyFrame(stateTimer, true);
            case JUMPING -> shaunJump;
            case IDLE, FALLING -> shaunIdle;
        };
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
            case RIGHT -> this.getB2Body().applyLinearImpulse(new Vector2(0.1f, 0),
                    this.getB2Body().getWorldCenter(), true);
            case LEFT -> this.getB2Body().applyLinearImpulse(new Vector2(-0.1f, 0),
                    this.getB2Body().getWorldCenter(), true);

        }
    }

    @Override
    public void dispose() {
    }
}
