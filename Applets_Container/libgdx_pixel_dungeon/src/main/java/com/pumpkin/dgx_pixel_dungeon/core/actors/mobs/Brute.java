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

import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.AscensionChallenge;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.ShieldBuff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Terror;
import com.pumpkin.dgx_pixel_dungeon.core.effects.FloatingText;
import com.pumpkin.dgx_pixel_dungeon.core.effects.SpellSprite;
import com.pumpkin.dgx_pixel_dungeon.core.items.Gold;
import com.pumpkin.dgx_pixel_dungeon.core.levels.features.Chasm;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.BruteSprite;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.CharSprite;
import com.pumpkin.dgx_pixel_dungeon.core.ui.BuffIndicator;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;

public class Brute extends Mob {
	
	{
		spriteClass = BruteSprite.class;
		
		HP = HT = 40;
		defenseSkill = 15;
		
		EXP = 8;
		maxLvl = 16;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	protected boolean hasRaged = false;
	
	@Override
	public int damageRoll() {
		return buff(BruteRage.class) != null ?
			Char.combatRoll( 15, 40 ) :
			Char.combatRoll( 5, 25 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Char.combatRoll(0, 8);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

		if (cause == Chasm.class){
			hasRaged = true; //don't let enrage trigger for chasm deaths
		}
	}

	@Override
	public synchronized boolean isAlive() {
		if (super.isAlive()){
			return true;
		} else {
			if (!hasRaged){
				triggerEnrage();
			}
			return !buffs(BruteRage.class).isEmpty();
		}
	}
	
	protected void triggerEnrage(){
		Buff.affect(this, BruteRage.class).setShield(HT/2 + 4);
		sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(HT/2 + 4), FloatingText.SHIELDING );
		if (Dungeon.level.heroFOV[pos]) {
			SpellSprite.show( this, SpellSprite.BERSERK);
		}
		spend( TICK );
		hasRaged = true;
	}
	
	private static final String HAS_RAGED = "has_raged";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HAS_RAGED, hasRaged);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		hasRaged = bundle.getBoolean(HAS_RAGED);
	}
	
	public static class BruteRage extends ShieldBuff {
		
		{
			type = buffType.POSITIVE;
		}
		
		@Override
		public boolean act() {
			
			if (target.HP > 0){
				detach();
				return true;
			}
			
			absorbDamage( Math.round(4*AscensionChallenge.statModifier(target)));
			
			if (shielding() <= 0){
				target.die(null);
			}
			
			spend( TICK );
			
			return true;
		}
		
		@Override
		public int icon () {
			return BuffIndicator.FURY;
		}
		
		@Override
		public String desc () {
			return Messages.get(this, "desc", shielding());
		}

		{
			immunities.add(Terror.class);
		}
	}
}
