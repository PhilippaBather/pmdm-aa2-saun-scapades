package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.BOMB_BIT;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class Bomb extends Character implements Disposable {

    private final Animation<TextureRegion> bombBlast;
    private final TextureRegion bombIdle;
    private float stateTimer;
    private boolean isDetonated;
    private boolean isDestroyed;

    public Bomb(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.ENEMY);
        setBounds(getX(), getY(), 16 / PPM, 16 / PPM);
        this.bombIdle = region;
        this.setRegion(this.bombIdle);
        this.bombBlast = setAnimationFrames("explosion", 1, 2, 0.3f);

        initValues();
    }

    private void initValues() {
        this.stateTimer = 0;
        this.isDetonated = false;
        this.isDestroyed = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDestroyed || stateTimer < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        this.stateTimer += delta;
        setRegion(getTextureRegion());
        if (!isDetonated) {
            setCenter(b2Body.getPosition().x, b2Body.getPosition().y);
        } else if(!isDestroyed) {
            world.destroyBody(b2Body);
            isDestroyed = true;
            stateTimer = 0;
        }

    }

    private TextureRegion getTextureRegion() {
        if (!isDetonated) {
            return bombIdle;
        } else {
            return bombBlast.getKeyFrame(stateTimer, false);
        }
    }

    @Override
    protected void move(UserInput input) {

    }

    @Override
    public void resetState() {
        isDetonated = true;
    }

    @Override
    public void createHead(FixtureDef fixDef) {

    }

    @Override
    public short getCategoryBit() {
        return BOMB_BIT;
    }

    @Override
    public void dispose() {

    }
}
