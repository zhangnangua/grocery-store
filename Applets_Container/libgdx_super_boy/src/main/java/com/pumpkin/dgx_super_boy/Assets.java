/******************************************************************************
 * Spine Runtimes Software License
 * Version 2.1
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software (typically granted by licensing Spine), you
 * may not (a) modify, translate, adapt or otherwise create derivative works,
 * improvements of the Software or develop new applications using the Software
 * or (b) remove, delete, alter or obscure any trademarks or any copyright,
 * trademark, patent or other intellectual property or proprietary rights notices
 * on or in the Software, including any copy thereof. Redistributions in binary
 * or source form must include this license and terms.
 *
 * THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package com.pumpkin.dgx_super_boy;

import static com.pumpkin.dgx_super_boy.Model.fps;

import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

/** Centralized place to load and store assets. */
public class Assets {
	public TextureAtlas playerAtlas, enemyAtlas;
	public TextureRegion bulletRegion, hitRegion, crosshair;
	public TextureRegion titleRegion, gameOverRegion, youLoseRegion, youWinRegion, startRegion;

	public SkeletonData playerSkeletonData, enemySkeletonData;
	public AnimationStateData playerAnimationData, enemyAnimationData;
	public ObjectMap<Model.State, View.StateView> playerStates = new ObjectMap(), enemyStates = new ObjectMap();

	public Assets () {
		bulletRegion = loadRegion("bullet.png");
		hitRegion = loadRegion("bullet-hit.png");
		titleRegion = loadRegion("title.png");
		gameOverRegion = loadRegion("gameOver.png");
		youLoseRegion = loadRegion("youLose.png");
		youWinRegion = loadRegion("youWin.png");
		startRegion = loadRegion("start.png");
		crosshair = loadRegion("crosshair.png");

		SoundEffect.shoot.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.ogg"));
		SoundEffect.hit.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.ogg"));
		SoundEffect.footstep1.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/footstep1.ogg"));
		SoundEffect.footstep2.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/footstep2.ogg"));
		SoundEffect.squish.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/squish.ogg"));
		SoundEffect.squish.volume = 0.6f;
		SoundEffect.hurtPlayer.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt-player.ogg"));
		SoundEffect.hurtAlien.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt-alien.ogg"));
		SoundEffect.hurtAlien.volume = 0.5f;

		loadPlayerAssets();
		loadEnemyAssets();
	}

	TextureRegion loadRegion (String name) {
		Texture texture = new Texture(name);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		return new TextureRegion(texture);
	}

	void loadPlayerAssets () {
		playerAtlas = new TextureAtlas(Gdx.files.internal("spineboy/spineboy.atlas"));

		SkeletonJson json = new SkeletonJson(playerAtlas);
		json.setScale(Player.height / Player.heightSource);
		playerSkeletonData = json.readSkeletonData(Gdx.files.internal("spineboy/spineboy.json"));

		playerAnimationData = new AnimationStateData(playerSkeletonData);
		playerAnimationData.setDefaultMix(0.2f);
		setMix(playerAnimationData, "idle", "run", 0.3f);
		setMix(playerAnimationData, "run", "idle", 0.1f);
		setMix(playerAnimationData, "shoot", "shoot", 0);

		setupState(playerStates, Model.State.death, playerSkeletonData, "death", false);
		View.StateView idle = setupState(playerStates, Model.State.idle, playerSkeletonData, "idle", true);
		View.StateView jump = setupState(playerStates, Model.State.jump, playerSkeletonData, "jump", false);
		View.StateView run = setupState(playerStates, Model.State.run, playerSkeletonData, "run", true);
		if (idle.animation != null) run.startTimes.put(idle.animation, 8 * fps);
		if (jump.animation != null) run.startTimes.put(jump.animation, 22 * fps);
		View.StateView fall = setupState(playerStates, Model.State.fall, playerSkeletonData, "jump", false);
		fall.defaultStartTime = 22 * fps;
	}

	void loadEnemyAssets () {
		enemyAtlas = new TextureAtlas(Gdx.files.internal("alien/alien.atlas"));

		SkeletonJson json = new SkeletonJson(enemyAtlas);
		json.setScale(Enemy.height / Enemy.heightSource);
		enemySkeletonData = json.readSkeletonData(Gdx.files.internal("alien/alien.json"));

		enemyAnimationData = new AnimationStateData(enemySkeletonData);
		enemyAnimationData.setDefaultMix(0.1f);

		setupState(enemyStates, Model.State.idle, enemySkeletonData, "run", true);
		setupState(enemyStates, Model.State.jump, enemySkeletonData, "jump", true);
		setupState(enemyStates, Model.State.run, enemySkeletonData, "run", true);
		setupState(enemyStates, Model.State.death, enemySkeletonData, "death", false);
		setupState(enemyStates, Model.State.fall, enemySkeletonData, "run", false);
	}

	void setMix (AnimationStateData data, String from, String to, float mix) {
		Animation fromAnimation = data.getSkeletonData().findAnimation(from);
		Animation toAnimation = data.getSkeletonData().findAnimation(to);
		if (fromAnimation == null || toAnimation == null) return;
		data.setMix(fromAnimation, toAnimation, mix);
	}

	View.StateView setupState (ObjectMap map, Model.State state, SkeletonData skeletonData, String name, boolean loop) {
		View.StateView stateView = new View.StateView();
		stateView.animation = skeletonData.findAnimation(name);
		stateView.loop = loop;
		map.put(state, stateView);
		return stateView;
	}

	void dispose () {
		playerAtlas.dispose();
		enemyAtlas.dispose();
	}

	public enum SoundEffect {
		shoot, hit, footstep1, footstep2, squish, hurtPlayer, hurtAlien;

		Sound sound;
		float volume = 1;

		public void play() {
			sound.play(volume);
		}
	}
}
