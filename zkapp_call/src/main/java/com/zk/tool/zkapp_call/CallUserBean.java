package com.zk.tool.zkapp_call;

public class CallUserBean {
    private String nickname;
    private int uid;
    private String img;
    private String privateMapKey;

    private CallUserBean(CallBuilder callBuilder) {
        nickname = callBuilder.nickname;
        uid = callBuilder.uid;
        img = callBuilder.img;
        privateMapKey = callBuilder.privateMapKey;
    }

    public String getPrivateMapKey() {
        return privateMapKey;
    }

    public String getNickname() {
        return nickname;
    }

    public int getUid() {
        return uid;
    }

    public String getImg() {
        return img;
    }

    public static class CallBuilder{
        private String nickname;
        private int uid;
        private String img;
        private String privateMapKey;
        public CallBuilder() {}

        public CallBuilder setPrivateMapKey(String privateMapKey) {
            this.privateMapKey = privateMapKey;
            return this;
        }

        public CallBuilder setImg(String img) {
            this.img = img;
            return this;
        }

        public CallBuilder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public CallBuilder setUid(int uid) {
            this.uid = uid;
            return this;
        }

        public CallUserBean builder(){
            return new CallUserBean(this);
        };
    }
}
