/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.pumpkin.dgx_pixel_dungeon.core;

import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.PixelScene;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.TitleScene;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.WelcomeScene;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Game;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Music;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.DeviceCompat;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PlatformSupport;

public class ShatteredPixelDungeon extends Game {

	//variable constants for specific older versions of shattered, used for data conversion
	public static final int v1_2_3 = 628; //v1.2.3 is kept for now, for old rankings score logic

	//savegames from versions older than v1.4.3 are no longer supported, and data from them is ignored
	public static final int v1_4_3 = 668;

	public static final int v2_0_2 = 700;
	public static final int v2_1_4 = 737; //iOS was 737, other platforms were 736
	public static final int v2_2_1 = 755; //iOS was 755 (also called v2.2.2), other platforms were 754
	public static final int v2_3_2 = 768;
	public static final int v2_4_0 = 780;
	
	public ShatteredPixelDungeon( PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//pre-v2.4.0
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.potions.brews.UnstableBrew.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.potions.AlchemicalCatalyst" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.spells.UnstableSpell.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.spells.ArcaneCatalyst" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.potions.elixirs.ElixirOfFeatherFall.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.spells.FeatherFall" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.potions.elixirs.ElixirOfFeatherFall.FeatherBuff.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.spells.FeatherFall$FeatherBuff" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.potions.brews.AquaBrew.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.spells.AquaBlast" );

		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.entrance.EntranceRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.EntranceRoom" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.exit.ExitRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.ExitRoom" );

		//pre-v2.3.0
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.items.bombs.Bomb.ConjuredBomb.class,
				"com.pumpkin.dgx_pixel_dungeon.core.items.bombs.Bomb$MagicalBomb" );

		//pre-v2.2.0
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.BlacksmithRoom.QuestEntrance.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.BlacksmithRoom$QuestEntrance" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.BlacksmithRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.BlacksmithRoom" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.MassGraveRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.special.MassGraveRoom" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.MassGraveRoom.Bones.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.special.MassGraveRoom$Bones" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.RitualSiteRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.RitualSiteRoom" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.RitualSiteRoom.RitualMarker.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.RitualSiteRoom$RitualMarker" );
		com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle.addAlias(
				com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.RotGardenRoom.class,
				"com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.special.RotGardenRoom" );
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		SPDAction.loadBindings();
		
		Music.INSTANCE.enable( SPDSettings.music() );
		Music.INSTANCE.volume( SPDSettings.musicVol()*SPDSettings.musicVol()/100f );
		Sample.INSTANCE.enable( SPDSettings.soundFx() );
		Sample.INSTANCE.volume( SPDSettings.SFXVol()*SPDSettings.SFXVol()/100f );

		Sample.INSTANCE.load( Assets.Sounds.all );
		
	}

	@Override
	public void finish() {
		if (!DeviceCompat.isiOS()) {
			super.finish();
		} else {
			//can't exit on iOS (Apple guidelines), so just go to title screen
			switchScene(TitleScene.class);
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}
	
	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}
	
	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}