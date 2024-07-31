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

package com.pumpkin.dgx_pixel_dungeon.core.ui;

import com.pumpkin.dgx_pixel_dungeon.core.ShatteredPixelDungeon;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.TitleScene;
import com.pumpkin.dgx_pixel_dungeon.core.windows.WndKeyBindings;
import com.pumpkin.dgx_pixel_dungeon.spd.input.GameAction;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Game;

public class ExitButton extends IconButton {

	public ExitButton() {
		super(Icons.EXIT.get());

		width = 20;
		height = 20;
	}

	@Override
	protected void onClick() {
		if (Game.scene() instanceof TitleScene) {
			Game.instance.finish();
		} else {
			ShatteredPixelDungeon.switchNoFade( TitleScene.class );
		}
	}

	@Override
	public GameAction keyAction() {
		return GameAction.BACK;
	}

	@Override
	protected String hoverText() {
		return Messages.titleCase(Messages.get(WndKeyBindings.class, "back"));
	}
}
