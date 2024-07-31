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

package com.pumpkin.dgx_pixel_dungeon.core.sprites;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Spinner;
import com.pumpkin.dgx_pixel_dungeon.core.effects.MagicMissile;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.MovieClip;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.TextureFilm;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Callback;

public class FungalSpinnerSprite extends MobSprite {

	public FungalSpinnerSprite() {
		super();

		perspectiveRaise = 0f;

		texture( Assets.Sprites.FUNGAL_SPINNER );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new MovieClip.Animation( 10, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 0, 1 );

		run = new MovieClip.Animation( 15, true );
		run.frames( frames, 0, 2, 0, 3 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 0, 4, 5, 0 );

		zap = attack.clone();

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 6, 7, 8, 9 );

		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (parent != null) {
			parent.sendToBack(this);
			if (aura != null){
				parent.sendToBack(aura);
			}
		}
		renderShadow = false;
	}

	public void zap( int cell ) {

		super.zap( cell );

		MagicMissile.boltFromChar( parent,
				MagicMissile.FOLIAGE,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Spinner)ch).shootWeb();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.MISS );
	}

	@Override
	public void onComplete( MovieClip.Animation anim ) {
		if (anim == zap) {
			play( run );
		}
		super.onComplete( anim );
	}

	@Override
	public int blood() {
		return 0xFF88CC44;
	}
}
