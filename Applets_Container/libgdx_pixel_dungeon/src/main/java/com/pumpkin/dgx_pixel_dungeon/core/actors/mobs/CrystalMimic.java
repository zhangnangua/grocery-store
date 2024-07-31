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
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Haste;
import com.pumpkin.dgx_pixel_dungeon.core.actors.hero.Hero;
import com.pumpkin.dgx_pixel_dungeon.core.effects.CellEmitter;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.items.Heap;
import com.pumpkin.dgx_pixel_dungeon.core.items.Honeypot;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.items.artifacts.Artifact;
import com.pumpkin.dgx_pixel_dungeon.core.items.rings.Ring;
import com.pumpkin.dgx_pixel_dungeon.core.items.scrolls.ScrollOfTeleportation;
import com.pumpkin.dgx_pixel_dungeon.core.items.trinkets.MimicTooth;
import com.pumpkin.dgx_pixel_dungeon.core.items.wands.Wand;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.MimicSprite;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PathFinder;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

import java.util.ArrayList;

public class CrystalMimic extends Mimic {

	{
		spriteClass = MimicSprite.Crystal.class;

		FLEEING = new Fleeing();
	}

	@Override
	public String name() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(Heap.class, "crystal_chest");
		} else {
			return super.name();
		}
	}

	@Override
	public String description() {
		if (alignment == Alignment.NEUTRAL){
			String desc = null;
			for (Item i : items){
				if (i instanceof Artifact){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "artifact"));
					break;
				} else if (i instanceof Ring){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "ring"));
					break;
				} else if (i instanceof Wand){
					desc = Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "wand"));
					break;
				}
			}
			if (desc == null) {
				desc = Messages.get(Heap.class, "locked_chest_desc");
			}
			if (!MimicTooth.stealthyMimics()){
				desc += "\n\n" + Messages.get(this, "hidden_hint");
			}
			return desc;
		} else {
			return super.description();
		}
	}

	//does not deal bonus damage, steals instead. See attackProc
	@Override
	public int damageRoll() {
		if (alignment == Alignment.NEUTRAL) {
			alignment = Alignment.ENEMY;
			int dmg = super.damageRoll();
			alignment = Alignment.NEUTRAL;
			return dmg;
		} else {
			return super.damageRoll();
		}
	}

	public void stopHiding(){
		state = FLEEING;
		if (sprite != null) sprite.idle();
		//haste for 2 turns if attacking
		if (alignment == Alignment.NEUTRAL){
			Buff.affect(this, Haste.class, 2f);
		} else {
			Buff.affect(this, Haste.class, 1f);
		}
		if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
			enemy = Dungeon.hero;
			target = Dungeon.hero.pos;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1.25f);
		}
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (alignment == Alignment.NEUTRAL && enemy == Dungeon.hero){
			steal( Dungeon.hero );

		} else {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
					candidates.add(pos + i);
				}
			}

			if (!candidates.isEmpty()){
				ScrollOfTeleportation.appear(enemy, Random.element(candidates));
			}

			if (alignment == Alignment.ENEMY) state = FLEEING;
		}
		return super.attackProc(enemy, damage);
	}

	protected void steal( Hero hero ) {

		int tries = 10;
		Item item;
		do {
			item = hero.belongings.randomUnequipped();
		} while (tries-- > 0 && (item == null || item.unique || item.level() > 0));

		if (item != null && !item.unique && item.level() < 1 ) {

			GLog.w( Messages.get(this, "ate", item.name()) );
			if (!item.stackable) {
				Dungeon.quickslot.convertToPlaceholder(item);
			}
			item.updateQuickslot();

			if (item instanceof Honeypot){
				items.add(((Honeypot)item).shatter(this, this.pos));
				item.detach( hero.belongings.backpack );
			} else {
				items.add(item.detach( hero.belongings.backpack ));
				if ( item instanceof Honeypot.ShatteredPot)
					((Honeypot.ShatteredPot)item).pickupPot(this);
			}

		}
	}

	@Override
	protected void generatePrize( boolean useDecks ) {
		//Crystal mimic already contains a prize item. Just guarantee it isn't cursed.
		for (Item i : items){
			i.cursed = false;
			i.cursedKnown = true;
		}
	}

	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void escaped() {
			if (!Dungeon.level.heroFOV[pos] && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {
				GLog.n(Messages.get(CrystalMimic.class, "escaped"));
				destroy();
				sprite.killAndErase();
			} else {
				state = WANDERING;
			}
		}

		@Override
		protected void nowhereToRun() {
			super.nowhereToRun();
			if (state == HUNTING){
				spend(-TICK); //crystal mimics are fast!
			}
		}
	}

}
