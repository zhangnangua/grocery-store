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

package com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.entrance;

import com.pumpkin.dgx_pixel_dungeon.core.levels.Level;
import com.pumpkin.dgx_pixel_dungeon.core.levels.Terrain;
import com.pumpkin.dgx_pixel_dungeon.core.levels.features.LevelTransition;
import com.pumpkin.dgx_pixel_dungeon.core.levels.painters.Painter;
import com.pumpkin.dgx_pixel_dungeon.core.levels.rooms.standard.HallwayRoom;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Point;

public class HallwayEntranceRoom extends HallwayRoom {

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int entrance = -1;
		for ( Point p : getPoints()){
			if (level.map[level.pointToCell(p)] == Terrain.STATUE_SP){
				entrance = level.pointToCell(p);
				break;
			}
		}
		Painter.set( level, entrance, Terrain.ENTRANCE_SP );
		level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

	}
}
