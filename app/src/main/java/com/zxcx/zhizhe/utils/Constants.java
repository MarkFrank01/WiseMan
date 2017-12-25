package com.zxcx.zhizhe.utils;

/**
 * Created by anm on 2017/6/2.
 */

public class Constants {
    public static final int RESULT_OK = 600;
    public static final int RESULT_FAIL = 0;
    public static final int TOKEN_OUTTIME = 800;
    public static final int NEED_LOGIN = 700;
    public static final int APP_TYPE = 0;

    public static final String PUSH_TYPE_CARD = "card";
    public static final String PUSH_TYPE_CARD_BAG = "cardBag";
    public static final String PUSH_TYPE_AD = "ad";

    public static final int PAGE_SIZE = 16;
    public static boolean IS_NIGHT = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight,false);

    public static final int NOTE_TYPE_CARD = 4;
    public static final int NOTE_TYPE_FREEDOM = 3;

    public static final int CLIP_IMAGE = 100;
}
