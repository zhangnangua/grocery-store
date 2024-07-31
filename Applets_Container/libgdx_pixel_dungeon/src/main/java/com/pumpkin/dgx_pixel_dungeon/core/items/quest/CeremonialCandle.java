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

package com.pumpkin.dgx_pixel_dungeon.core.items.quest;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Elemental;
import com.pumpkin.dgx_pixel_dungeon.core.effects.CellEmitter;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.ElmoParticle;
import com.pumpkin.dgx_pixel_dungeon.core.items.Heap;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.levels.PrisonLevel;
import com.pumpkin.dgx_pixel_dungeon.core.levels.RegularLevel;
import com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.quest.RitualSiteRoom;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.GameScene;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.particles.Emitter;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PathFinder;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

import java.util.ArrayList;


public class CeremonialCandle extends Item {

	//generated with the wandmaker quest
	public static int ritualPos;

	{
		image = ItemSpriteSheet.CANDLE;

		defaultAction = AC_THROW;

		unique = true;
		stackable = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public void doDrop(Hero hero) {
		super.doDrop(hero);
		aflame = false;
		checkCandles();
	}

	@Override
	protected void onThrow(int cell) {
		super.onThrow(cell);
		aflame = false;
		checkCandles();
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		if (super.doPickUp(hero, pos)){
			aflame = false;
			return true;
		}
		return false;
	}

	public boolean aflame = false;

	public static String AFLAME = "aflame";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(AFLAME, aflame);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		aflame = bundle.getBoolean(AFLAME);
	}

	@Override
	public Emitter emitter() {
		if (aflame) {
			Emitter emitter = new Emitter();
			emitter.pos(6, 0);
			emitter.fillTarget = false;
			emitter.pour(ElmoParticle.FACTORY, 0.25f);
			return emitter;
		}
		return super.emitter();
	}

	private static void checkCandles(){
		if (!(Dungeon.level instanceof RegularLevel)){
			return;
		}

		if (!(((RegularLevel) Dungeon.level).room(ritualPos) instanceof RitualSiteRoom)){
			return;
		}

		Heap[] candleHeaps = new Heap[4];

		candleHeaps[0] = Dungeon.level.heaps.get(ritualPos - Dungeon.level.width());
		candleHeaps[1] = Dungeon.level.heaps.get(ritualPos + 1);
		candleHeaps[2] = Dungeon.level.heaps.get(ritualPos + Dungeon.level.width());
		candleHeaps[3] = Dungeon.level.heaps.get(ritualPos - 1);

		boolean allCandles = true;
		for (Heap h : candleHeaps){
			if (h != null && h.type == Heap.Type.HEAP){
				boolean foundCandle = false;
				for (Item i : h.items){
					if (i instanceof CeremonialCandle){
						if (!((CeremonialCandle) i).aflame) {
							((CeremonialCandle) i).aflame = true;
							h.sprite.view(h).place(h.pos);
						}
						foundCandle = true;
					}
				}
				if (!foundCandle){
					allCandles = false;
				}
			} else {
				allCandles = false;
			}
		}

		if (allCandles){

			for (Heap h : candleHeaps) {
				for (Item i : h.items.toArray(new Item[0])){
					if (i instanceof CeremonialCandle){
						h.remove(i);
					}
				}
			}
				
			Elemental.NewbornFireElemental elemental = new Elemental.NewbornFireElemental();
			Char ch = Actor.findChar( ritualPos );
			if (ch != null) {
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int n : PathFinder.NEIGHBOURS8) {
					int cell = ritualPos + n;
					if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Actor.findChar( cell ) == null) {
						candidates.add( cell );
					}
				}
				if (candidates.size() > 0) {
					elemental.pos = Random.element( candidates );
				} else {
					elemental.pos = ritualPos;
				}
			} else {
				elemental.pos = ritualPos;
			}
			elemental.state = elemental.HUNTING;
			GameScene.add(elemental, 1);

			if (Dungeon.level instanceof PrisonLevel){
				((PrisonLevel) Dungeon.level).updateWandmakerQuestMusic();
			}

			for (int i : PathFinder.NEIGHBOURS9){
				CellEmitter.get(ritualPos+i).burst(ElmoParticle.FACTORY, 10);
			}
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
		}

	}
}
