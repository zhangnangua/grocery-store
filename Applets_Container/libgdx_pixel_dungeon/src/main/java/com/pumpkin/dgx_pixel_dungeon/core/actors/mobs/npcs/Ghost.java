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

package com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.npcs;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.Statistics;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.AscensionChallenge;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.FetidRat;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.GnollTrickster;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.GreatCrab;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mob;
import com.pumpkin.dgx_pixel_dungeon.core.items.Generator;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.Armor;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.LeatherArmor;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.MailArmor;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.PlateArmor;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.ScaleArmor;
import com.pumpkin.dgx_pixel_dungeon.core.items.trinkets.ParchmentScrap;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.Weapon;
import com.pumpkin.dgx_pixel_dungeon.core.journal.Notes;
import com.pumpkin.dgx_pixel_dungeon.core.levels.SewerLevel;
import com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.Room;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.GhostSprite;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.core.windows.WndQuest;
import com.pumpkin.dgx_pixel_dungeon.core.windows.WndSadGhost;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Game;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Music;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Callback;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class Ghost extends NPC {

	{
		spriteClass = GhostSprite.class;
		
		flying = true;

		WANDERING = new Wandering();
		state = WANDERING;

		//not actually large of course, but this makes the ghost stick to the exit room
		properties.add(Property.LARGE);
	}

	protected class Wandering extends Mob.Wandering{
		@Override
		protected int randomDestination() {
			int pos = super.randomDestination();
			//cannot wander onto heaps or the level exit
			if (Dungeon.level.heaps.get(pos) != null || pos == Dungeon.level.exit()){
				return -1;
			}
			return pos;
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			return true;
		}
		if (Dungeon.level.heroFOV[pos] && !Quest.completed()){
			Notes.add( Notes.Landmark.GHOST );
		}
		return super.act();
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}

	@Override
	public void damage( int dmg, Object src ) {
		//do nothing
	}

	@Override
	public boolean add( Buff buff ) {
		return false;
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );
		
		Sample.INSTANCE.play( Assets.Sounds.GHOST );

		if (c != Dungeon.hero){
			return super.interact(c);
		}
		
		if (Quest.given) {
			if (Quest.weapon != null) {
				if (Quest.processed) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndSadGhost(Ghost.this, Quest.type));
						}
					});
				} else {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							switch (Quest.type) {
								case 1:
								default:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "rat_2")));
									break;
								case 2:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "gnoll_2")));
									break;
								case 3:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "crab_2")));
									break;
							}
						}
					});

				}
			}
		} else {
			Mob questBoss;
			String txt_quest;

			switch (Quest.type){
				case 1: default:
					questBoss = new FetidRat();
					txt_quest = Messages.get(this, "rat_1", Messages.titleCase(Dungeon.hero.name())); break;
				case 2:
					questBoss = new GnollTrickster();
					txt_quest = Messages.get(this, "gnoll_1", Messages.titleCase(Dungeon.hero.name())); break;
				case 3:
					questBoss = new GreatCrab();
					txt_quest = Messages.get(this, "crab_1", Messages.titleCase(Dungeon.hero.name())); break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell( this );

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				Quest.given = true;
				Notes.add( Notes.Landmark.GHOST );
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndQuest( Ghost.this, txt_quest ){
							@Override
							public void hide() {
								super.hide();
								Music.INSTANCE.fadeOut(1f, new Callback() {
									@Override
									public void call() {
										if (Dungeon.level != null) {
											Dungeon.level.playLevelMusic();
										}
									}
								});
							}
						} );
					}
				});
			}

		}

		return true;
	}

	public static class Quest {
		
		private static boolean spawned;

		private static int type;

		private static boolean given;
		private static boolean processed;
		
		private static int depth;
		
		public static Weapon weapon;
		public static Armor armor;
		public static Weapon.Enchantment enchant;
		public static Armor.Glyph glyph;
		
		public static void reset() {
			spawned = false;
			
			weapon = null;
			armor = null;
			enchant = null;
			glyph = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE        = "type";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		private static final String ENCHANT		= "enchant";
		private static final String GLYPH		= "glyph";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed );
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );

				if (enchant != null) {
					node.put(ENCHANT, enchant);
					node.put(GLYPH, glyph);
				}
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				type = node.getInt(TYPE);
				given	= node.getBoolean( GIVEN );
				processed = node.getBoolean( PROCESSED );

				depth	= node.getInt( DEPTH );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );

				if (node.contains(ENCHANT)) {
					enchant = (Weapon.Enchantment) node.get(ENCHANT);
					glyph   = (Armor.Glyph) node.get(GLYPH);
				}
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level, Room room ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.pointToCell(room.random());
				} while (ghost.pos == -1 || ghost.pos == level.exit());
				level.mobs.add( ghost );
				
				spawned = true;
				//dungeon depth determines type of quest.
				//depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth-1;
				
				given = false;
				processed = false;
				depth = Dungeon.depth;

				//50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
				switch (Random.chances(new float[]{0, 0, 10, 6, 3, 1})){
					default:
					case 2: armor = new LeatherArmor(); break;
					case 3: armor = new MailArmor();    break;
					case 4: armor = new ScaleArmor();   break;
					case 5: armor = new PlateArmor();   break;
				}
				//50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
				int wepTier = Random.chances(new float[]{0, 0, 10, 6, 3, 1});
				weapon = (Weapon) Generator.random(Generator.wepTiers[wepTier - 1]);

				//clear weapon's starting properties
				weapon.level(0);
				weapon.enchant(null);
				weapon.cursed = false;

				//50%:+0, 30%:+1, 15%:+2, 5%:+3
				float itemLevelRoll = Random.Float();
				int itemLevel;
				if (itemLevelRoll < 0.5f){
					itemLevel = 0;
				} else if (itemLevelRoll < 0.8f){
					itemLevel = 1;
				} else if (itemLevelRoll < 0.95f){
					itemLevel = 2;
				} else {
					itemLevel = 3;
				}
				weapon.upgrade(itemLevel);
				armor.upgrade(itemLevel);

				// 20% base chance to be enchanted, stored separately so status isn't revealed early
				float enchantRoll = Random.Float();
				if (enchantRoll < 0.2f * ParchmentScrap.enchantChanceMultiplier()){
					enchant = Weapon.Enchantment.random();
					glyph = Armor.Glyph.random();
				}

			}
		}
		
		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n( Messages.get(Ghost.class, "find_me") );
				Sample.INSTANCE.play( Assets.Sounds.GHOST );
				processed = true;
				Statistics.questScores[0] = 1000;

				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						Music.INSTANCE.fadeOut(1f, new Callback() {
							@Override
							public void call() {
								if (Dungeon.level != null) {
									Dungeon.level.playLevelMusic();
								}
							}
						});
					}
				});
			}
		}

		public static boolean active(){
			return spawned && given && !processed && depth == Dungeon.depth;
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Notes.remove( Notes.Landmark.GHOST );
		}

		public static boolean processed(){
			return spawned && processed;
		}
		
		public static boolean completed(){
			return processed() && weapon == null && armor == null;
		}
	}
}
