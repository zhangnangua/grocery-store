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

package com.pumpkin.dgx_pixel_dungeon.core.levels.features;

import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.ShatteredPixelDungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.HeroClass;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Talent;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.ArmoredStatue;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.npcs.Blacksmith;
import com.pumpkin.dgx_pixel_dungeon.core.effects.CellEmitter;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.LeafParticle;
import com.pumpkin.dgx_pixel_dungeon.core.items.Dewdrop;
import com.pumpkin.dgx_pixel_dungeon.core.items.Generator;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.glyphs.Camouflage;
import com.pumpkin.dgx_pixel_dungeon.core.items.artifacts.DriedRose;
import com.pumpkin.dgx_pixel_dungeon.core.items.artifacts.SandalsOfNature;
import com.pumpkin.dgx_pixel_dungeon.core.items.food.Berry;
import com.pumpkin.dgx_pixel_dungeon.core.items.trinkets.PetrifiedSeed;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Level;
import com.pumpkin.dgx_pixel_dungeon.core.levels.MiningLevel;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Terrain;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class HighGrass {
	
	//prevents items dropped from grass, from trampling that same grass.
	//yes this is a bit ugly, oh well.
	private static boolean freezeTrample = false;

	public static void trample( Level level, int pos ) {
		
		if (freezeTrample) return;
		
		Char ch = Actor.findChar(pos);
		
		if (level.map[pos] == Terrain.FURROWED_GRASS){
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				//Do nothing
				freezeTrample = true;
			} else {
				Level.set(pos, Terrain.GRASS);
			}
			
		} else {
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				Level.set(pos, Terrain.FURROWED_GRASS);
				freezeTrample = true;
			} else {
				Level.set(pos, Terrain.GRASS);
			}
			
			int naturalismLevel = 0;
			
			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.itemLevel() + 1;
						naturalism.charge();
					} else {
						naturalismLevel = -1;
					}
				}

				//berries try to drop on floors 2/3/4/6/7/8, to a max of 4/6
				if (ch instanceof Hero && ((Hero) ch).hasTalent(Talent.NATURES_BOUNTY)){
					int berriesAvailable = 2 + 2*((Hero) ch).pointsInTalent(Talent.NATURES_BOUNTY);

					Talent.NatureBerriesDropped dropped = Buff.affect(ch, Talent.NatureBerriesDropped.class);
					berriesAvailable -= dropped.count();

					if (berriesAvailable > 0) {
						int targetFloor = 2 + 2 * ((Hero) ch).pointsInTalent(Talent.NATURES_BOUNTY);
						targetFloor -= berriesAvailable;
						targetFloor += (targetFloor >= 5) ? 3 : 2;

						//If we're behind: 1/10, if we're on page: 1/30, if we're ahead: 1/90
						boolean droppingBerry = false;
						if (Dungeon.depth > targetFloor) droppingBerry = Random.Int(10) == 0;
						else if (Dungeon.depth == targetFloor) droppingBerry = Random.Int(30) == 0;
						else if (Dungeon.depth < targetFloor) droppingBerry = Random.Int(90) == 0;

						if (droppingBerry) {
							dropped.countUp(1);
							level.drop(new Berry(), pos).sprite.drop();
						}
					}

				}
			}

			//grass gives 1/3 the normal amount of loot in fungi level
			if (Dungeon.level instanceof MiningLevel
					&& Blacksmith.Quest.Type() == Blacksmith.Quest.FUNGI
					&& Random.Int(3) != 0){
				naturalismLevel = -1;
			}
			
			if (naturalismLevel >= 0) {
				// Seed, scales from 1/25 to 1/9
				float lootChance = 1/(25f - naturalismLevel*4f);

				// absolute max drop rate is ~1/6.5 with footwear of nature, ~1/18 without
				lootChance *= PetrifiedSeed.grassLootMultiplier();

				if (Random.Float() < lootChance) {
					if (Random.Float() < PetrifiedSeed.stoneInsteadOfSeedChance()) {
						level.drop(Generator.randomUsingDefaults(Generator.Category.STONE), pos).sprite.drop();
					} else {
						level.drop(Generator.random(Generator.Category.SEED), pos).sprite.drop();
					}
				}
				
				// Dew, scales from 1/6 to 1/4
				lootChance = 1/(6f -naturalismLevel/2f);
				if (Random.Float() < lootChance) {
					level.drop(new Dewdrop(), pos).sprite.drop();
				}
			}

			//Camouflage
			if (ch instanceof Hero) {
				Hero hero = (Hero) ch;
				if (hero.belongings.armor() != null && hero.belongings.armor().hasGlyph(Camouflage.class, hero)) {
					Camouflage.activate(hero, hero.belongings.armor.buffedLvl());
				}
			} else if (ch instanceof DriedRose.GhostHero){
				DriedRose.GhostHero ghost = (DriedRose.GhostHero) ch;
				if (ghost.armor() != null && ghost.armor().hasGlyph(Camouflage.class, ghost)){
					Camouflage.activate(ghost, ghost.armor().buffedLvl());
				}
			} else if (ch instanceof ArmoredStatue){
				ArmoredStatue statue = (ArmoredStatue) ch;
				if (statue.armor() != null && statue.armor().hasGlyph(Camouflage.class, statue)){
					Camouflage.activate(statue, statue.armor().buffedLvl());
				}
			}
			
		}
		
		freezeTrample = false;
		
		if (ShatteredPixelDungeon.scene() instanceof GameScene) {
			GameScene.updateMap(pos);
			
			CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
		}
	}
}
