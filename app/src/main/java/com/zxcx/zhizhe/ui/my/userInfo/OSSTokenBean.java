package com.zxcx.zhizhe.ui.my.userInfo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/9/18.
 */

public class OSSTokenBean extends RetrofitBaen{

    /**
     * accessKeyId : string
     * accessKeySecret : string
     * expiration : string
     * securityToken : string
     */

    @JSONField(name = "accessKeyId")
    private String accessKeyId;
    @JSONField(name = "accessKeySecret")
    private String accessKeySecret;
    @JSONField(name = "expiration")
    private String expiration;
    @JSONField(name = "securityToken")
    private String securityToken;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
