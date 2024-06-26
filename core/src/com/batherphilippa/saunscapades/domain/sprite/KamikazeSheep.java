package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.domain.sprite.SpriteState.*;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.KAMIKAZE_SHEEP_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * KamikazeSheep - define un objeto de una oveja que se caye desde el cielo y paraliza temporalmente al jugador; extiende Chracter.
 */
public class KamikazeSheep extends Character {

    final private TextureRegion groundedSheep;
    private final Animation<TextureRegion> fallingSheep;
    private final Animation<TextureRegion> deadSheep;
    private SpriteState currState;
    private SpriteState prevState;
    private boolean hasLanded;
    private boolean isDead;
    private boolean isDestroyed;
    private float stateTimer;

    public KamikazeSheep(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.KAMIKAZE_SHEEP);

        // establece el tamaño de la textura
        setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // establece las regiones y animaciones
        this.setRegion(region);
        this.groundedSheep = spriteManager.getTextureRegion("timmy_idle", -1);
        this.fallingSheep = setAnimationFrames("timmy_victory", 1, 14, 0.005f);
        this.deadSheep = setAnimationFrames("timmy_electrocute", 0, 7, 0.5f);

        init();
    }

    /**
     * Establece los valores iniciales.
     */
    private void init() {
        this.stateTimer = 0;
        this.currState = SpriteState.IDLE;
        this.prevState = SpriteState.IDLE;
        this.hasLanded = false;
        this.isDead = false;
        this.isDestroyed = false;
        this.b2Body.setActive(false);
    }

    @Override
    public short getCategoryBit() {
        return KAMIKAZE_SHEEP_BIT;
    }

    public boolean isHasLanded() {
        return !hasLanded;
    }

    public void setHasLanded(boolean hasLanded) {
        this.hasLanded = hasLanded;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDestroyed || stateTimer < 2) {
            super.draw(batch);
        }
    }

    @Override
    public void update(float dt) {
        this.stateTimer += dt;
        b2Body.setActive(true);
        // divide por 2 porque las cordenadas 'x' y 'y'  x and y se toman desde el centro del B2 Body
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setCenter(b2Body.getPosition().x, b2Body.getPosition().y);

        // establece la región de la textura
        setRegion(getFrame(dt));

        // si está muerto, eliminalo desde el mundo Box2D
        if (isDead && !isDestroyed) {
            world.destroyBody(b2Body);
            isDestroyed = true;
            stateTimer = 0; // re-establecer el temporazidor
        }
    }

    @Override
    protected void move(UserInput input) { }

    /**
     * Devuevle la región de la textura para el fotograma de la animación
     * @param dt - delta time
     * @return la región de la textura
     */
    private TextureRegion getFrame(float dt) {
        currState = getSpritePositionState();
        TextureRegion region = getTextureRegion();
        stateTimer = currState == prevState ? stateTimer + dt : 0;
        prevState = currState;

        return region;
    }

    /**
     * Devuevle el estado del Sprite.
     * @return
     */
    private SpriteState getSpritePositionState() {
        if (isDead) {
            return DEAD;
        } else if (b2Body.getLinearVelocity().y < 0) {
            currState = FALLING;
            return FALLING;
        } else {
            currState = IDLE;
            return IDLE;
        }
    }

    /**
     * Devuelve la región de la textura depende del estado.
     * @return la región de la textura
     */
    private TextureRegion getTextureRegion() {
        return switch (currState) {
            case IDLE, JUMPING, MOVING, PARALYSED, VICTORY -> groundedSheep;
            case DEAD -> deadSheep.getKeyFrame(stateTimer, true);
            case FALLING -> fallingSheep.getKeyFrame(stateTimer, true);
        };
    }

    @Override
    public void resetState(SpriteState state) {
        this.isDead = true;
    }

    @Override
    public void createHead(FixtureDef fixDef) {

    }

    @Override
    public void dispose() {

    }
}
