package com.mewmakovs.game;

import com.badlogic.gdx.Game;
import com.mewmakovs.hkHelpers.AssetLoader;
import com.mewmakovs.screens.GameScreen;

public class MyGame extends Game {

	private GameScreen gameScreen;

	@Override
	public void create () {
		AssetLoader.load();
		gameScreen = new GameScreen();

		setScreen(gameScreen);
	}

	@Override
	public void dispose () {
		super.dispose();
		AssetLoader.dispose();
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}
}
