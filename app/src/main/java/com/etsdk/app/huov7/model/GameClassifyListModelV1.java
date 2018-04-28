package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/14.
 *
 * 游戏分类实体类
 */

public class GameClassifyListModelV1 extends BaseModel {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int count;
        private List<GameClassify> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GameClassify> getList() {
            return list;
        }

        public void setList(List<GameClassify> list) {
            this.list = list;
        }
    }
    public static class GameClassify {
        private String  typeid;//	INT	类型ID
        private String  typename;//	STRING	类型名称	类型名称
        private String  icon;//	STRING(URL)	类型名称	类型图标
        private String  subcount;//	INT	子类型数量	表示子类型sublist数量
        private List<GameClassify> sublist;//二级列表

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSubcount() {
            return subcount;
        }

        public void setSubcount(String subcount) {
            this.subcount = subcount;
        }

        public List<GameClassify> getSublist() {
            return sublist;
        }

        public void setSublist(List<GameClassify> sublist) {
            this.sublist = sublist;
        }
    }
}
