/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masotros.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.masotros.floatingmenu.RayMenu;

public class MainActivity extends Activity {
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
			R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };
	private static final int[] ITEM_DRAWABLES_MOD = { R.drawable.ponele, R.drawable.rock,
		R.drawable.atuvida, R.drawable.guuachin, R.drawable.locura, R.drawable.maquinaa };
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final int itemCount = ITEM_DRAWABLES.length;
		

		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = true;
		Bitmap gradient = BitmapFactory.decodeResource(getResources(), R.drawable.top_fade, options);
		rayMenu.setBackground(gradient);
		
		for (int i = 0; i < itemCount; i++) {
			ImageView itemIcon = new ImageView(this);
			itemIcon.setImageResource(ITEM_DRAWABLES[i]);
			ImageView itemTitle = new ImageView(this);
			itemTitle.setImageResource(ITEM_DRAWABLES_MOD[i]);
			//final int position = i;
			rayMenu.addItem(itemIcon, itemTitle, new OnClickListener() {

				@Override
				public void onClick(View v) {
					//Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
				}
			});// Add a menu item
			
		}
	}
}
