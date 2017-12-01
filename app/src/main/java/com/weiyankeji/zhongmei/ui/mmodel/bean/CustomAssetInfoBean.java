package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午5:53
 * function:
 */

public class CustomAssetInfoBean implements Serializable{


    /**
     * total_asset : long,总资产（单位：分）
     * total_earning : long,累计收益（单位：分）
     * unback_interest : long,待收利息（单位：分）
     * road_asset : long,在途资产（单位：分）
     * available : long,可用余额（单位：分）
     * unback_principal : long,待收本金（单位：分）
     */

    public long total_asset;
    public long total_earning;
    public long unback_interest;
    public long road_asset;
    public long available;
    public long unback_principal;


}
