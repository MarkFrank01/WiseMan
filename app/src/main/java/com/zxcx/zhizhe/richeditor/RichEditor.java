package com.zxcx.zhizhe.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxcx.zhizhe.BuildConfig;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (C) 2017 Wasabeef <p> Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at <p> http://www.apache.org/licenses/LICENSE-2.0 <p> Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */

public class RichEditor extends WebView {

	//    private static final String SETUP_HTML = "http://192.168.1.149:8043/view/zzeditor";
	private static final String CALLBACK_SCHEME = "re-callback://";
	private static final String STATE_SCHEME = "re-state://";
	//    private static final String SETUP_HTML = "file:///android_asset/editor.html";
	private static String SETUP_HTML;
	private boolean isReady = false;
	private String mContents;
	private OnTextChangeListener mTextChangeListener;
	private OnDecorationStateListener mDecorationStateListener;
	private AfterInitialLoadListener mLoadListener;

	public RichEditor(Context context) {
		this(context, null);
	}

	public RichEditor(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.webViewStyle);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		if (Constants.IS_NIGHT) {
			setBackgroundColor(ContextCompat.getColor(context, R.color.background));
		}

		setVerticalScrollBarEnabled(false);
		setHorizontalScrollBarEnabled(false);
		getSettings().setJavaScriptEnabled(true);
		setWebChromeClient(new WebChromeClient());
		setWebViewClient(createWebviewClient());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getSettings().setMixedContentMode(
				WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);//内容加载混合模式，适用于https和http混合时使用
		}

		if (BuildConfig.DEBUG) {
			WebView.setWebContentsDebuggingEnabled(true);//开启WebView内容调试
		}

		this.getSettings().setLoadWithOverviewMode(true);
		this.getSettings().setUseWideViewPort(true);
		this.getSettings().setBuiltInZoomControls(true);

		applyAttributes(context, attrs);
	}

	protected EditorWebViewClient createWebviewClient() {
		return new EditorWebViewClient();
	}

	public void setUrl(String url) {
		SETUP_HTML = url;
		loadUrl(SETUP_HTML);
	}

	public void setOnTextChangeListener(OnTextChangeListener listener) {
		mTextChangeListener = listener;
	}

	public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
		mDecorationStateListener = listener;
	}

	public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
		mLoadListener = listener;
	}

	private void callback(String text) {
		mContents = text.replaceFirst(CALLBACK_SCHEME, "");
		if (mTextChangeListener != null) {
			mTextChangeListener.onTextChange(mContents);
		}
	}

	private void stateCheck(String text) {
		String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
		List<Type> types = new ArrayList<>();
		for (Type type : Type.values()) {
			if (TextUtils.indexOf(state, type.name()) != -1) {
				types.add(type);
			}
		}

		if (mDecorationStateListener != null) {
			mDecorationStateListener.onStateChangeListener(state, types);
		}
	}

	private void applyAttributes(Context context, AttributeSet attrs) {
		final int[] attrsArray = new int[]{
			android.R.attr.gravity
		};
		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

		int gravity = ta.getInt(0, NO_ID);
		switch (gravity) {
			case Gravity.LEFT:
				exec("setTextAlign(\"left\")");
				break;
			case Gravity.RIGHT:
				exec("setTextAlign(\"right\")");
				break;
			case Gravity.TOP:
				exec("setVerticalAlign(\"top\")");
				break;
			case Gravity.BOTTOM:
				exec("setVerticalAlign(\"bottom\")");
				break;
			case Gravity.CENTER_VERTICAL:
				exec("setVerticalAlign(\"middle\")");
				break;
			case Gravity.CENTER_HORIZONTAL:
				exec("setTextAlign(\"center\")");
				break;
			case Gravity.CENTER:
				exec("setVerticalAlign(\"middle\")");
				exec("setTextAlign(\"center\")");
				break;
		}

		ta.recycle();
	}

	public String getHtml() {
		return mContents;
	}

	public void setHtml(String contents) {
		if (contents == null) {
			contents = "";
		}
		try {
			exec("setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
		} catch (UnsupportedEncodingException e) {
			// No handling
		}
		mContents = contents;
	}

	public void setEditorFontColor(int color) {
		String hex = convertHexColorString(color);
		exec("setBaseTextColor('" + hex + "');");
	}

	public void setEditorFontSize(int px) {
		exec("setBaseFontSize('" + px + "px');");
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		exec("setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
			+ "px');");
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		// still not support RTL.
		setPadding(start, top, end, bottom);
	}

	public void setEditorBackgroundColor(int color) {
		setBackgroundColor(color);
	}

	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
	}

	@Override
	public void setBackgroundResource(int resid) {
		Bitmap bitmap = Utils.decodeResource(getContext(), resid);
		String base64 = Utils.toBase64(bitmap);
		bitmap.recycle();

		exec("setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
	}

	@Override
	public void setBackground(Drawable background) {
		Bitmap bitmap = Utils.toBitmap(background);
		String base64 = Utils.toBase64(bitmap);
		bitmap.recycle();

		exec("setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
	}

	public void setBackground(String url) {
		exec("setBackgroundImage('url(" + url + ")');");
	}

	public void setEditorWidth(int px) {
		exec("setWidth('" + px + "px');");
	}

	public void setEditorHeight(int px) {
		exec("setHeight('" + px + "px');");
	}

	public void setPlaceholder(String placeholder) {
		exec("setPlaceholder('" + placeholder + "');");
	}

	public void setInputEnabled(Boolean inputEnabled) {
		exec("setInputEnabled(" + inputEnabled + ")");
	}

	public void loadCSS(String cssFile) {
		String jsCSSImport = "(function() {" +
			"    var head  = document.getElementsByTagName(\"head\")[0];" +
			"    var link  = document.createElement(\"link\");" +
			"    link.rel  = \"stylesheet\";" +
			"    link.type = \"text/css\";" +
			"    link.href = \"" + cssFile + "\";" +
			"    link.media = \"all\";" +
			"    head.appendChild(link);" +
			"}) ();";
		exec("" + jsCSSImport + "");
	}

	public void undo() {
		exec("undo();");
	}

	public void redo() {
		exec("redo();");
	}

	public void setBold() {
		exec("setBold();");
	}

	public void setBold(boolean isBold) {
		exec("setBold(" + isBold + ");");
	}

	public void setItalic() {
		exec("setItalic();");
	}

	public void setSubscript() {
		exec("setSubscript();");
	}

	public void setSuperscript() {
		exec("setSuperscript();");
	}

	public void setStrikeThrough() {
		exec("setStrikeThrough();");
	}

	public void setUnderline() {
		exec("setUnderline();");
	}

	public void setIsEyeshield(boolean isEyeshield) {
		exec("setIsEyeshield(" + isEyeshield + ");");
	}

	/**
	 * 文章再编辑
	 *
	 * @param viewType 审核未通过2，草稿箱3
	 */
	public void articleReedit(Integer cardId, String token, Integer viewType) {
		exec("articleReedit(" + cardId + ",'" + token + "'," + viewType + ");");
	}

	public void noteReedit(Integer cardId, String token) {
		exec("noteReedit(" + cardId + ",'" + token + "');");
	}

	public void setTimeStampAndToken(String token) {
		exec("setTimeStampAndToken('" + token + "');");
	}

	public void setTextColor(int color) {
		exec("prepareInsert();");

		String hex = convertHexColorString(color);
		exec("setTextColor('" + hex + "');");
	}

	public void setTextBackgroundColor(int color) {
		exec("prepareInsert();");

		String hex = convertHexColorString(color);
		exec("setTextBackgroundColor('" + hex + "');");
	}

	public void setFontSize(int fontSize) {

		exec("setFontSize('" + fontSize + "');");
	}

	public void removeFormat() {
		exec("removeFormat();");
	}

	public void setHeading(int heading) {
		exec("setHeading('" + heading + "');");
	}

	public void setIndent() {
		exec("setIndent();");
	}

	public void setOutdent() {
		exec("setOutdent();");
	}

	public void setAlignLeft() {
		exec("setJustifyLeft();");
	}

	public void setAlignCenter() {
		exec("setJustifyCenter();");
	}

	public void setAlignRight() {
		exec("setJustifyRight();");
	}

	public void setBlockquote() {
		exec("setBlockquote();");
	}

	public void setBullets() {
		exec("setBullets();");
	}

	public void setNumbers() {
		exec("setNumbers();");
	}

	public void insertImage(String url) {
		exec("prepareInsert();");
		exec("insertImage('" + url + "');");
	}

	public void setTitleImage(String url) {
		exec("setTitleImage('" + url + "');");
	}

	public void setContentImage(String url) {
		exec("setContentImage('" + url + "');");
	}

	public void insertLink(String href, String title) {
		exec("prepareInsert();");
		exec("insertLink('" + href + "', '" + title + "');");
	}

	public void insertTodo() {
		exec("prepareInsert();");
		exec("setTodo('" + Utils.getCurrentTime() + "');");
	}

	public void focusEditor() {
		requestFocus();
		exec("focus();");
	}

	public void clearFocusEditor() {
		exec("blurFocus();");
	}

	private String convertHexColorString(int color) {
		return String.format("#%06X", (0xFFFFFF & color));
	}

	protected void exec(final String trigger) {
		if (isReady) {
			load(trigger);
		} else {
			postDelayed(new Runnable() {
				@Override
				public void run() {
					exec(trigger);
				}
			}, 100);
		}
	}

	private void load(String trigger) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			evaluateJavascript(trigger, null);
		} else {
			loadUrl(trigger);
		}
	}

	public enum Type {
		BOLD,
		ITALIC,
		SUBSCRIPT,
		SUPERSCRIPT,
		STRIKETHROUGH,
		UNDERLINE,
		H1,
		H2,
		H3,
		H4,
		H5,
		H6,
		ORDEREDLIST,
		UNORDEREDLIST,
		JUSTIFYCENTER,
		JUSTIFYFULL,
		JUSTUFYLEFT,
		JUSTIFYRIGHT
	}

	public interface OnTextChangeListener {

		void onTextChange(String text);
	}

	public interface OnDecorationStateListener {

		void onStateChangeListener(String text, List<Type> types);
	}

	public interface AfterInitialLoadListener {

		void onAfterInitialLoad(boolean isReady);
	}

	protected class EditorWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			isReady = url.equalsIgnoreCase(SETUP_HTML);
			if (mLoadListener != null) {
				mLoadListener.onAfterInitialLoad(isReady);
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			String decode;
			try {
				decode = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// No handling
				return false;
			}

			if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
				callback(decode);
				return true;
			} else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
				stateCheck(decode);
				return true;
			}

			return super.shouldOverrideUrlLoading(view, url);
		}
	}
}
