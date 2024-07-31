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

package com.pumpkin.dgx_pixel_dungeon.core.actors.mobs;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.ShaftParticle;
import com.pumpkin.dgx_pixel_dungeon.core.items.Generator;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.Armor;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.melee.MeleeWeapon;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.TormentedSpiritSprite;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class TormentedSpirit extends Wraith {

	{
		spriteClass = TormentedSpiritSprite.class;
	}

	//50% more damage scaling than regular wraiths
	@Override
	public int damageRoll() {
		return Char.combatRoll( 1 + Math.round(1.5f*level)/2, 2 + Math.round(1.5f*level) );
	}

	//50% more accuracy (and by extension evasion) scaling than regular wraiths
	@Override
	public int attackSkill( Char target ) {
		return 10 + Math.round(1.5f*level);
	}

	public void cleanse(){
		Sample.INSTANCE.play( Assets.Sounds.GHOST );
		yell(Messages.get(this, "thank_you"));

		//50/50 between weapon or armor, always uncursed
		Item prize;
		if (Random.Int(2) == 0){
			prize = Generator.randomWeapon(true);
			if (((MeleeWeapon)prize).hasCurseEnchant()){
				((MeleeWeapon) prize).enchantment = null;
			}
		} else {
			prize = Generator.randomArmor();
			if (((Armor) prize).hasCurseGlyph()){
				((Armor) prize).glyph = null;
			}
		}
		prize.cursed = false;
		prize.cursedKnown = true;

		Dungeon.level.drop(prize, pos).sprite.drop();

		destroy();
		sprite.die();
		sprite.tint(1, 1, 1, 1);
		sprite.emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
		sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );

	}

}
