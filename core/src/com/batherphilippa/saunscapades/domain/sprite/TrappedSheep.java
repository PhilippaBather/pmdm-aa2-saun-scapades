package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.TRAPPED_SHEEP_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * TrappedSheep - define un objeto de una oveja atrapada; extiende Character
 */
public class TrappedSheep extends Character {

    private final TextureRegion shirleyIdle;
    private final Animation<TextureRegion> deadAnimation;
    private boolean isDead;

    private int stateTimer;

    public TrappedSheep(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.SHIRLEY);

        // establece el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // asocia la región de textura con el sprite
        this.shirleyIdle = region;
        this.setRegion(this.shirleyIdle);
        this.deadAnimation = setAnimationFrames("shirley_electrocute",0, 7, 0.5f);

        this.stateTimer = 0;
    }
    @Override
    public void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float delta) {
        this.stateTimer += delta;
        setRegion(getTextureRegion());
        setCenter(b2Body.getPosition().x, b2Body.getPosition().y);
        if (isDead) {

        }
    }

    /**
     * Devuele la región de la textura
     * @return la región de la textura
     */
    private TextureRegion getTextureRegion() {
        if (!isDead) {
            return shirleyIdle;
        } else {
            return deadAnimation.getKeyFrame(stateTimer, false);
        }
    }

    @Override
    protected void move(UserInput input) {

    }

    @Override
    public void resetState(SpriteState state) {
        if (state == SpriteState.DEAD) {
            isDead = true;
        }
    }

    @Override
    public void createHead(FixtureDef fixDef) {

    }

    @Override
    public short getCategoryBit() {
        return TRAPPED_SHEEP_BIT;
    }

    @Override
    public void dispose() {

    }
}
