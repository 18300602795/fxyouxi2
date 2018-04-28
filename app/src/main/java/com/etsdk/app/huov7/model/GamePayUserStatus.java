package com.etsdk.app.huov7.model;

import java.util.Map;

/**
 * Created by liu hong liang on 2017/2/24.
 * 支付页面用户操作状态数据
 */

public class GamePayUserStatus {
    private String gameId;//充值的游戏id
    private int money;//选择的金钱
    private int inputMoney;//用户输入的金钱
    private Map<String,Integer> selectCouponCountMap;//用户选择的代金券的数量

    private GamePayResultBean gamePayResultBean;

    public GamePayResultBean getGamePayResultBean() {
        return gamePayResultBean;
    }

    public void setGamePayResultBean(GamePayResultBean gamePayResultBean) {
        this.gamePayResultBean = gamePayResultBean;
    }

    public GamePayUserStatus(String gameId, int money) {
        this.gameId = gameId;
        this.money = money;
    }


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getInputMoney() {
        return inputMoney;
    }

    public void setInputMoney(int inputMoney) {
        this.inputMoney = inputMoney;
    }

    public Map<String, Integer> getSelectCouponCountMap() {
        return selectCouponCountMap;
    }

    public void setSelectCouponCountMap(Map<String, Integer> selectCouponCountMap) {
        this.selectCouponCountMap = selectCouponCountMap;
    }
    public float getRealMoney(){
        return money==0?inputMoney:money;
    }
    /**
     * 更新ui，主要更新
     */
    public void calculateUIData() {
        try {
            float realMoney=getRealMoney();
            float aileMoney=realMoney*gamePayResultBean.getRate();
            //计算当前选择的代金券总额是否超出了realMoney的一半
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
