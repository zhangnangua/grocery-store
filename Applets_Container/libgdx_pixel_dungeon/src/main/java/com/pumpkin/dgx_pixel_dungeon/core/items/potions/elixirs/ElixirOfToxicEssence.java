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

package com.pumpkin.dgx_pixel_dungeon.core.items.potions.elixirs;

import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.ToxicImbue;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.PoisonParticle;
import com.pumpkin.dgx_pixel_dungeon.core.items.potions.exotic.PotionOfCorrosiveGas;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;

public class ElixirOfToxicEssence extends Elixir {
	
	{
		image = ItemSpriteSheet.ELIXIR_TOXIC;
	}
	
	@Override
	public void apply(Hero hero) {
		Buff.affect(hero, ToxicImbue.class).set(ToxicImbue.DURATION);
		hero.sprite.emitter().burst(PoisonParticle.SPLASH, 10);
	}
	
	public static class Recipe extends com.pumpkin.dgx_pixel_dungeon.core.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfCorrosiveGas.class};
			inQuantity = new int[]{1};
			
			cost = 8;
			
			output = ElixirOfToxicEssence.class;
			outQuantity = 1;
		}
		
	}
	
}
