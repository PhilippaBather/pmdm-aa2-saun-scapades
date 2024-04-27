package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.GameLevel;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.BOMB_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * Bomb - la clase rerpesenta una bomba; extiende Character y implementa Disposable.
 */
public class Bomb extends Character implements Disposable {

    private final Animation<TextureRegion> bombBlast;
    private final TextureRegion bombIdle;
    private float stateTimer;
    private boolean isDetonated;
    private boolean isDestroyed;
    private float timeCount;
    private int detonationTime;

    public Bomb(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.ENEMY);

        // establece el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // asocia la región de textura con el sprite
        this.bombIdle = region;
        this.setRegion(this.bombIdle);
        this.bombBlast = setAnimationFrames("explosion", 1, 3, 0.1f);

        initValues();
        b2Body.setActive(false);
    }

    /**
     * Establece los valores iniciales.
     */
    private void initValues() {
        this.stateTimer = 0;
        this.isDetonated = false;
        this.isDestroyed = false;

        if (ShaunScapades.currGameLevel == GameLevel.LEVEL_2) {
            this.timeCount = 0;
            this.detonationTime = 10;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDestroyed || stateTimer < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        b2Body.setActive(true);

        if (ShaunScapades.currGameLevel == GameLevel.LEVEL_2) {
            levelTwoUpdates(delta);
        }

        this.stateTimer += delta;
        setRegion(getTextureRegion());

        if (!isDetonated) {
            // si no ha detonado
            setCenter(b2Body.getPosition().x, b2Body.getPosition().y);
        } else if (!isDestroyed) {
            // elimina el B2Body
            world.destroyBody(b2Body);
            isDestroyed = true;
            stateTimer = 0;
        }
    }

    /**
     * Define las actualizaciones para el segundo nivel
     * @param delta - delta time
     */
    private void levelTwoUpdates(float delta) {
        setCountDown(delta);

        if (detonationTime <= 0) {
            // impulsa la bomba a la izquierda cuando se detona
            b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
        }
    }

    /**
     * Devuelve la región de la textura
     * @return
     */
    private TextureRegion getTextureRegion() {
        if (!isDetonated) {
            // bomba estacionaria
            return bombIdle;
        } else {
            // bomba detonada
            return bombBlast.getKeyFrame(stateTimer, false);
        }
    }

    /**
     * Establece la cuenta atrás para detonar la bomba
     * @param delta
     */
    public void setCountDown(float delta) {
        if (!isDetonated) {
            timeCount += delta;
            if (timeCount >= 1) { // 1 segundo
                detonationTime--;
                timeCount = 0;
            }
        }
    }


    public void setDetonated(boolean detonated) {
        isDetonated = detonated;
    }

    @Override
    protected void move(UserInput input) {

    }

    @Override
    public void resetState(SpriteState state) {
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
