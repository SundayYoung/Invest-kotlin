package com.weiyankeji.zhongmei.utils;

/**
 * Created by caiwancheng on 2017/7/27.
 */

public class ConstantUtils {

    /**
     * third SDK
     */
    public static final String BUGLY_APP_ID = "bbb39b36ac";
    public static final String WX_APP_ID = "wx48c1029246c2e946";
    public static final String MTA_APP_KEY = "A81NUD7LCL1I";

    /**
     * 充值-2 和 提现-1
     */
    public static final int TYPE_WITHDRAW = 1;
    public static final int TYPE_RECHARGE = 2;
    /**
     * 我的卡券 未使用
     */
    public static final int TICKET_UNUSE = 0;
    /**
     * 我的卡券 已使用
     */
    public static final int TICKET_USED = 1;
    /**
     * 我的卡券 已过期
     */
    public static final int TICKET_TIMED = 2;
    /**
     * 标的卡券 可用
     */
    public static final int TICKET_AVAILABLE = 13;
    /**
     * 标的卡券 不可用
     */
    public static final int TICKET_UNAVAILABLE = 14;

    /**
     * 首页 和理财首页 定制理财
     */
    public static final int TYPE_CUSTOM_INVEST = 1;
    /**
     * 首页 和理财首页 精选理财
     */
    public static final int TYPE_SELECT_INVEST = 2;
    /**
     * 首页 和理财首页 精选定期
     */
    public static final int TYPE_SELECT_STIPULATE_INVEST = 3;


    /**
     * 标的类型 定制
     */
    public static final int INVEST_SKU_TYPE_CUSTOM = 1;
    /**
     * 标的类型 定期
     */
    public static final int INVEST_SKU_TYPE_STIPULATE = 2;
    /**
     * 标的类型 网贷
     */
    public static final int INVEST_SKU_TYPE_NET_LOAN = 3;

    /**
     * 周期类型 - 天
     */
    public static final int PERIOD_TYPE_DAY = 1;

    /**
     * 周期类型 - 月
     */
    public static final int PERIOD_TYPE_MONTH = 2;

    /**
     * 卡券类型 代金券
     */
    public static final int MARKET_CARD_TYPE_VOUCHERS = 1;

    /**
     * 卡券类型 加息券
     */
    public static final int MARKET_CARD_TYPE_RATE_COUPON = 0;


    /**
     * 一般用来传递一些数据
     */
    public static final String KEY_DATA = "data";

    /**
     * 一般用来传递标题
     */
    public static final String KEY_TITLE = "title";

    /**
     * 一般用来传递id
     */
    public static final String KEY_ID = "id";
    /**
     * 一般用来传递id
     */
    public static final String KEY_URL = "url";
    /**
     * 一般用来传递value
     */
    public static final String KEY_VALUE = "value";

    /**
     * 一般用来传递类型区别
     */
    public static final String KEY_TYPE = "type";

    /**
     * 众美的 Sharedpreferences 的文件名
     */
    public static final String SHARED_FLIE = "zhongmei";

    /**
     * SharedPreferences name
     */
    public static final String SHAREDPREFERENCES_NAME = "zhongmei";
    /**
     * 配置 目前是更新策略，小黄条
     * SharedPreferences name
     */
    public static final String SHAREDPREFERENCES_NAME_CONFIG = "config";

    /**
     * SharedPreferences key ：我的页面金额是否脱敏显示
     */
    public static final String SP_MINE_DESENSITIZATION = "mine_desensitization";
    /**
     * SharedPreferences key ：消息提示（底部小红点）
     */
    public static final String SP_MESSAGE_MINE = "message_mine";
    /**
     * SharedPreferences key ：消息提示（时间）
     */
    public static final String SP_MESSAGE_TIME = "message_time";
    /**
     * SharedPreferences key ：消息提示（顶部小红点）
     */
    public static final String SP_MESSAGE_MESSAGE = "message_message";
    /**
     * SharedPreferences key ：Home页 小黄条ID
     */
    public static final String SP_HOME_NOTICE_ID = "home_notice_id";
    /**
     * SharedPreferences key ：检测更新策略缓存
     */
    public static final String SP_VERSION_CHECK = "version_check";


    /**
     * 我的定制与定期标志
     */
    public static final String SKEY_SIGN_TAG = "mine_sign";

    /**
     * 投资中、已经结束
     */
    public static final int STYPE_INVESTMENT = 1;
    public static final int STYPE_ISOVER = 2;

    /**
     * 投资详情
     */
    public static final String SKEY_CUSTOM_DETAILS = "custom_details";

    /**
     * status状态
     */
    public static final int INVESTMENT_UNKNOWN = 0;
    public static final int INVESTMENT_WAIT_ON_SHELVES = 1;
    public static final int INVESTMENT_IN_INVESTMENT = 2;
    public static final int INVESTMENT_BACK_PAYMENT_3 = 3;
    public static final int INVESTMENT_BACK_PAYMENT_4 = 4;
    public static final int INVESTMENT_BACK_PAYMENT_5 = 5;
    public static final int INVESTMENT_PENDING_PAYMENT_6 = 6;
    public static final int INVESTMENT_PENDING_PAYMENT_7 = 7;
    public static final int ISOVER_INVESTMENT_WAIT_PAYMENT = 8;
    public static final int INVESTMENT_PENDING_PAYMENT_9 = 9;
    public static final int ISOVER_FAILURE = 10;
    public static final int ISOVER_BACK_PAYMENT = 11;
    public static final int ISOVER_HAVE_INVESTMENT_11 = 12;
    public static final int ISOVER_FAILURE_INVEST = 13;

