package com.zxcx.zhizhe.mvpBase;

/**
 * Created by chenf on 2016/9/1.
 * 同时具有Get和Post功能的View接口
 */
public interface GetPostView<T, P> extends GetView<T>, PostView<P> {


}
