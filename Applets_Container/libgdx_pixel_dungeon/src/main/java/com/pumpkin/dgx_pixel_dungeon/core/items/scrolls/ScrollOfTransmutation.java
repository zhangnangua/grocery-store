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

package com.pumpkin.dgx_pixel_dungeon.core.items.scrolls;

import com.pumpkin.dgx_pixel_dungeon.core.Challenges;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Transmuting;
import com.pumpkin.dgx_pixel_dungeon.core.items.EquipableItem;
import com.pumpkin.dgx_pixel_dungeon.core.items.Generator;
import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.items.KindOfWeapon;
import com.pumpkin.dgx_pixel_dungeon.core.items.artifacts.Artifact;
import com.pumpkin.dgx_pixel_dungeon.core.items.artifacts.DriedRose;
import com.pumpkin.dgx_pixel_dungeon.core.items.potions.Potion;
import com.pumpkin.dgx_pixel_dungeon.core.items.potions.brews.Brew;
import com.pumpkin.dgx_pixel_dungeon.core.items.potions.elixirs.Elixir;
import com.pumpkin.dgx_pixel_dungeon.core.items.potions.exotic.ExoticPotion;
import com.pumpkin.dgx_pixel_dungeon.core.items.quest.Pickaxe;
import com.pumpkin.dgx_pixel_dungeon.core.items.rings.Ring;
import com.pumpkin.dgx_pixel_dungeon.core.items.scrolls.exotic.ExoticScroll;
import com.pumpkin.dgx_pixel_dungeon.core.items.stones.Runestone;
import com.pumpkin.dgx_pixel_dungeon.core.items.trinkets.Trinket;
import com.pumpkin.dgx_pixel_dungeon.core.items.wands.Wand;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.Weapon;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.melee.MagesStaff;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.melee.MeleeWeapon;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.missiles.MissileWeapon;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.missiles.darts.Dart;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.missiles.darts.TippedDart;
import com.pumpkin.dgx_pixel_dungeon.core.journal.Catalog;
import com.pumpkin.dgx_pixel_dungeon.core.levels.MiningLevel;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.plants.Plant;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Reflection;

