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

package com.pumpkin.dgx_pixel_dungeon.core.items.potions;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.blobs.Blob;
import com.pumpkin.dgx_pixel_dungeon.core.actors.blobs.ToxicGas;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;

public class PotionOfToxicGas extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_TOXICGAS;
	}

	@Override
	public void shatter( int cell ) {

		splash( cell );
		if (Dungeon.level.heroFOV[cell]) {
			identify();

			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}

		GameScene.add( Blob.seed( cell, 1000, ToxicGas.class ) );
	}
	
	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
