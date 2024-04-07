package com.batherphilippa.saunscapades.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.batherphilippa.saunscapades.domain.tilemap.TileObject;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

public class WorldContactListener implements ContactListener {

    private ResourceManager resManager;
    private Hud hud;

    public WorldContactListener(ResourceManager resManager, Hud hud) {
        this.resManager = resManager;
        this.hud = hud;
    }

    // TODO - should the WorldContactListener manage contacts or the classes for sprites and TileObjects?

    @Override
    public void beginContact(Contact contact) {
        // un contato consiste en dos fixtures
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // identifica una colisión con la cabeza de shaun
        if ((fixA.getUserData() == "head") || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            // identifica el tipo de objeto
            if ((object.getUserData() != null) && (TileObject.class.isAssignableFrom(object.getUserData().getClass()))) {
                ((TileObject) object.getUserData()).onContact(resManager, hud);
            }

        }

        // use bitwise operation OR to get the collision definition
//        int collisionDef = fixB.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
//
//        switch(collisionDef) {
//            case(SHAUN_BIT | COIN_BIT) -> {
//                if(fixA.getFilterData().categoryBits == COIN_BIT) {
//                    // TODO
//                    System.out.println("coin hit...");
//                }
//            }
//        };

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
