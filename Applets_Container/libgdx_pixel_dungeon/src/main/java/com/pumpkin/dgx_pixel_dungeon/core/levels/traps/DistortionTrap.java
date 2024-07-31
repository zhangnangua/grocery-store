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

import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Acidic;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Albino;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.ArmoredBrute;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Bandit;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Bestiary;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.CausticSlime;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.DM201;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Elemental;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mimic;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mob;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Piranha;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Senior;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Statue;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Wraith;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.npcs.RatKing;
import com.pumpkin.dgx_pixel_dungeon.core.items.scrolls.ScrollOfTeleportation;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PathFinder;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class DistortionTrap extends Trap{

	private static final float DELAY = 2f;

	{
		color = TEAL;
		shape = LARGE_DOT;
	}

	private static final ArrayList<Class<?extends Mob>> RARE = new ArrayList<>(Arrays.asList(
			Albino.class, CausticSlime.class,
			Bandit.class,
			ArmoredBrute.class, DM201.class,
			Elemental.ChaosElemental.class, Senior.class,
			Acidic.class));

	@Override
	public void activate() {

		int nMobs = 3;
		if (Random.Int( 2 ) == 0) {
			nMobs++;
			if (Random.Int( 2 ) == 0) {
				nMobs++;
			}
		}

		ArrayList<Integer> candidates = new ArrayList<>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
				candidates.add( p );
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<>();

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );

			respawnPoints.add( candidates.remove( index ) );
			nMobs--;
		}

		ArrayList<Mob> mobs = new ArrayList<>();

		int summoned = 0;
		for (Integer point : respawnPoints) {
			summoned++;
			Mob mob;
			switch (summoned){
				case 1:
					if (Dungeon.depth != 5 && Random.Int(100) == 0){
						mob = new RatKing();
						break;
					}
				case 3: case 5 : default:
					int floor;
					do {
						floor = Random.Int(25);
					} while( Dungeon.bossLevel(floor));
					mob = Reflection.newInstance(Bestiary.getMobRotation(floor).get(0));
					break;
				case 2:
					switch (2){
						case 0: default:
							Wraith.spawnAt(point);
							continue; //wraiths spawn themselves, no need to do more
						case 1:
							//yes it's intended that these are likely to die right away
							mob = Piranha.random();
							break;
						case 2:
							mob = Mimic.spawnAt(point, false);
							((Mimic)mob).stopHiding();
							mob.alignment = Char.Alignment.ENEMY;
							break;
						case 3:
							mob = Statue.random(false);
							break;
					}
					break;
				case 4:
					mob = Reflection.newInstance(Random.element(RARE));
					break;
			}

			if (Char.hasProp(mob, Char.Property.LARGE) && !Dungeon.level.openSpace[point]){
				continue;
			}

			mob.maxLvl = Hero.MAX_LEVEL-1;
			mob.state = mob.WANDERING;
			mob.pos = point;
			GameScene.add(mob, DELAY);
			mobs.add(mob);
		}

		//important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
		Trap t;
		for (Mob mob : mobs){
			//manually trigger traps first to avoid sfx spam
			if ((t = Dungeon.level.traps.get(mob.pos)) != null && t.active){
				if (t.disarmedByActivation) t.disarm();
				t.reveal();
				t.activate();
			}
			ScrollOfTeleportation.appear(mob, mob.pos);
			Dungeon.level.occupyCell(mob);
		}

	}
}