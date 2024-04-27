package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.domain.sprite.SpriteType.PLAYER;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.SHAUN_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.SHAUN_HEAD_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

public class Shaun extends Character {

    private SpriteState currState;
    private SpriteState prevState;
    private final Animation<TextureRegion> shaunMove;
    private final Animation<TextureRegion> shaunVictory;
    private final TextureRegion shaunJump;
    private final TextureRegion shaunIdle;
    private final TextureRegion shaunDead;
    private float timeCount;
    private float stateTimer;
    private int paralysedDuration;
    private boolean isDirRight;
    private boolean hasLostLife;
    private boolean isParalysed;
    private boolean isVictorious;

    public Shaun(TextureAtlas.AtlasRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, PLAYER);

        // establece el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // asocia la región de textura con el sprite
        this.shaunIdle = region;
        this.setRegion(shaunIdle);

        setInitialState();

        // establece las regiones y animaciónes
        shaunJump = spriteManager.getTextureRegion("shaun_jump_up", 4);
        shaunDead = spriteManager.getTextureRegion("shaun_electrocute", 0);
        shaunMove = setAnimationFrames("shaun_walk", 0, 7, 0.1f);
        shaunVictory = setAnimationFrames("shaun_victory", 1, 6, 0.2f);

        this.hasLostLife = false;
        this.isParalysed = false;

    }

    /**
     * Establece los valores iniciales.
     */
    private void setInitialState() {
        currState = SpriteState.IDLE;
        prevState = SpriteState.IDLE;
        stateTimer = 0;
        isDirRight = true;
        timeCount = 0;
        paralysedDuration = 2;
    }

    /**
     * Re-establece el estado del jugador
     * @param state - estado
     */
    @Override
    public void resetState(SpriteState state) {
        if (state == SpriteState.DEAD) {
            hasLostLife = true;
        }
        if (state == SpriteState.VICTORY) {
            isVictorious = true;
        }
        if (state == SpriteState.PARALYSED) {
            isParalysed = true;
        }
    }

    /**
     * Crea una definición del 'fixture' en la cabeza del objeto.
     * @param fixDef - fixture definition
     */
    public void createHead(FixtureDef fixDef) {
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-6, 10).scl(1 / PPM);
        vertices[1] = new Vector2(6, 10).scl(1 / PPM); // to right
        vertices[2] = new Vector2(-5, 6).scl(1 / PPM);
        vertices[3] = new Vector2(5, 6).scl(1 / PPM);
        head.set(vertices);
        fixDef.shape = head;

        // 'bounciness' - el grado de rebote
        fixDef.restitution = 0.5f;

        // aplica un bit de categoría
        fixDef.filter.categoryBits = SHAUN_HEAD_BIT;

        // tener acceso al objeto desde el 'collision handler'
        b2Body.createFixture(fixDef).setUserData(this);
    }

    @Override
    public short getCategoryBit() {
        return SHAUN_BIT;
    }

    public boolean isHasLostLife() {
        return hasLostLife;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float dt) {
        // as x and y coordinates are taken from the centre of the body
        this.setPosition((b2Body.getPosition().x - getWidth() / 2), b2Body.getPosition().y - getHeight() / 2);

        if (currState == SpriteState.PARALYSED) {
            setParalysedCountDown(dt);
        }

        if (paralysedDuration <= 0) {
            isParalysed = false;
            paralysedDuration = 2;
        }

        setRegion(getFrame(dt));
    }

    /**
     * Devuevle la región de la textura para el fotograma de la animación
     * @param dt - delta time
     * @return la región de la textura
     */
    private TextureRegion getFrame(float dt) {
        currState = getSpritePositionState();
        TextureRegion region = getTextureRegion();

        // cambia la dirección del B2Body depende de su moviemiento por el eje 'x'
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
     * @return SpriteState - el estado de la actividad del Sprite
     */
    public SpriteState getSpritePositionState() {
        if (hasLostLife) {
            return SpriteState.DEAD;
        } else if (isParalysed) {
            return SpriteState.PARALYSED;
        } else if (isVictorious) {
            return SpriteState.VICTORY;
        } else if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && prevState == SpriteState.JUMPING)) {
            return SpriteState.JUMPING;
        } else if (b2Body.getLinearVelocity().y < 0) {
            return SpriteState.FALLING;
        } else if (b2Body.getLinearVelocity().x != 0) {
            return SpriteState.MOVING;
        } else {
            return SpriteState.IDLE;
        }
    }

    /**
     * Establece el temporazidor para la cantidad del tiempo que el jugador está paralizado.
     * @param dt - delta time
     */
    private void setParalysedCountDown(float dt) {
        if (isParalysed) {
            timeCount += dt;
            if (timeCount >= 1) { // 1 segundo
                paralysedDuration--;
                timeCount = 0;
            }
        }
    }

    /**
     * Devuelve la región de la textura según el estado.
     * @return
     */
    private TextureRegion getTextureRegion() {
        return switch (currState) {
            case MOVING -> shaunMove.getKeyFrame(stateTimer, true);
            case JUMPING -> shaunJump;
            case DEAD -> shaunDead;
            case VICTORY -> shaunVictory.getKeyFrame(stateTimer, true);
            case IDLE, FALLING, PARALYSED -> shaunIdle;
        };
    }

    public float getLinearVelocity() {
        return this.getB2Body().getLinearVelocity().x;
    }

    @Override
    public void move(UserInput input) {
        if (!isParalysed) {
            switch (input) {
                case UP -> this.getB2Body().applyLinearImpulse(new Vector2(0, 4f),
                        this.getB2Body().getWorldCenter(), true);
                case RIGHT -> this.getB2Body().applyLinearImpulse(new Vector2(0.1f, 0),
                        this.getB2Body().getWorldCenter(), true);
                case LEFT -> this.getB2Body().applyLinearImpulse(new Vector2(-0.1f, 0),
                        this.getB2Body().getWorldCenter(), true);
            }
        }
    }

    /**
     * Lanza el jugador en el aire si ha tocado una bomba detonando.
     */
    public void launchShaun() {
        b2Body.applyLinearImpulse(new Vector2(0, 5f), b2Body.getWorldCenter(), true);
    }

    @Override
    public void dispose() {}
}
