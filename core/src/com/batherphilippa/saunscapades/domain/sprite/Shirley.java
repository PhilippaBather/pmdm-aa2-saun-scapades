package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.SHIRLEY_BIT;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class Shirley extends Character {

    private TextureRegion shirleyIdle;

    public Shirley(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager, SpriteType.SHIRLEY);
        setBounds(getX(), getY(), 16 / PPM, 16 / PPM);

        shirleyIdle = region;
        this.setRegion(shirleyIdle);
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
        return SHIRLEY_BIT;
    }

    @Override
    public void dispose() {

    }
}
