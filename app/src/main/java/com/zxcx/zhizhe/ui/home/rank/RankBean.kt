package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.retrofit.RetrofitBaen

data class RankBean(
        var myRank : UserRankBean,
        var userRankList : List<UserRankBean>
): RetrofitBaen()

