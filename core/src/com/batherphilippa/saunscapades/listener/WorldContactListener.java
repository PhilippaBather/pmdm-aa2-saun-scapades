package com.batherphilippa.saunscapades.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.tilemap.Coin;
import com.batherphilippa.saunscapades.domain.tilemap.Water;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.*;

public class WorldContactListener implements ContactListener {

    private SaunScapades game;
    private ResourceManager resManager;
    private Hud hud;

    public WorldContactListener(ResourceManager resManager, Hud hud, SaunScapades game) {
        this.game = game;
        this.resManager = resManager;
        this.hud = hud;
    }

    // TODO - should the WorldContactListener manage contacts or the classes for sprites and TileObjects?

    @Override
    public void beginContact(Contact contact) {
        // un contato consiste en dos fixtures
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use bitwise operation OR to get the collision definition
        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collisionDef) {
            case (SHAUN_BIT | COIN_BIT) -> {
                if (fixA.getFilterData().categoryBits == COIN_BIT) {
                    ((Coin) fixA.getUserData()).onContact(resManager, hud);
                } else {
                    ((Coin) fixB.getUserData()).onContact(resManager, hud);
                }
            }
            case (SHAUN_BIT | WATER_BIT) -> {
                if (fixA.getFilterData().categoryBits == WATER_BIT) {
                    ((Water) fixA.getUserData()).onContact(resManager, hud);
                } else {
                    ((Water) fixB.getUserData()).onContact(resManager, hud);
                }
                SpriteManager spriteManager = this.game.getSpriteManager();
                spriteManager.resetPlayerState();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
