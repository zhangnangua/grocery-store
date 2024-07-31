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

package com.pumpkin.dgx_pixel_dungeon.core.items.stones;

import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Belongings;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Enchanting;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.items.armor.Armor;
import com.pumpkin.dgx_pixel_dungeon.core.items.scrolls.exotic.ScrollOfEnchantment;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.Weapon;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;

public class StoneOfEnchantment extends InventoryStone {
	
	{
		preferredBag = Belongings.Backpack.class;
		image = ItemSpriteSheet.STONE_ENCHANT;

		unique = true;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return ScrollOfEnchantment.enchantable(item);
	}
	
	@Override
	protected void onItemSelected(Item item) {
		curItem.detach( curUser.belongings.backpack );
		
		if (item instanceof Weapon) {
			
			((Weapon)item).enchant();
			
		} else {
			
			((Armor)item).inscribe();
			
		}
		
		curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
		Enchanting.show( curUser, item );
		
		if (item instanceof Weapon) {
			GLog.p(Messages.get(this, "weapon"));
		} else {
			GLog.p(Messages.get(this, "armor"));
		}
		
		useAnimation();
		
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}

	@Override
	public int energyVal() {
		return 5 * quantity;
	}

}
