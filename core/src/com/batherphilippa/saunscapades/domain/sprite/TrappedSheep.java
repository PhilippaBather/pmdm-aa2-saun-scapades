package com.batherphilippa.saunscapades.domain.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.batherphilippa.saunscapades.manager.SpriteManager;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.TRAPPED_SHEEP_BIT;

public class TrappedSheep extends Shirley{

    public TextureRegion sheepDead;
    public TrappedSheep(TextureRegion region, World world, float x, float y, float radius, SpriteManager spriteManager) {
        super(region, world, x, y, radius, spriteManager);
    }
    @Override
    public short getCategoryBit() {
        return TRAPPED_SHEEP_BIT;
    }

    public void setKilled() {
        sheepDead = spriteManager.getTextureRegion("shaun_electrocute", 0);
        this.setRegion(sheepDead);
    }
}
