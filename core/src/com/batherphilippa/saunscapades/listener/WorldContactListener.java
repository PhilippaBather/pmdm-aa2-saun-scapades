package com.batherphilippa.saunscapades.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.sprite.AngrySheep;
import com.batherphilippa.saunscapades.domain.sprite.Bomb;
import com.batherphilippa.saunscapades.domain.sprite.SpriteState;
import com.batherphilippa.saunscapades.domain.sprite.SpriteType;
import com.batherphilippa.saunscapades.domain.tilemap.Coin;
import com.batherphilippa.saunscapades.domain.tilemap.Water;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.*;

public class WorldContactListener implements ContactListener, Disposable {

    private final SaunScapades game;
    private final ResourceManager resManager;
    private final Hud hud;

    public WorldContactListener(ResourceManager resManager, Hud hud, SaunScapades game) {
        this.game = game;
        this.resManager = resManager;
        this.hud = hud;
    }

    @Override
    public void beginContact(Contact contact) {
        SpriteManager spriteManager = this.game.getSpriteManager();

        // un contato consiste en dos fixtures
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use bitwise operation OR to get the collision definition
        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collisionDef) {
            case (SHAUN_BIT | COIN_BIT) -> {
                if (fixA.getFilterData().categoryBits == COIN_BIT) {
                    ((Coin) fixA.getUserData()).onContact(resManager);
                } else {
                    ((Coin) fixB.getUserData()).onContact(resManager);
                }
                spriteManager.updateScore(250);
            }
            case (SHAUN_BIT | WATER_BIT) -> {
                if (fixA.getFilterData().categoryBits == WATER_BIT) {
                    ((Water) fixA.getUserData()).onContact(resManager);
                } else {
                    ((Water) fixB.getUserData()).onContact(resManager);
                }
                spriteManager.playerHit(SpriteType.OBJECT, 3);
            }
            case (ENEMY_BIT | OBJECT_BIT) -> {
                if (fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((AngrySheep) fixA.getUserData()).reverseMovement(true, false);
                } else {
                    ((AngrySheep) fixB.getUserData()).reverseMovement(true, false);
                }
            }
            case (ENEMY_BIT | SHAUN_BIT) -> {
                if (fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((AngrySheep) fixA.getUserData()).reverseMovement(true, false);
                } else {
                    ((AngrySheep) fixB.getUserData()).reverseMovement(true, false);
                }
                spriteManager.playerHit(SpriteType.ENEMY, 1);
            }
            case (ENEMY_HEAD_BIT | SHAUN_BIT) -> {
                if (fixA.getFilterData().categoryBits == ENEMY_HEAD_BIT) {
                    ((AngrySheep) fixA.getUserData()).resetState(SpriteState.DEAD);
                } else {
                    ((AngrySheep) fixB.getUserData()).resetState(SpriteState.DEAD);
                }
                spriteManager.enemyHit();
            }
            case (BOMB_BIT | SHAUN_BIT) -> {
                if (fixA.getFilterData().categoryBits == BOMB_BIT) {
                    ((Bomb) fixA.getUserData()).resetState(SpriteState.DEAD);
                } else {
                    ((Bomb) fixB.getUserData()).resetState(SpriteState.DEAD);
                }
                spriteManager.playerHit(SpriteType.BOMB, 1);
            }
            case (SHAUN_BIT | SHIRLEY_BIT) -> {
                spriteManager.levelEndCelebration();
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

    @Override
    public void dispose() {
        this.game.dispose();
        this.hud.dispose();
        this.resManager.dispose();
    }
}
