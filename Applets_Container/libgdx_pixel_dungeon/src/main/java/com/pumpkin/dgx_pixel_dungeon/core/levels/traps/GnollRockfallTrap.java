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

package com.pumpkin.dgx_pixel_dungeon.core.levels.traps;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Paralysis;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.GnollGeomancer;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.GnollGuard;
import com.pumpkin.dgx_pixel_dungeon.core.effects.CellEmitter;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Level;
import com.pumpkin.dgx_pixel_dungeon.core.levels.MiningLevel;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Terrain;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.PixelScene;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.BArray;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PathFinder;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

import java.util.ArrayList;

public class GnollRockfallTrap extends RockfallTrap {

	@Override
	public void activate() {

		ArrayList<Integer> rockCells = new ArrayList<>();

		//drop rocks in a 5x5 grid, ignoring cells next to barricades
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level instanceof MiningLevel){
					boolean barricade = false;
					for (int j : PathFinder.NEIGHBOURS9){
						if (Dungeon.level.map[i+j] == Terrain.BARRICADE){
							barricade = true;
						}
					}
					if (barricade) continue;
				}
				rockCells.add(i);
			}
		}

		boolean seen = false;
		for (int cell : rockCells){

			if (Dungeon.level.heroFOV[ cell ]){
				CellEmitter.get( cell - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
				seen = true;
			}

			Char ch = Actor.findChar( cell );

			if (ch != null && ch.isAlive() && !(ch instanceof GnollGeomancer)){
				//deals notably less damage than a regular rockfall trap, but ignores armor
				int damage = Char.combatRoll(6, 12);
				ch.damage( Math.max(damage, 0) , this);

				//guards take full paralysis, otherwise just a little
				Buff.prolong(ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3);

				if (!ch.isAlive() && ch == Dungeon.hero){
					Dungeon.fail( this );
					GLog.n( Messages.get(this, "ondeath") );
				}
			} else if (ch == null
					&& Dungeon.level instanceof MiningLevel
					&& Dungeon.level.traps.get(cell) == null
					&& Dungeon.level.plants.get(cell) == null
					&& Random.Int(2) == 0){
				Level.set( cell, Terrain.MINE_BOULDER );
				GameScene.updateMap(cell);
			}
		}

		if (seen){
			PixelScene.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.Sounds.ROCKS);
		}

	}

}
