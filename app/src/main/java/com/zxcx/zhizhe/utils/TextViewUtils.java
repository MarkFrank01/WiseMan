package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.TextView;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;

import static com.zxcx.zhizhe.utils.StringUtils.isEmpty;

/**
 * Created by chenf on 2016/9/1.
 */
public class TextViewUtils {

	//清除
	public static void clearTextRightDrawable(Context context,
		TextView tvName) {
		Drawable[] drawables = tvName.getCompoundDrawables();
		drawables[2] = null;
		tvName.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
	}

	//资源ID
	public static void setTextRightDrawable(Context context, int drawableRes,
		TextView tvName) {
		Drawable drawableRight = ContextCompat.getDrawable(context, drawableRes);
		Drawable[] drawables = tvName.getCompoundDrawables();
		drawables[2] = drawableRight;
		// 必须设置图片大小，否则不显示
		drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
			drawableRight.getMinimumHeight());
		tvName.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
	}

	//bitmap,
	public static void setTextRightDrawable(Context context, Bitmap bitmap,
		TextView tvName) {
		BitmapDrawable drawableRight = new BitmapDrawable(Resources.getSystem(), bitmap);
		Drawable[] drawables = tvName.getCompoundDrawables();
		drawables[2] = drawableRight;
		// 必须设置图片大小，否则不显示
		drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
			drawableRight.getMinimumHeight());
		tvName.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
	}

	//资源ID
	public static void setTextLeftDrawable(Context context, int drawableRes,
		TextView tvName) {
		Drawable drawableLeft = ContextCompat.getDrawable(context, drawableRes);
		Drawable[] drawables = tvName.getCompoundDrawables();
		drawables[0] = drawableLeft;
		// 必须设置图片大小，否则不显示
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
			drawableLeft.getMinimumHeight());
		tvName.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
	}

	//bitmap,
	public static void setTextLeftDrawable(Context context, Bitmap bitmap,
		TextView tvName) {
		BitmapDrawable drawableLeft = new BitmapDrawable(Resources.getSystem(), bitmap);
		Drawable[] drawables = tvName.getCompoundDrawables();
		drawables[0] = drawableLeft;
		// 必须设置图片大小，否则不显示
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
			drawableLeft.getMinimumHeight());
		tvName.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
	}

	public static void adjustTvTextSize(TextView tv) {
		int avaiHeight = tv.getHeight() - tv.getPaddingTop() - tv.getPaddingBottom();

		if (avaiHeight <= 0) {
			return;
		}

		TextPaint textPaintClone = new TextPaint(tv.getPaint());
		// note that Paint text size works in px not sp
		float trySize = textPaintClone.getTextSize();

		Rect rect = new Rect();
		String s = tv.getText().toString();
		textPaintClone.getTextBounds(s, 0, s.length(), rect);

		if (tv.getMeasuredHeight() < avaiHeight) {
			while (rect.height() < avaiHeight) {
				trySize++;
				textPaintClone.setTextSize(trySize);
				textPaintClone.getTextBounds(s, 0, s.length(), rect);
			}
			trySize--;
		} else {
			while (rect.height() > avaiHeight) {
				trySize--;
				textPaintClone.setTextSize(trySize);
				textPaintClone.getTextBounds(s, 0, s.length(), rect);
			}
		}

		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
	}

	public static void adjustTvTextSize(TextView tv, int length) {
		final String finalText = tv.getText().toString();
		int avaiWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();

		if (avaiWidth <= 0) {
			return;
		}

		while (tv.getText().length() < length) {
			tv.setText(tv.getText().toString() + tv.getText().toString());
		}

		TextPaint textPaintClone = new TextPaint(tv.getPaint());
		// note that Paint text size works in px not sp
		float trySize = textPaintClone.getTextSize();

		Rect rect = new Rect();
		String s = tv.getText().toString();
		textPaintClone.getTextBounds(s, 0, length, rect);

		if (tv.getMeasuredWidth() < avaiWidth) {
			while (rect.width() < avaiWidth) {
				trySize++;
				textPaintClone.setTextSize(trySize);
				textPaintClone.getTextBounds(s, 0, length, rect);
			}
			trySize--;
		} else {
			while (rect.width() > avaiWidth) {
				trySize--;
				textPaintClone.setTextSize(trySize);
				textPaintClone.getTextBounds(s, 0, length, rect);
			}
		}
		tv.setText(finalText);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
	}

	public static void setTextViewColorBlue(TextView textView, String key, String value) {
		if (isEmpty(value)) {
			return;
		}
		if (!isEmpty(key)) {
			SpannableStringBuilder style = new SpannableStringBuilder(value);
			int index = value.indexOf(key);
			if (index >= 0) {
				while (index < value.length() && index >= 0) {
					style.setSpan(new ForegroundColorSpan(
							ContextCompat.getColor(App.getContext(), R.color.button_blue)), index,
						index + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textView.setText(style);
					index = value.indexOf(key, index + key.length());
				}
			} else {
				textView.setText(value);
			}

		} else {
			textView.setText(value);
		}
	}

	public static void setTextViewColorBlack(TextView textView, String key, String value) {
		if (isEmpty(value)) {
			return;
		}
		if (!isEmpty(key)) {
			SpannableStringBuilder style = new SpannableStringBuilder(value);
			int index = value.indexOf(key);
			if (index >= 0) {
				while (index < value.length() && index >= 0) {
					style.setSpan(new ForegroundColorSpan(
							ContextCompat.getColor(App.getContext(), R.color.text_color_1)), index,
						index + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textView.setText(style);
					index = value.indexOf(key, index + key.length());
				}
			} else {
				textView.setText(value);
			}

		} else {
			textView.setText(value);
		}
	}
}
