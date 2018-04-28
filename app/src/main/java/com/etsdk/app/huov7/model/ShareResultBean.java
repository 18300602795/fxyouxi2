package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/24.
 */

public class ShareResultBean extends BaseModel{
    private DateBean data;

    public DateBean getData() {
        return data;
    }

    public void setData(DateBean data) {
        this.data = data;
    }

    public static class DateBean{
        private String shareid;//STRING	分享ID	分享的ID
        private String title;//	STRING	分享名称	例如好玩游戏
        private String url;//	STRING(URL)	分享链接	例如：https://www.huosdk.com/ios
        private String sharetext;//	STRING	分享内容	70字以内

        public String getShareid() {
            return shareid;
        }

        public void setShareid(String shareid) {
            this.shareid = shareid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSharetext() {
            return sharetext;
        }

        public void setSharetext(String sharetext) {
            this.sharetext = sharetext;
        }
    }

}
