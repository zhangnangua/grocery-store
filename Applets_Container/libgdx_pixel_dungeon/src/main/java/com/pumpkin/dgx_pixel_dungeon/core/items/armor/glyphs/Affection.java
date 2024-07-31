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

package com.pumpkin.dgx_pixel_dungeon.core.items.armor.glyphs;

import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Charm;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.Armor;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.Armor.Glyph;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSprite;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSprite.Glowing;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class Affection extends Glyph {
	
	private static Glowing PINK = new Glowing( 0xFF4488 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.buffedLvl());
		
		// lvl 0 - 15%
		// lvl 1 ~ 19%
		// lvl 2 ~ 23%
		float procChance = (level+3f)/(level+20f) * procChanceMultiplier(defender);
		if (Random.Float() < procChance) {

			float powerMulti = Math.max(1f, procChance);

			Buff.affect( attacker, Charm.class, Math.round(Charm.DURATION*powerMulti) ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

		}
		
		return damage;
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}
