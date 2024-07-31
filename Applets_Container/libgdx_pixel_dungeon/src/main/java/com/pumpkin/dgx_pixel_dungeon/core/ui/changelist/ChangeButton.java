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

package com.pumpkin.dgx_pixel_dungeon.core.ui.changelist;

import com.pumpkin.dgx_pixel_dungeon.core.items.Item;
import com.pumpkin.dgx_pixel_dungeon.core.messages.Messages;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.ChangesScene;
import com.pumpkin.dgx_pixel_dungeon.core.scenes.PixelScene;
import com.pumpkin.dgx_pixel_dungeon.core.sprites.ItemSprite;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.Image;
import com.pumpkin.dgx_pixel_dungeon.spd.noosa.ui.Component;

//not actually a button, but functions as one.
public class ChangeButton extends Component {
	
	protected Image icon;
	protected String title;
	protected String[] messages;
	
	public ChangeButton( Image icon, String title, String... messages){
		super();
		
		this.icon = icon;
		add(this.icon);
		
		this.title = Messages.titleCase(title);
		this.messages = messages;
		
		layout();
	}
	
	public ChangeButton(Item item, String message ){
		this( new ItemSprite(item), item.name(), message);
	}
	
	protected void onClick() {
		ChangesScene.showChangeInfo(new Image(icon), title, messages);
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width()) / 2f;
		icon.y = y + (height - icon.height()) / 2f;
		PixelScene.align(icon);
	}
}
