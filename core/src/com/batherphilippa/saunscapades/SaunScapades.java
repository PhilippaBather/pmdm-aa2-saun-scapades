package com.batherphilippa.saunscapades;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batherphilippa.saunscapades.manager.B2WorldManager;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.screen.GameScreen;

public class SaunScapades extends Game {
	public SpriteBatch batch;
	public B2WorldManager b2WorldManager;
	public CameraManager camManager;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camManager = new CameraManager();
		b2WorldManager = new B2WorldManager(this);

		this.setScreen(new GameScreen(this));
	}

	public B2WorldManager getB2WorldManager() {
		return b2WorldManager;
	}

	public CameraManager getCamManager() {
		return camManager;
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
