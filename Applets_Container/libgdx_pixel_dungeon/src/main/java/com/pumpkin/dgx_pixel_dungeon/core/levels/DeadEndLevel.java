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

package com.pumpkin.dgx_pixel_dungeon.core.levels;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Bones;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mob;
import com.pumpkin.dgx_pixel_dungeon.core.items.Heap;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.levels.features.LevelTransition;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

import java.util.ArrayList;

public class DeadEndLevel extends Level {

	private static final int SIZE = 5;
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {
		
		setSize(7, 7);
		
		for (int i=2; i < SIZE; i++) {
			for (int j=2; j < SIZE; j++) {
				map[i * width() + j] = Terrain.EMPTY;
			}
		}
		
		for (int i=1; i <= SIZE; i++) {
			map[width() + i] =
			map[width() * SIZE + i] =
			map[width() * i + 1] =
			map[width() * i + SIZE] =
				Terrain.WATER;
		}
		
		int entrance = SIZE * width() + SIZE / 2 + 1;

		//different exit behaviour depending on main branch or side one
		if (Dungeon.branch == 0) {
			transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
		} else {
			transitions.add(new LevelTransition(this,
					entrance,
					LevelTransition.Type.BRANCH_ENTRANCE,
					Dungeon.depth,
					0,
					LevelTransition.Type.BRANCH_EXIT));
		}
		map[entrance] = Terrain.ENTRANCE;
		
		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Random.pushGenerator(Random.Long());
			ArrayList<Item> bonesItems = Bones.get();
			if (bonesItems != null) {
				for (Item i : bonesItems) {
					drop(i, entrance()-width()).setHauntedIfCursed().type = Heap.Type.REMAINS;
				}
			}
		Random.popGenerator();
	}
	
	@Override
	public int randomRespawnCell( Char ch ) {
		return entrance()-width();
	}

}