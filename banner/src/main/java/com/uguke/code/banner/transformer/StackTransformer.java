package com.uguke.code.banner.transformer;

import android.view.View;

public class StackTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {

		if (position <= 0f) {
			view.setTranslationX(0f);
		} else if (position <= 1f) {
			view.setTranslationX(view.getWidth() * -position);
		}
	}

	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
