package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.ENEMY_BIT;
import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.ENEMY_HEAD_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * AngrySheep - define el inimigo de una oveja enfada; extiende Character.
 */
public class AngrySheep extends Character {

    private final Animation<TextureRegion> deadAnimation;
    private final Animation<TextureRegion> chargeAnimation;
    private float stateTimer;
    private final Vector2 movement;
    private boolean isDirRight;
    private boolean isDead;
    private boolean isDestroyed;

    public AngrySheep(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.ENEMY);

        // establece el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        // establece las regiones y animaciónes
        this.chargeAnimation = setAnimationFrames("black_sheep_run", 1, 4, 0.3f);
        this.deadAnimation = setAnimationFrames("black_sheep_dead", 1, 3, 0.5f);

        // movimiento
        this.movement = new Vector2(-1, 0);

        this.isDead = false;
        this.isDirRight = true;
        this.stateTimer = 0;
    }

    /**
     * Vuelve al revés la veolocidad en el eje.     *
     * @param x - x velocidad
     * @param y y velocidad
     */
    public void reverseMovement(boolean x, boolean y) {
        if (x) {
            movement.x = -movement.x;
        }
        if (y) {
            movement.y = -movement.y;
        }
    }

    /**
     * Crea una definición del 'fixture' en la cabeza del objeto.
     * @param fixDef - fixture definition
     */
    public void createHead(FixtureDef fixDef) {
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-6, 10).scl( 1 / PPM);
        vertices[1] = new Vector2(6, 10).scl( 1 / PPM);
        vertices[2] = new Vector2(-5, 6).scl( 1 / PPM);
        vertices[3] = new Vector2(5, 6).scl( 1 / PPM);
        head.set(vertices);
        fixDef.shape = head;

        // 'bounciness' - el grado de rebote
        fixDef.restitution = 0.5f;

        // aplica un bit de categoría
        fixDef.filter.categoryBits = ENEMY_HEAD_BIT;

        // tener acceso al objeto desde el 'collision handler'
        b2Body.createFixture(fixDef).setUserData(this);
    }

    @Override
    public short getCategoryBit() {
        return ENEMY_BIT;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDestroyed || stateTimer < 2) {
            super.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        this.stateTimer += delta;
        // establece la animación/la región de textura
        setRegion(getFrame());

        // si no está muerto, establece la velocidad lineal y la ubicación del B2Body
        if (!isDead) {
            b2Body.setLinearVelocity(movement);
            this.setPosition((b2Body.getPosition().x - getWidth() / 2), b2Body.getPosition().y - getHeight() / 2);
        } else if (!isDestroyed) {
            // si está muerto, elimina el B2Body
            world.destroyBody(b2Body);
            isDestroyed = true;
            stateTimer = 0; // re-esetablecer el temporazidor
        }
    }

    /**
     * Devuelve la región de textura requerida para el fotograma ('frame').
     * @return la región de la textura
     */
    private TextureRegion getFrame() {
        // obtiene la región de la textura
        TextureRegion region = getTextureRegion();
        // cambia la dirección del B2Body depende de su moviemiento por el eje 'x'
        if ((b2Body.getLinearVelocity().x > 0 || !isDirRight) && !region.isFlipX()) {
            region.flip(true, false);
            isDirRight = false;
        } else if ((b2Body.getLinearVelocity().x < 0 || isDirRight) && region.isFlipX()) {
            region.flip(true, false);
            isDirRight = true;
        }
        return region;
    }

    /**
     * Devuele la región de la textura
     * @return la región de la textura
     */
    private TextureRegion getTextureRegion() {
        if (!isDead) {
            return chargeAnimation.getKeyFrame(stateTimer, true);
        } else {
            return deadAnimation.getKeyFrame(stateTimer, false);
        }
    }


    @Override
    protected void move(UserInput input) {

    }

    @Override
    public void resetState(SpriteState state) {
        this.isDead = true;
    }

    @Override
    public void dispose() {
    }
}
