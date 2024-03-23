package com.batherphilippa.saunscapades;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batherphilippa.saunscapades.manager.B2WorldManager;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.GameScreen;

public class SaunScapades extends Game {
	public SpriteBatch batch;
	private B2WorldManager b2WorldManager;
	private CameraManager camManager;
	private ResourceManager resManager;
	private SpriteManager spriteManager;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camManager = new CameraManager();
		b2WorldManager = new B2WorldManager(this);
		resManager = new ResourceManager();
		spriteManager = new SpriteManager(this, batch);

		this.setScreen(new GameScreen(this));
	}

	public B2WorldManager getB2WorldManager() {
		return b2WorldManager;
	}

	public CameraManager getCamManager() {
		return camManager;
	}

	public ResourceManager getResManager() {
		return resManager;
	}

	public SpriteManager getSpriteManager() {
		return spriteManager;
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
