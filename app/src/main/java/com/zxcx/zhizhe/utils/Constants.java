package com.zxcx.zhizhe.utils;

/**
 * Created by anm on 2017/6/2.
 */

public class Constants {
    public static final int RESULT_OK = 600;
    public static final int RESULT_FAIL = 0;
    public static final int TOKEN_OUTTIME = 800;
    public static final int NEED_LOGIN = 700;
    public static final int NEED_REGISTER = 710;
    public static final int APP_TYPE = 0;

    public static final String PUSH_TYPE_CARD = "card";
    public static final String PUSH_TYPE_CARD_BAG = "cardBag";
    public static final String PUSH_TYPE_AD = "ad";

    public static final int PAGE_SIZE = 10;
    public static boolean IS_NIGHT = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight,false);

    public static final int NOTE_TYPE_CARD = 4;
    public static final int NOTE_TYPE_FREEDOM = 3;

    public static final int CLIP_IMAGE = 100;

    /*任务类型*/
    //注册智者
    public static final int MISSION_REGISTER = 1;
    //卡片阅读
    public static final int MISSION_READ = 2;
    //卡片创作
    public static final int MISSION_CREATION = 3;
    //卡片被点赞
    public static final int MISSION_HAS_LIKE = 4;
    //卡片被收藏
    public static final int MISSION_HAS_COLLECT = 5;
    //新增关注
    public static final int MISSION_HAS_FOLLOW = 6;
    //首页推荐
    public static final int MISSION_HAS_HOT = 7;
    //上榜排名
    public static final int MISSION_HAS_RANK = 8;

}
