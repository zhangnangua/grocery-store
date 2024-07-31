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

package com.pumpkin.dgx_pixel_dungeon.core.items.wands;

import com.pumpkin.dgx_pixel_dungeon.core.Assets;
import com.pumpkin.dgx_pixel_dungeon.core.Badges;
import com.pumpkin.dgx_pixel_dungeon.core.Dungeon;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Actor;
import com.pumpkin.dgx_pixel_dungeon.core.actors.Char;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Barrier;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Buff;
import com.pumpkin.dgx_pixel_dungeon.core.actors.buffs.Charm;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mimic;
import com.pumpkin.dgx_pixel_dungeon.core.actors.mobs.Mob;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Beam;
import com.pumpkin.dgx_pixel_dungeon.core.effects.CellEmitter;
import com.pumpkin.dgx_pixel_dungeon.core.effects.FloatingText;
import com.pumpkin.dgx_pixel_dungeon.core.effects.Speck;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.BloodParticle;
import com.pumpkin.dgx_pixel_dungeon.core.effects.particles.ShadowParticle;
import com.pumpkin.dgx_pixel_dungeon.core.items.weapon.melee.MagesStaff;
import com.pumpkin.dgx_pixel_dungeon.core.mechanics.Ballistica;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.CharSprite;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSpriteSheet;
import com.pumpkin.dgx_pixel_dungeon.core.tiles.DungeonTilemap;
import com.pumpkin.dgx_pixel_dungeon.core.utils.GLog;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.audio.Sample;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Bundle;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Callback;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.PointF;
import com.pumpkin.dgx_pixel_dungeon.spd.utils.Random;

public class WandOfTransfusion extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_TRANSFUSION;

		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	public int min(int level) {
		return 3 + level;
	}

	@Override
	public int max(int level) {
		return 6 + 2*level;
	}

	private boolean freeCharge = false;

	@Override
	public void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);

		if (ch instanceof Mob){
			
			wandProc(ch, chargesPerCast());
			
			//this wand does different things depending on the target.
			
			//heals/shields an ally or a charmed enemy while damaging self
			if (ch.alignment == Char.Alignment.ALLY || ch.buff(Charm.class) != null){
				
				// 5% of max hp
				int selfDmg = Math.round(curUser.HT*0.05f);
				
				int healing = selfDmg + 3*buffedLvl();
				int shielding = (ch.HP + healing) - ch.HT;
				if (shielding > 0){
					healing -= shielding;
					Buff.affect(ch, Barrier.class).setShield(shielding);
				} else {
					shielding = 0;
				}
				
				ch.HP += healing;
				
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2 + buffedLvl() / 2);
				if (healing > 0) {
					ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(healing), FloatingText.HEALING);
				}
				if (shielding > 0){
					ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shielding), FloatingText.SHIELDING);
				}
				
				if (!freeCharge) {
					damageHero(selfDmg);
				} else {
					freeCharge = false;
				}

			//for enemies...
			//(or for mimics which are hiding, special case)
			} else if (ch.alignment == Char.Alignment.ENEMY || ch instanceof Mimic) {

				//grant a self-shield, and...
				Buff.affect(curUser, Barrier.class).setShield((5 + buffedLvl()));
				curUser.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(5+buffedLvl()), FloatingText.SHIELDING);
				
				//charms living enemies
				if (!ch.properties().contains(Char.Property.UNDEAD)) {
					Charm charm = Buff.affect(ch, Charm.class, Charm.DURATION/2f);
					charm.object = curUser.id();
					charm.ignoreHeroAllies = true;
					ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 3 );
				
				//harms the undead
				} else {
					ch.damage(damageRoll(), this);
					ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + buffedLvl());
					Sample.INSTANCE.play(Assets.Sounds.BURNING);
				}

			}
			
		}
		
	}

	//this wand costs health too
	private void damageHero(int damage){
		
		curUser.damage(damage, this);

		if (!curUser.isAlive()){
			Badges.validateDeathFromFriendlyMagic();
			Dungeon.fail( this );
			GLog.n( Messages.get(this, "ondeath") );
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		if (defender.buff(Charm.class) != null && defender.buff(Charm.class).object == attacker.id()){
			//grants a free use of the staff and shields self
			freeCharge = true;
			int shieldToGive = Math.round((2*(5 + buffedLvl()))*procChanceMultiplier(attacker));
			Buff.affect(attacker, Barrier.class).setShield(shieldToGive);
			attacker.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shieldToGive), FloatingText.SHIELDING);
			GLog.p( Messages.get(this, "charged") );
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}
	}

	@Override
	public void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xCC0000 );
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f);
		particle.radiateXY(0.5f);
	}

	@Override
	public String statsDesc() {
		int selfDMG = Math.round(Dungeon.hero.HT*0.05f);
		if (levelKnown)
			return Messages.get(this, "stats_desc", selfDMG, selfDMG + 3*buffedLvl(), 5+buffedLvl(), min(), max());
		else
			return Messages.get(this, "stats_desc", selfDMG, selfDMG, 5, min(0), max(0));
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

}
