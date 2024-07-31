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

package com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard;

import com.pumpkin.dgx_pixel_dungeon.core.levels.Level;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Terrain;
import com.pumpkin.dgx_pixel_dungeon.core.levels.painters.Painter;
import com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.Room;
import com.pumpkin.dgx_pixel_dungeon.core.levels.traps.BurningTrap;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Point;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class BurnedRoom extends PatchRoom {
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 1, 0};
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		int cell = l.pointToCell(pointInside(p, 1));
		return l.map[cell] == Terrain.EMPTY;
	}

	@Override
	protected float fill() {
		//past 8x8 each point of width/height decreases fill by 3%
		// e.g. a 14x14 burned room has a fill of 54%
		return Math.min( 1f, 1.48f - (width()+height())*0.03f);
	}

	@Override
	protected int clustering() {
		return 2;
	}

	@Override
	protected boolean ensurePath() {
		return false;
	}

	@Override
	protected boolean cleanEdges() {
		return false;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		setupPatch(level);
		
		for (int i=top + 1; i < bottom; i++) {
			for (int j=left + 1; j < right; j++) {
				if (!patch[xyToPatchCoords(j, i)])
					continue;
				int cell = i * level.width() + j;
				int t;
				switch (Random.Int( 5 )) {
					case 0: default:
						t = Terrain.EMPTY;
						break;
					case 1:
						t = Terrain.EMBERS;
						break;
					case 2:
						t = Terrain.TRAP;
						level.setTrap(new BurningTrap().reveal(), cell);
						break;
					case 3:
						t = Terrain.SECRET_TRAP;
						level.setTrap(new BurningTrap().hide(), cell);
						break;
					case 4:
						t = Terrain.INACTIVE_TRAP;
						BurningTrap trap = new BurningTrap();
						trap.reveal().active = false;
						level.setTrap(trap, cell);
						break;
				}
				level.map[cell] = t;
			}
		}
	}
	
	@Override
	public boolean canPlaceWater(Point p) {
		return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
	}
	
}
