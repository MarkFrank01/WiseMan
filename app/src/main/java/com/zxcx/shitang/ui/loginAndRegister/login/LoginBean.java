package com.zxcx.shitang.ui.loginAndRegister.login;

public class LoginBean {

    /**
     * serviceStartTime : 0
     * token : string
     * user : {"avatar":"string","birth":"string","createTime":"2017-07-24T06:46:20.757Z","gender":0,"id":0,"name":"string"}
     */

    private int serviceStartTime;
    private String token;
    private UserBean user;

    public int getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(int serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * avatar : string
         * birth : string
         * createTime : 2017-07-24T06:46:20.757Z
         * gender : 0
         * id : 0
         * name : string
         */

        private String avatar;
        private String birth;
        private String createTime;
        private int gender;
        private int id;
        private String name;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