public class ScrollOfTransmutation extends InventoryScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_TRANSMUTE;
		
		bones = true;

		talentFactor = 2f;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		//all melee weapons, except pickaxe when in a mining level
		if (item instanceof MeleeWeapon){
			return !(item instanceof Pickaxe && Dungeon.level instanceof MiningLevel);

		//all missile weapons except untipped darts
		} else if (item instanceof MissileWeapon){
			return item.getClass() != Dart.class;

		//all regular or exotic potions. No brews or elixirs
		} else if (item instanceof Potion){
			return !(item instanceof Elixir || item instanceof Brew);

		//all regular or exotic scrolls, except itself
		} else if (item instanceof Scroll){
			return item != this || item.quantity() > 1;

		//all rings, wands, artifacts, trinkets, seeds, and runestones
		} else {
			return item instanceof Ring || item instanceof Wand || item instanceof Artifact
					|| item instanceof Trinket || item instanceof Plant.Seed
					|| item instanceof Runestone;
		}
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		Item result = changeItem(item);
		
		if (result == null){
			//This shouldn't ever trigger
			GLog.n( Messages.get(this, "nothing") );
			curItem.collect( curUser.belongings.backpack );
		} else {
			if (result != item) {
				int slot = Dungeon.quickslot.getSlot(item);
				if (item.isEquipped(Dungeon.hero)) {
					item.cursed = false; //to allow it to be unequipped
					if (item instanceof Artifact && result instanceof Ring){
						//if we turned an equipped artifact into a ring, ring goes into inventory
						((EquipableItem) item).doUnequip(Dungeon.hero, false);
						if (!result.collect()){
							Dungeon.level.drop(result, curUser.pos).sprite.drop();
						}
					} else if (item instanceof KindOfWeapon && Dungeon.hero.belongings.secondWep() == item){
						((EquipableItem) item).doUnequip(Dungeon.hero, false);
						((KindOfWeapon) result).equipSecondary(Dungeon.hero);
					} else {
						((EquipableItem) item).doUnequip(Dungeon.hero, false);
						((EquipableItem) result).doEquip(Dungeon.hero);
					}
					Dungeon.hero.spend(-Dungeon.hero.cooldown()); //cancel equip/unequip time
				} else {
					item.detach(Dungeon.hero.belongings.backpack);
					if (!result.collect()) {
						Dungeon.level.drop(result, curUser.pos).sprite.drop();
					} else if (result.stackable && Dungeon.hero.belongings.getSimilar(result) != null){
						result = Dungeon.hero.belongings.getSimilar(result);
					}
				}
				if (slot != -1
						&& result.defaultAction() != null
						&& !Dungeon.quickslot.isNonePlaceholder(slot)
						&& Dungeon.hero.belongings.contains(result)){
					Dungeon.quickslot.setSlot(slot, result);
				}
			}
			if (result.isIdentified()){
				Catalog.setSeen(result.getClass());
			}
			Transmuting.show(curUser, item, result);
			curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
			GLog.p( Messages.get(this, "morph") );
		}
		
	}

	public static Item changeItem( Item item ){
		if (item instanceof MagesStaff) {
			return changeStaff((MagesStaff) item);
		}else if (item instanceof TippedDart){
			return changeTippedDart( (TippedDart)item );
		} else if (item instanceof MeleeWeapon || item instanceof MissileWeapon) {
			return changeWeapon( (Weapon)item );
		} else if (item instanceof Scroll) {
			return changeScroll( (Scroll)item );
		} else if (item instanceof Potion) {
			return changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			return changeRing( (Ring)item );
		} else if (item instanceof Wand) {
			return changeWand( (Wand)item );
		} else if (item instanceof Plant.Seed) {
			return changeSeed((Plant.Seed) item);
		} else if (item instanceof Runestone) {
			return changeStone((Runestone) item);
		} else if (item instanceof Artifact) {
			Artifact a = changeArtifact( (Artifact)item );
			if (a == null){
				//if no artifacts are left, generate a random +0 ring with shared ID/curse state
				Item result = Generator.randomUsingDefaults(Generator.Category.RING);
				result.levelKnown = item.levelKnown;
				result.cursed = item.cursed;
				result.cursedKnown = item.cursedKnown;
				result.level(0);
				return result;
			} else {
				return a;
			}
		} else if (item instanceof Trinket) {
			return changeTrinket( (Trinket)item );
		} else {
			return null;
		}
	}
	
	private static MagesStaff changeStaff( MagesStaff staff ){
		Class<?extends Wand> wandClass = staff.wandClass();
		
		if (wandClass == null){
			return null;
		} else {
			Wand n;
			do {
				n = (Wand) Generator.randomUsingDefaults(Generator.Category.WAND);
			} while (Challenges.isItemBlocked(n) || n.getClass() == wandClass);
			n.level(0);
			n.identify();
			staff.imbueWand(n, null);
		}
		
		return staff;
	}

	private static TippedDart changeTippedDart( TippedDart dart ){
		TippedDart n;
		do {
			n = TippedDart.randomTipped(1);
		} while (n.getClass() == dart.getClass());

		return n;
	}
	
	private static Weapon changeWeapon( Weapon w ) {
		Weapon n;
		Generator.Category c;
		if (w instanceof MeleeWeapon) {
			c = Generator.wepTiers[((MeleeWeapon)w).tier - 1];
		} else {
			c = Generator.misTiers[((MissileWeapon)w).tier - 1];
		}
		
		do {
			n = (Weapon)Generator.randomUsingDefaults(c);
		} while (Challenges.isItemBlocked(n) || n.getClass() == w.getClass());

		n.level(0);
		n.quantity(1);
		int level = w.trueLevel();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.enchantment = w.enchantment;
		n.curseInfusionBonus = w.curseInfusionBonus;
		n.masteryPotionBonus = w.masteryPotionBonus;
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.augment = w.augment;
		n.enchantHardened = w.enchantHardened;
		
		return n;
		
	}
	
	private static Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.randomUsingDefaults( Generator.Category.RING );
		} while (Challenges.isItemBlocked(n) || n.getClass() == r.getClass());
		
		n.level(0);
		
		int level = r.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;
		
		return n;
	}
	
	private static Artifact changeArtifact( Artifact a ) {
		Artifact n;
		do {
			n = Generator.randomArtifact();
		} while ( n != null && (Challenges.isItemBlocked(n) || n.getClass() == a.getClass()));
		
		if (n != null){

			if (a instanceof DriedRose){
				if (((DriedRose) a).ghostWeapon() != null){
					Dungeon.level.drop(((DriedRose) a).ghostWeapon(), Dungeon.hero.pos);
				}
				if (((DriedRose) a).ghostArmor() != null){
					Dungeon.level.drop(((DriedRose) a).ghostArmor(), Dungeon.hero.pos);
				}
			}

			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
			return n;
		}
		
		return null;
	}

	private static Trinket changeTrinket( Trinket t ){
		Trinket n;
		do {
			n = (Trinket)Generator.random(Generator.Category.TRINKET);
		} while ( Challenges.isItemBlocked(n) || n.getClass() == t.getClass());

		n.level(t.trueLevel());
		n.levelKnown = t.levelKnown;
		n.cursed = t.cursed;

		return n;
	}
	
	private static Wand changeWand( Wand w ) {
		Wand n;
		do {
			n = (Wand)Generator.randomUsingDefaults( Generator.Category.WAND );
		} while ( Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
		
		n.level( 0 );
		int level = w.trueLevel();
		n.upgrade( level );

		n.levelKnown = w.levelKnown;
		n.curChargeKnown = w.curChargeKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.curseInfusionBonus = w.curseInfusionBonus;
		n.resinBonus = w.resinBonus;

		n.curCharges =  w.curCharges;
		n.updateLevel();
		
		return n;
	}
	
	private static Plant.Seed changeSeed( Plant.Seed s ) {
		Plant.Seed n;
		
		do {
			n = (Plant.Seed)Generator.randomUsingDefaults( Generator.Category.SEED );
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private static Runestone changeStone( Runestone r ) {
		Runestone n;
		
		do {
			n = (Runestone) Generator.randomUsingDefaults( Generator.Category.STONE );
		} while (n.getClass() == r.getClass());
		
		return n;
	}

	private static Scroll changeScroll( Scroll s ) {
		if (s instanceof ExoticScroll) {
			return Reflection.newInstance(ExoticScroll.exoToReg.get(s.getClass()));
		} else {
			return Reflection.newInstance(ExoticScroll.regToExo.get(s.getClass()));
		}
	}

	private static Potion changePotion( Potion p ) {
		if	(p instanceof ExoticPotion) {
			return Reflection.newInstance(ExoticPotion.exoToReg.get(p.getClass()));
		} else {
			return Reflection.newInstance(ExoticPotion.regToExo.get(p.getClass()));
		}
	}
	
	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

	@Override
	public int energyVal() {
		return isKnown() ? 10 * quantity : super.energyVal();
	}
}
