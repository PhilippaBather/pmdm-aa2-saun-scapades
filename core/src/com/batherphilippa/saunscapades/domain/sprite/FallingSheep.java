package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;

import static com.batherphilippa.saunscapades.domain.sprite.SpriteState.FALLING;
import static com.batherphilippa.saunscapades.domain.sprite.SpriteState.IDLE;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.FALLING_SHEEP_BIT;

public class FallingSheep extends ShirleySheep {

    final private TextureRegion groundedSheep;
    private final Animation<TextureRegion> fallingSheep;
    private SpriteState currState;
    private SpriteState prevState;
    private boolean hasLanded;
    private float stateTimer;

    public FallingSheep(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager);
        setRegion(region);

        this.currState = IDLE;
        this.groundedSheep = spriteManager.getTextureRegion("timmy_idle", -1);
        this.fallingSheep = setAnimationFrames("timmy_victory", 1, 14, 0.005f);

        this.stateTimer = 0;
        this.currState = SpriteState.IDLE;
        this.prevState = SpriteState.IDLE;
        this.hasLanded = false;

        b2Body.setActive(false);
    }

    @Override
    public short getCategoryBit() {
        return FALLING_SHEEP_BIT;
    }

    public boolean isHasLanded() {
        return !hasLanded;
    }

    public void setHasLanded(boolean hasLanded) {
        this.hasLanded = hasLanded;
    }

    @Override
    public void update(float dt) {
        b2Body.setActive(true);
        // as x and y coordinates are taken from the centre of the body
        this.setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currState = getSpritePositionState();
        TextureRegion region = getTextureRegion();
        stateTimer = currState == prevState ? stateTimer + dt : 0;
        prevState = currState;

        return region;
    }

    private SpriteState getSpritePositionState() {
       if (b2Body.getLinearVelocity().y < 0) {
           currState = FALLING;
           return FALLING;
        } else {
           return SpriteState.IDLE;
        }
    }

    private TextureRegion getTextureRegion() {
        return switch (currState) {
            case IDLE, DEAD, JUMPING, MOVING, PARALYSED, VICTORY -> groundedSheep;
            case FALLING -> fallingSheep.getKeyFrame(stateTimer, true);
        };
    }
}
