package com.batherphilippa.saunscapades.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.domain.sprite.*;
import com.batherphilippa.saunscapades.domain.tilemap.Block;
import com.batherphilippa.saunscapades.domain.tilemap.Coin;
import com.batherphilippa.saunscapades.domain.tilemap.Water;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.scene.Hud;

import static com.batherphilippa.saunscapades.listener.WorldCategoryBits.*;
import static com.batherphilippa.saunscapades.screen.constants.PointsConstants.POINTS_COIN;

public class WorldContactListener implements ContactListener, Disposable {

    private final GameScreen gameScreen;
    private final ResourceManager resManager;
    private final Hud hud;

    public WorldContactListener(ResourceManager resManager, Hud hud, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.resManager = resManager;
        this.hud = hud;
    }

    @Override
    public void beginContact(Contact contact) {
        SpriteManager spriteManager = this.gameScreen.getSpriteManager();

        // un contato consiste en dos fixtures
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use bitwise operation OR to get the collision definition
        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collisionDef) {
            case (SHAUN_BIT | COIN_BIT) -> {
                if (fixA.getFilterData().categoryBits == COIN_BIT) {
                    ((Coin) fixA.getUserData()).onContact();
                } else {
                    ((Coin) fixB.getUserData()).onContact();
                }
                spriteManager.updateScore(POINTS_COIN);
            }
            case (SHAUN_BIT | WATER_BIT) -> {
                if (fixA.getFilterData().categoryBits == WATER_BIT) {
                    ((Water) fixA.getUserData()).onContact();
                } else {
                    ((Water) fixB.getUserData()).onContact();
                }
                spriteManager.playerHit(SpriteType.OBJECT, 2);
            }
            case (ENEMY_BIT | OBJECT_BIT) -> {
                if (fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((AngrySheep) fixA.getUserData()).reverseMovement(true, false);
                } else {
                    ((AngrySheep) fixB.getUserData()).reverseMovement(true, false);
                }
            }
            case (ENEMY_BIT) -> {
                ((AngrySheep) fixA.getUserData()).reverseMovement(true, false);
                ((AngrySheep) fixB.getUserData()).reverseMovement(true, false);
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
            case (SHAUN_BIT | SHIRLEY_BIT) -> spriteManager.levelEndCelebration();
            case (SHAUN_HEAD_BIT | BLOCK_BIT) -> {
                if (fixA.getFilterData().categoryBits == BLOCK_BIT) {
                    ((Block) fixA.getUserData()).onContact();
                } else {
                    ((Block) fixB.getUserData()).onContact();
                }
            }
            case (TRAPPED_SHEEP_BIT | BOMB_BIT) -> {
                if (fixA.getFilterData().categoryBits == BOMB_BIT) {
                    ((Bomb) fixA.getUserData()).setDetonated(true);
                } else {
                    ((Bomb) fixB.getUserData()).setDetonated(true);
                }
                if (fixA.getFilterData().categoryBits == TRAPPED_SHEEP_BIT) {
                    ((TrappedSheep) fixA.getUserData()).setKilled();
                } else {
                    ((TrappedSheep) fixB.getUserData()).setKilled();
                }
                spriteManager.handleSheepDeath();
            }
            case (TRAPPED_SHEEP_BIT | GROUND_BIT), SHAUN_BIT -> spriteManager.handleSavedSheep();
            case (FALLING_SHEEP_BIT | SHAUN_HEAD_BIT) -> {
                if (fixA.getFilterData().categoryBits == FALLING_SHEEP_BIT) {
                    if (((FallingSheep) fixA.getUserData()).isHasLanded()) {
                        spriteManager.handleParalysedShaun();
                    }
                    ((FallingSheep) fixA.getUserData()).setHasLanded(true);
                } else {
                    if (((FallingSheep) fixB.getUserData()).isHasLanded()) {
                        spriteManager.handleParalysedShaun();
                    }
                    ((FallingSheep) fixB.getUserData()).setHasLanded(true);
                }
            }
            case (FALLING_SHEEP_BIT | GROUND_BIT), TRAPPED_SHEEP_BIT, OBJECT_BIT -> spriteManager.handleFallingSheep();
            case (BOMB_BIT | ENEMY_BIT) -> {
                if (fixA.getFilterData().categoryBits == BOMB_BIT) {
                    ((Bomb) fixA.getUserData()).resetState(SpriteState.DEAD);
                    ((AngrySheep) fixB.getUserData()).resetState(SpriteState.DEAD);
                } else {
                    ((Bomb) fixB.getUserData()).resetState(SpriteState.DEAD);
                    ((AngrySheep) fixA.getUserData()).resetState(SpriteState.DEAD);
                }
                spriteManager.handleEnemyHitByBomb();
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
        this.gameScreen.dispose();
        this.hud.dispose();
        this.resManager.dispose();
    }
}
