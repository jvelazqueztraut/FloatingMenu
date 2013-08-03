package com.masotros.floatingmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RayMenu extends RelativeLayout {
	private RayLayout mRayLayout;
	private LinearLayout mRayLayoutTitles;
	private ImageView mRayBackground;
	private ImageView mHintView;

	public RayMenu(Context context) {
		super(context);
		init(context);
	}

	public RayMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		setClipChildren(false);

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.ray_menu, this);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.item_layout);		
		mRayLayout = (RayLayout) ll.findViewById(R.id.item_icon);
		mRayLayoutTitles = (LinearLayout) ll.findViewById(R.id.item_title);
		
		mRayBackground = (ImageView) findViewById(R.id.fade_background);
		
		mRayLayoutTitles.setVisibility(android.view.View.INVISIBLE);
		mRayBackground.setVisibility(android.view.View.INVISIBLE);

		mHintView = (ImageView) findViewById(R.id.control_hint);
		mHintView.setClickable(true);
		mHintView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mHintView.startAnimation(createHintSwitchAnimation(mRayLayout.isExpanded()));
					mRayLayoutTitles.startAnimation(createTitleSwitchAnimation(mRayLayout.isExpanded()));
					mRayBackground.startAnimation(createTitleSwitchAnimation(mRayLayout.isExpanded()));
					mRayLayout.switchState(true);
				}

				return false;
			}
		});
	}

	public void addItem(ImageView icon, ImageView title, OnClickListener listener) {
		icon.setOnClickListener(getItemClickListener(listener));
		mRayLayout.addView(icon);
		mHintView.setImageDrawable(icon.getDrawable());
		
		mRayLayoutTitles.addView(title);
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layoutParams.width=LayoutParams.WRAP_CONTENT;
		layoutParams.height=(mRayLayout.getGapSize()+mRayLayout.getChildSize())*mRayLayout.getChildCount();
		layoutParams.topMargin=mRayLayout.getHolderSize() + mRayLayout.getGapSize()/2;
		mRayLayoutTitles.setLayoutParams(layoutParams);
		
		LinearLayout.LayoutParams titleParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		titleParams.gravity= Gravity.CENTER | Gravity.RIGHT;
		titleParams.width=LayoutParams.WRAP_CONTENT;
		titleParams.height=0;
		titleParams.weight=1.0f;
		title.setLayoutParams(titleParams);
	}
	
	@Override
	public void setBackground(Drawable background){
		mRayBackground.setImageDrawable(background);
	}
	public void setBackground(Bitmap background){
		mRayBackground.setImageBitmap(background);
	}

	private OnClickListener getItemClickListener(final OnClickListener listener) {
		return new OnClickListener() {

			@Override
			public void onClick(final View viewClicked) {
				Animation animation = bindItemAnimation(viewClicked, true, 400);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						postDelayed(new Runnable() {

							@Override
							public void run() {
								itemDidDisappear();
							}
						}, 0);
					}
				});

				final int itemCount = mRayLayout.getChildCount();
				for (int i = 0; i < itemCount; i++) {
					View item = mRayLayout.getChildAt(i);
					if (viewClicked != item) {
						bindItemAnimation(item, false, 300);
					}
				}
				
				if (listener != null) {
					listener.onClick(viewClicked);
				}
				
				ImageView icon = (ImageView) viewClicked;
				mHintView.setImageDrawable(icon.getDrawable());

				mRayLayout.invalidate();
				mHintView.startAnimation(createHintSwitchAnimation(true));
				mRayLayoutTitles.startAnimation(createTitleSwitchAnimation(true));
				mRayBackground.startAnimation(createTitleSwitchAnimation(true));
			}
		};
	}

	private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
		Animation animation = createItemDisapperAnimation(duration, isClicked);
		child.setAnimation(animation);

		return animation;
	}

	private void itemDidDisappear() {
		final int itemCount = mRayLayout.getChildCount();
		for (int i = 0; i < itemCount; i++) {
			View item = mRayLayout.getChildAt(i);
			item.clearAnimation();
		}

		mRayLayout.switchState(false);
	}

	private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 1.25f : 0.0f, 1.0f, isClicked ? 1.25f : 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

		animationSet.setDuration(duration);
		animationSet.setInterpolator(new DecelerateInterpolator());
		animationSet.setFillAfter(true);

		return animationSet;
	}

	private static Animation createHintSwitchAnimation(final boolean expanded) {
		Animation animation = new RotateAnimation(expanded ? 360 : 0, expanded ? 0 : 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setStartOffset(0);
		animation.setDuration(250);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setFillAfter(true);

		return animation;
	}
	
	private static Animation createTitleSwitchAnimation(final boolean expanded) {
		Animation animation = new AlphaAnimation(expanded ? 1.0f : 0.0f, expanded ? 0.0f : 1.0f);
		animation.setStartOffset(100);
		animation.setDuration(200);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setFillAfter(true);

		return animation;
	}

}
