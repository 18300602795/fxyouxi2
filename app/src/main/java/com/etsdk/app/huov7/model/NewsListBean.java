package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class NewsListBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":1,"list":[{"id":4,"title":"测试","gameid":101,"img":"http://statics.niudaosy.com/upload/20170412/58edd9d952fe2.jpg","pudate":"2017-04-12","author":1,"commentcnt":0,"likecnt":0,"type":1}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 1
         * list : [{"id":4,"title":"测试","gameid":101,"img":"http://statics.niudaosy.com/upload/20170412/58edd9d952fe2.jpg","pudate":"2017-04-12","author":1,"commentcnt":0,"likecnt":0,"type":1}]
         */

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 4
             * title : 测试
             * gameid : 101
             * img : http://statics.niudaosy.com/upload/20170412/58edd9d952fe2.jpg
             * pudate : 2017-04-12
             * author : 1
             * commentcnt : 0
             * likecnt : 0
             * type : 1
             */

            private int id;
            private String title;
            private int gameid;
            private String img;
            private String pudate;
            private int author;
            private int commentcnt;
            private int likecnt;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getGameid() {
                return gameid;
            }

            public void setGameid(int gameid) {
                this.gameid = gameid;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPudate() {
                return pudate;
            }

            public void setPudate(String pudate) {
                this.pudate = pudate;
            }

            public int getAuthor() {
                return author;
            }

            public void setAuthor(int author) {
                this.author = author;
            }

            public int getCommentcnt() {
                return commentcnt;
            }

            public void setCommentcnt(int commentcnt) {
                this.commentcnt = commentcnt;
            }

            public int getLikecnt() {
                return likecnt;
            }

            public void setLikecnt(int likecnt) {
                this.likecnt = likecnt;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
