package com.zxcx.zhizhe.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anm on 2018/3/13.
 * 验证码输入框
 */

public class VerificationCodeInput2 extends LinearLayout implements TextWatcher, View.OnKeyListener,
	View.OnFocusChangeListener {

	private final static String TYPE_NUMBER = "number";
	private final static String TYPE_TEXT = "text";
	private final static String TYPE_PASSWORD = "password";
	private final static String TYPE_PHONE = "phone";

	private static final String TAG = "VerificationCodeInput";
	private int box = 4;
	private int boxWidth = ScreenUtils.dip2px(32);
	private int boxHeight = ScreenUtils.dip2px(44);
	private int childHPadding = 14;
	private int childVPadding = 14;
	private String inputType = TYPE_NUMBER;
	private Drawable boxBgFocus = null;
	private Drawable boxBgNormal = null;
	private Listener listener;
	private boolean focus = false;
	private List<EditText> mEditTextList = new ArrayList<>();
	private int currentPosition = 0;

	public VerificationCodeInput2(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput);
		box = a.getInt(R.styleable.VerificationCodeInput_box, 4);

		childHPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_h_padding, 0);
		childVPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_v_padding, 0);
		boxBgFocus = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_focus);
		boxBgNormal = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_normal);
		inputType = a.getString(R.styleable.VerificationCodeInput_inputType);
		boxWidth = (int) a.getDimension(R.styleable.VerificationCodeInput_child_width, boxWidth);
		boxHeight = (int) a.getDimension(R.styleable.VerificationCodeInput_child_height, boxHeight);
		a.recycle();
		initViews();
	}


	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();


	}

	private void initViews() {
		for (int i = 0; i < box; i++) {
			EditText editText = new EditText(getContext());
			LayoutParams layoutParams = new LayoutParams(boxWidth,
				boxHeight);
			layoutParams.bottomMargin = childVPadding;
			layoutParams.topMargin = childVPadding;
			layoutParams.leftMargin = childHPadding;
			layoutParams.rightMargin = childHPadding;
			layoutParams.gravity = Gravity.CENTER;

			editText.setOnKeyListener(this);
			editText.setOnFocusChangeListener(this);
			if (i == 0) {
				setBg(editText, true);
			} else {
				setBg(editText, false);
			}
			editText.setTextColor(ContextCompat.getColor(getContext(), R.color.button_blue));
			editText.setLayoutParams(layoutParams);
			editText.setGravity(Gravity.CENTER);
			editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

			if (TYPE_NUMBER.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			} else if (TYPE_PASSWORD.equals(inputType)) {
				editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			} else if (TYPE_TEXT.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
			} else if (TYPE_PHONE.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_PHONE);

			}
			editText.setId(i);
			editText.setEms(1);
			editText.addTextChangedListener(this);
			addView(editText, i);
			mEditTextList.add(editText);

		}

	}

	private void backFocus() {
		int count = getChildCount();
		EditText editText;
		for (int i = count - 1; i >= 0; i--) {
			editText = (EditText) getChildAt(i);
			if (editText.getText().length() == 1) {
				editText.requestFocus();
				setBg(mEditTextList.get(i), true);
				//setBg(mEditTextList.get(i-1),true);
				editText.setSelection(1);
				return;
			}
		}
	}

	public void focus() {
		int count = getChildCount();
		EditText editText;
		for (int i = 0; i < count; i++) {
			editText = (EditText) getChildAt(i);
			if (editText.getText().length() < 1) {
				editText.requestFocus();
				InputMethodManager imm = (InputMethodManager) editText.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(editText, 0);
				return;
			}
		}
	}

	private void setBg(EditText editText, boolean focus) {
		if (boxBgNormal != null && !focus) {
			editText.setBackground(boxBgNormal);
		} else if (boxBgFocus != null && focus) {
			editText.setBackground(boxBgFocus);
		}
	}

	private void setBg() {
		int count = getChildCount();
		EditText editText;
		for (int i = 0; i < count; i++) {
			editText = (EditText) getChildAt(i);
			if (boxBgNormal != null && !focus) {
				editText.setBackground(boxBgNormal);
			} else if (boxBgFocus != null && focus) {
				editText.setBackground(boxBgFocus);
			}
		}

	}

	private void checkAndCommit() {
		StringBuilder stringBuilder = new StringBuilder();
		boolean full = true;
		for (int i = 0; i < box; i++) {
			EditText editText = (EditText) getChildAt(i);
			String content = editText.getText().toString();
			if (content.length() == 0) {
				full = false;
				break;
			} else {
				stringBuilder.append(content);
			}

			Spannable spn = editText.getText();
			int typeface = Typeface.BOLD;
			spn.setSpan(new StyleSpan(typeface),0,1,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}
		if (full) {
			if (listener != null) {
				//关闭输入法
				Context context = getContext();
				if (context instanceof Activity) {
					Activity activity = (Activity) context;
					InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
					}
				}
				listener.onComplete(stringBuilder.toString());
			}

		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			child.setEnabled(enabled);
		}
	}

	public void setOnCompleteListener(Listener listener) {
		this.listener = listener;
	}

	@Override

	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
		if (count > 0) {
			View child = getChildAt(0);
			int cHeight = child.getMeasuredHeight();
			int cWidth = child.getMeasuredWidth();
			int maxH = cHeight + 2 * childVPadding;
			int maxW = (cWidth + 2 * childHPadding) * box;
			setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
				resolveSize(maxH, heightMeasureSpec));
		}
		
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			
			child.setVisibility(View.VISIBLE);
			int cWidth = child.getMeasuredWidth();
			int cHeight = child.getMeasuredHeight();
			int cl = (i) * (cWidth + 2 * childHPadding) + childHPadding;
			int cr = cl + cWidth;
			int ct = childVPadding;
			int cb = ct + cHeight;
			child.layout(cl, ct, cr, cb);
		}
		
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 0) {
		} else {
			focus();
			checkAndCommit();
		}
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		EditText editText = (EditText) view;
		if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
			int action = event.getAction();
			if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
				currentPosition--;
				mEditTextList.get(currentPosition).requestFocus();
				setBg(mEditTextList.get(currentPosition), true);
				setBg(mEditTextList.get(currentPosition + 1), false);
				mEditTextList.get(currentPosition).setText("");
			}
		}
		return false;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			((EditText) v).setText("");
			currentPosition = mEditTextList.indexOf(((EditText) v));
		}
		setBg((EditText) v, hasFocus);
	}
	
	public interface Listener {
		
		void onComplete(String content);
	}
}