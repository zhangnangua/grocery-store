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

import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.ui.BuffIndicator;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Image;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;

public class WandEmpower extends Buff {

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon() {
		return BuffIndicator.UPGRADE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 1, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (3-left) / 3f);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(left);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dmgBoost, left);
	}

	public int dmgBoost;
	public int left;

	public void set(int dmg, int shots){
		dmgBoost = dmg;
		left = Math.max(left, shots);
	}

	private static final String BOOST = "boost";
	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( BOOST, dmgBoost );
		bundle.put( LEFT, left );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		dmgBoost = bundle.getInt( BOOST );
		left = bundle.getInt( LEFT );
	}

}
