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

package com.pumpkin.dgx_pixel_dungeon.core.actors.buffs;

import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Talent;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.ui.BuffIndicator;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Image;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;

public class HoldFast extends Buff {

	{
		type = buffType.POSITIVE;
	}

	public int pos = -1;

	@Override
	public boolean act() {
		if (pos != target.pos) {
			detach();
		} else {
			spend(TICK);
		}
		return true;
	}

	public int armorBonus(){
		if (pos == target.pos && target instanceof Hero){
			return Char.combatRoll(0, 2* ((Hero) target).pointsInTalent(Talent.HOLD_FAST));
		} else {
			detach();
			return 0;
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1.9f, 2.4f, 3.25f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 2*Dungeon.hero.pointsInTalent(Talent.HOLD_FAST));
	}

	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(POS, pos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		pos = bundle.getInt(POS);
	}
}
