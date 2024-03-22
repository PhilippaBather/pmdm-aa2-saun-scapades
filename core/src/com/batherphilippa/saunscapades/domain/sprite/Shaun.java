package com.batherphilippa.saunscapades.domain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Shaun extends Character {

    public Shaun(World world, float x, float y, float radius) {
        super(world, x, y, radius);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void move() {
    }

    @Override
    public void dispose() {

    }
}
