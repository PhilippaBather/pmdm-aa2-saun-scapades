package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.BALLOON_BIT;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * Balloons - la clase define el objeto que representa el find de un nivel; extiende Character.
 */
public class Balloons extends Character implements Disposable {

    public Balloons(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.BALLOON);
        // establece el tamaño de la textura
        this.setBounds(getX(), getY(), 16 / PPM, 16 / PPM);
        // asocia la región de textura con el sprite
        this.setRegion(region);
    }

    @Override
    protected void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float delta) {
        setCenter(b2Body.getPosition().x, b2Body.getPosition().y);
    }

    @Override
    protected void move(UserInput input) {

    }

    @Override
    public void resetState(SpriteState state) {

    }

    @Override
    public void createHead(FixtureDef fixDef) {

    }

    @Override
    public short getCategoryBit() {
        return BALLOON_BIT;
    }

    @Override
    public void dispose() {

    }
}
