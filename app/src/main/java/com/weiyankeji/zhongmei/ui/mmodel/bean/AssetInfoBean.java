package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/8/29  上午10:14
 * function:
 */

public class AssetInfoBean implements Serializable {

    /**
     * total_asset : long,总资产（单位：分）
     * total_earning : long,累计总收益（单位：分）
     * unback_principal : long,待收本金（单位：分）
     * unback_interest : long,待收利息（单位：分）
     */

    public long total_asset;
    public long total_earning;
    public long unback_principal;
    public long unback_interest;


}
