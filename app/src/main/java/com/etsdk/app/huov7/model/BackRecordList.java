package com.etsdk.app.huov7.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class BackRecordList implements Serializable{
    private List<BackRecord> list;

    public List<BackRecord> getList() {
        return list;
    }

    public void setList(List<BackRecord> list) {
        this.list = list;
    }

    public class BackRecord implements Serializable{
        String id;
        String time;
        String money;
        String game_name;
        String user_id;
        String game_id;
        String role_name;
        String area_clothing;
        String status;
        String rebate_time;
        String mem_id;
        String mem_name;
        String obligate;
        String game_icon;
        String req;

        public String getReq() {
            return req;
        }

        public void setReq(String req) {
            this.req = req;
        }

        public String getGame_icon() {
            return game_icon;
        }

        public void setGame_icon(String game_icon) {
            this.game_icon = game_icon;
        }

        public String getObligate() {
            return obligate;
        }

        public void setObligate(String obligate) {
            this.obligate = obligate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getRole_name() {
            return role_name;
        }

        public void setRole_name(String role_name) {
            this.role_name = role_name;
        }

        public String getArea_clothing() {
            return area_clothing;
        }

        public void setArea_clothing(String area_clothing) {
            this.area_clothing = area_clothing;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRebate_time() {
            return rebate_time;
        }

        public void setRebate_time(String rebate_time) {
            this.rebate_time = rebate_time;
        }

        public String getMem_id() {
            return mem_id;
        }

        public void setMem_id(String mem_id) {
            this.mem_id = mem_id;
        }

        public String getMem_name() {
            return mem_name;
        }

        public void setMem_name(String mem_name) {
            this.mem_name = mem_name;
        }
    }
}