    /**
     * 每一页请求的条目
     */
    public static final int PAGE_SIZE = 20;

    /**
     * onst SMS_REGISTER        = 0;//注册发送短信认证码
     * const SMS_FORGET_PASSWORD = 1;//找回密码发送短信认证码
     * const SMS_RECHARGE        = 2;//充值短信通知
     * const SMS_CHANGE_MOBILE   = 3;//修改绑定手机 -
     * const SMS_CHANGE_TRADE    = 4;//修改交易密码 -
     * const SMS_BIND_BANKCODE   = 5;//绑定银行卡
     * const SMS_CHANGE_BANKCODE = 6;//修改绑定银行卡
     * const SMS_CHANGE_MOBILE2  = 7;//绑定新手机
     * const SMS_FORGET_TRADE    = 8;//忘记交易密码
     * 发送验证码
     */
    public static final int VERIFY_TYPE_REGI = 0;
    public static final int VERIFY_TYPE_FORGO_TPASSWORD = 1;
    public static final int VERIFY_TYPE_RECHARGE = 2;
    public static final int VERIFY_TYPE_LOGIN_CHANGE_PASSWORD = 3;
    public static final int VERIFY_TYPE_CHANGE_TRADE = 4;
    public static final int VERIFY_TYPE_BANK_BIND = 5;
    public static final int VERIFY_TYPE_BANK_CHANGE = 6;
    public static final int VERIFY_TYPE_NEW_PHONE = 7;
    public static final int VERIFY_TYPE_FORGET_TRADE = 8;

    /**
     * 请求成功码
     */
    public static final int SUCCESS_VERFICATION_CODE = 0;

    /**
     * 开通支付账户状态 服务器返回0,1,3,7
     * 0：什么都没开通
     * 1：仅实名认证
     * 2：仅绑卡
     * 3：实名认证 + 绑卡
     * 4：仅支付密码
     * 5：实名认证 + 交易密码
     * 6：绑卡 + 交易密码
     * 7：绑卡 + 实名认证 + 交易密码
     */

    public static final int ACCOUNT_NOTHING = 0;
    public static final int ACCOUNT_NAME_AUTH = 1;
    public static final int ACCOUNT_BIND_CARD = 2;
    public static final int ACCOUNT_AUTH_AND_CARD = 3;
    public static final int ACCOUNT_PASSWORD = 4;
    public static final int ACCOUNT_AUTH_AND_PASSWORD = 5;
    public static final int ACCOUNT_BIND_AND_PASSWORD = 6;
    public static final int ACCOUNT_ALL_VALIDA = 7;

    /**
     * 是否认证
     */
    public static final int AUTHENTICATION_NO = 0;
    public static final int AUTHENTICATION_IN = 1;


    /**
     * INTENT 跳转的时候，需要ForResult的时候使用
     */
    public static final int INTENT_REQUEST = 1;
    public static final int INTENT_RESPONSE = 1;

    /**
     * 资产类型
     */
    public static final int ASSETS_ALL = 0;
    public static final int ASSETS_CUSTOMIZATION = 1;
    public static final int ASSETS_REGULAR = 2;
    public static final int ASSETS_LOAN = 3;

    /**
     * 日期格式类
     */

    public static final String TIME_FORMAT_POINT = "yyyy.MM.dd";
    public static final String TIME_FORMAT_DEX = "yyyy-MM-dd";


    /**
     * 短信验证码的长度
     */
    public static final int MESSAGE_VALIDA_CODE_LENG = 6;


    /**
     * 充值类型
     */

    public static final int RECHARGE_TYPE = 1; //银行卡

    /**
     * 标的状态 募集中 - 立即投资
     * 4 5 8 10 11 为 抢光了
     */
    public static final int SKU_TYPE_ING = 2;

    /**
     * 标的状态 募集结束 - 募集结束
     * 4 5 8 10 11 为 抢光了
     */
    public static final int SKU_TYPE_END = 3;

    /**
     * 请求短信验证码
     */
    public static final int MAX_MSG_CODE_LENGTH = 6;

    /**
     * 交易密码长度
     */
    public static final int MAX_PAY_PW_LENGTH = 6;

    /**
     * 确定强制升级
     */
    public static final int FORCE_UPGRADE_TRUE = 1;

    /**
     * 取消强制升级
     */
    public static final int FORCE_UPGRADE_FALSE = 0;

    /**
     * 传递js方法名字和字段名字
     */
    public static final String H5_METHOD = "getQrCodeInfo";
    public static final String H5_SKEY_MOBILE = "mobile";
    public static final String H5_SKEY_AVATAR = "avatar";
    public static final String H5_SKEY_QRCODE = "qr_code";

    /**
     * 奖励明细
     */
    public static final int BOUNS_DETAILS_INVITEE = 1;
    public static final int BOUNS_DETAILS_DAYS = 2;


    public static final String DEBUG_URL = "debug_url";
    public static final String DEBUG_SSL = "debug_SSL";

}
