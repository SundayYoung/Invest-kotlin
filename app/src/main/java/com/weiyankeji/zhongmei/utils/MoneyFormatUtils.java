package com.weiyankeji.zhongmei.utils;


import com.weiyankeji.library.utils.NumberUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;

import java.text.DecimalFormat;

/**
 * @aythor: lilei
 * time: 2017/8/16  下午3:01
 * function:
 * 金钱格式化工具类
 */

public class MoneyFormatUtils {


    /**
     * 一百万 单位：分
     */
    public static final int TENTHOUSANDYUAN = 100000000;

    /**
     * string -> string
     * 加逗号
     *
     * @param
     * @return
     */
    public static String sFormatS(String mSMoney) {
        StringBuilder mStringBuilder = new StringBuilder();
        int mPointNum = mSMoney.indexOf(".");
        String mContainPoit = mSMoney.substring(mPointNum, mSMoney.length());
        mStringBuilder.append(mContainPoit);
        String mSubString;
        for (int i = mPointNum - 1; i >= 0; i -= 3) {
            if (i - 2 > 0) {
                mSubString = mSMoney.substring(i - 2, i + 1);
                mStringBuilder.insert(0, mSubString);
                mStringBuilder.insert(0, ",");
            } else {
                mSubString = mSMoney.substring(0, i + 1);
                mStringBuilder.insert(0, mSubString);
            }
        }
        return mStringBuilder.toString();

    }

    /**
     * @param mSMoney 金钱
     * @param mBool   是否保留小数点后两位
     * @return
     */
    public static String sFormatSForComma(String mSMoney, boolean mBool) {
        StringBuilder mStringBuilder = new StringBuilder();
        String mSubString;
        for (int i = mSMoney.length() - 1; i >= 0; i -= 3) {
            if (i - 2 > 0) {
                mSubString = mSMoney.substring(i - 2, i + 1);
                mStringBuilder.insert(0, mSubString);
                mStringBuilder.insert(0, ",");
            } else {
                mSubString = mSMoney.substring(0, i + 1);
                mStringBuilder.insert(0, mSubString);
            }
        }
        return mBool ? mStringBuilder.toString() + ".00" : mStringBuilder.toString();
    }


    /**
     * 金钱格式化
     *
     * @param mMoney
     * @return
     */
    public static String lFormatS(Long mMoney) {
        String result;
        String valueMoney = String.valueOf(mMoney).trim();
        if (valueMoney.length() == 1) {
            result = "0.0" + valueMoney;
        } else if (valueMoney.length() == 2) {
            result = "0." + valueMoney;
        } else {
            result = valueMoney.substring(0, valueMoney.length() - 2) + "." + valueMoney.substring(valueMoney.length() - 2, valueMoney.length());
        }
        return sFormatS(result);
    }

    /**
     * 金钱格式化 不带小数点
     *
     * @param money 分为单位
     * @return
     */
    public static String lFormatNoPoint(Long money) {
        String unit = getMoneyUnit(money);
        long calculateMoney;
        if (money >= 10000000000f) {
            calculateMoney = money / 10000000000L;
        } else if (money >= 100000000) {
            calculateMoney = money / 1000000;
        } else {
            calculateMoney = money / 100;
        }
        return sFormatSForComma(calculateMoney + "", false) + unit;
    }

    /**
     * 金钱格式化 不带小数点 不带单位 按元返回
     *
     * @param money 分为单位
     * @return
     */
    public static String lFormatYuanNoPoint(Long money) {
        long calculateMoney = money / 100;
        return sFormatSForComma(calculateMoney + "", false);
    }

    /**
     * 获取单位
     *
     * @param money 分为单位
     * @return
     */
    public static String getMoneyUnit(Long money) {
        if (money >= TENTHOUSANDYUAN) {
            return ZMApplication.getZMApplication().getString(R.string.ten_thousand_yuan);
        } else {
            return ZMApplication.getZMApplication().getString(R.string.yuan);
        }
    }

    /**
     * 是否大于万元
     * @param money
     * @return
     */
    public static boolean isThousand(Long money) {
        if (money >= 1000000) {
            return true;
        }
        return false;
    }


    /**
     * 返回万为单位
     *
     * @param money 分位单位 300000 = 0.3万
     * @return
     */
    public static String getThousandYuan(long money) {
        double tempDate = money * 0.000001;
        DecimalFormat df = new DecimalFormat("######.##");
        return df.format(tempDate);
    }

    /**
     * 年化收益率计算 保留1位小数点
     *
     * @param yield
     * @return
     */
    public static String yieldPointDouble(int yield) {
        return yieldPointDouble(yield, 1);
    }

    /**
     * 年化收益率计算 保留设置位小数点
     *
     * @param yield
     * @return
     */
    public static String yieldPointDouble(int yield, int length) {
        double data = yield * 0.01f;
        return moneyPoint(data, length) + "%";
    }

    public static String yieldPointRuleOne(int yield) {
        double data = yield * 0.01f;
        DecimalFormat df = new DecimalFormat("##0.0#");
        return df.format(data);
    }

    /**
     * 设置金额
     *
     * @param data
     * @param length 小数点位数
     * @return
     */
    public static String moneyPoint(double data, int length) {
        if (length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append("0");
            }
            DecimalFormat df = new DecimalFormat("#####0." + builder.toString());
            return df.format(data);
        }
        return data + "";
    }

    /**
     * 分转元 double 类型
     */
    public static String doubleChangeF2Y(long amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00"); //格式化设置
        return decimalFormat.format(NumberUtil.getDouble(lFormatS(amount).replace(",", "")));
    }

    /**
     * 单位规则 + 小数规则1
     * 金额小于1,000,000元，单位“元”；金额大于等于1,000,000元，单位“万元”，保留小数位数时，向下取整。
     * 例如：1,000元，显示1,000元；1,030,000元，显示103万元；1,031,000元，显示103.1万元；137,247,594元，显示1372.47万元
     * @param amount 金额 单位：分
     */
    public static String unitRule(long amount) {
        double bei = amount >= TENTHOUSANDYUAN ? 0.000001 : 0.01;
        double tempDate = amount * bei;
        DecimalFormat df = new DecimalFormat("#,###.##"); //格式化，三位隔开，最后保留两位小数
        return df.format(tempDate);
    }

    /**
     * 单位：元
     * 小数规则1：
     * 有两位小数显示两位小数，有一位小数显示一位小数，无小数显示整数。
     * 例如：345元；234.4元；372.98元
     * @param amount 金额 单位：分
     */
    public static String decimalFormatRuleOne(long amount) {
        double tempDate = amount * 0.01;
        DecimalFormat df = new DecimalFormat("#,###.##"); //格式化，三位隔开，最后保留两位小数
        return df.format(tempDate);
    }

}
