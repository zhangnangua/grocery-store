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

package com.pumpkin.dgx_pixel_dungeon.core.items.weapon.missiles;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Badges;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.items.wands.WandOfBlastWave;
import com.pumpkin.dgx_pixel_dungeon.core.levels.traps.TenguDartTrap;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PathFinder;

import java.util.ArrayList;

public class ForceCube extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.FORCE_CUBE;
		
		tier = 5;
		baseUses = 5;
		
		sticky = false;
	}

	@Override
	public void hitSound(float pitch) {
		//no hitsound as it never hits enemies directly
	}

	@Override
	protected void onThrow(int cell) {
		if (Dungeon.level.pit[cell]){
			super.onThrow(cell);
			return;
		}

		rangedHit( null, cell );
		Dungeon.level.pressCell(cell);
		
		ArrayList<Char> targets = new ArrayList<>();
		if (Actor.findChar(cell) != null) targets.add(Actor.findChar(cell));
		
		for (int i : PathFinder.NEIGHBOURS8){
			if (!(Dungeon.level.traps.get(cell+i) instanceof TenguDartTrap)) Dungeon.level.pressCell(cell+i);
			if (Actor.findChar(cell + i) != null) targets.add(Actor.findChar(cell + i));
		}
		
		for (Char target : targets){
			curUser.shoot(target, this);
			if (target == Dungeon.hero && !target.isAlive()){
				Badges.validateDeathFromFriendlyMagic();
				Dungeon.fail(this);
				GLog.n(Messages.get(this, "ondeath"));
			}
		}
		
		WandOfBlastWave.BlastWave.blast(cell);
		Sample.INSTANCE.play( Assets.Sounds.BLAST );
	}
}
