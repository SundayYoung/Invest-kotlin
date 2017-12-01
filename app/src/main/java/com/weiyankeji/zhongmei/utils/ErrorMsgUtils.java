package com.weiyankeji.zhongmei.utils;

import android.text.TextUtils;

import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.event.LoginSuccessEvent;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by caiwancheng on 2017/9/14.
 */

public class ErrorMsgUtils {


    /**
     * 需要重新登陆传递的type参数
     */
    public static final int NEED_LOGIN_RESPONSE_CODE = 555;

    //20000 token 失效
    public static final int TOKEN_INVALID = 20000;
    //20001 需要重新登陆
    public static final int NEED_RELOGIN = 20001;
    //22001 手机号已存在
    private static final int PHONE_EXIST = 22001;
    //22002 手机号不存在
    private static final int PHONE_NO_EXIST = 22002;
    //22003 不支持的格式
    private static final int ERROR_FORMAT = 22003;
    //22004 邀请人账号不存在
    private static final int INVITER_ACCOUNT_NO_EXIST = 22004;
    //22005 密码不正确
    private static final int ERROR_PASSWORD = 22005;
    //22006 身份证号与实名认证身份证号不匹配
    public static final int ID_CARD_NO_MATCH_FOR_AUTH = 22006;
    //22007 短信验证状态失效
    public static final int MESSAGE_STATUS_INVALID = 22007;
    //22008 短信验证码失效
    private static final int MESSAGE_VALIDA_CODE_INVALID = 22008;
    //非法的标的认证码
    public static final int PARAMS_SKU_CODE_ERROR = 22009;
    //认证码已经被绑定
    public static final int PARAMS_SKU_CODE_ISBAND = 22010;

    //25102 短信验证码失效
    private static final int RPC_CONFIG_NO_EXIST = 25102;
    //25001 登陆失效
    private static final int LOGIN_INVALID = 25001;
    //25201 交易密码已存在
    private static final int TRADE_PASSWORD_EXIST = 25201;
    //25202 检测账户是否存在异常
    private static final int ACCOUNT_ERROR = 25202;
    //标的不存在
    public static final int SKU_NO_ERROR = 42002;
    //该状态下的标不允许投资
    public static final int SKU_STATE_ERROR = 42003;
    //投资金额大于可投金额
    public static final int SKU_AMOUNT_ERROR = 42005;
    //卡券信息失效
    public static final int TICKET_ERROR = 42012;
    //卡券不可用
    public static final int TICKET_CANNOT = 42013;
    //找不到投资记录
    public static final int INVEST_RECORD_NO_FOUND = 42014;
    //未知的邀请人
    public static final int UNKNOW_INVITER = 42006;
    //未知的被邀请人
    public static final int UNKNOW_INVITEE = 42007;
    //25101 表单验证错误
    private static final int FROM_VALIDA_ERROR = 25101;
    //    逻辑实现异常
    private static final int LOGIC_CALL_FAILED = 25002;


    //未知错误
    private static final int UNKNOW_ERROR = 40002;
    //42001 参数异常
    private static final int ARGUMENT_ERROR = 42001;
    //系统繁忙
    private static final int DEFAULT_ERROR = -1;
    //24101 rpc请求异常
    private static final int RPC_RESPONSE_ERROR = 24101;
    //27001 系统发生未知错误
    private static final int SYSTEM_UNKONW_ERROR = 27001;
    //27002 类方法未发现
    private static final int CLASS_NO_FOUND = 27002;
    //本地网络异常（与library的 NET_ERROR_CODE 值保持一致）
    private static final int NET_ERROR_CODE = 445566;

    //支付 密码错误
    public static final int PAY_PASSWORD_ERROR = 51006;
    //支付 可用余额不足
    public static final int PAY_MONEN_ENOUTH = 51009;

    /**
     * 显示错误信息Toast
     *
     * @param code 根据错误码来
     * @param msg
     */
    public static boolean showNetErrorToastOrLogin(BaseFragment fragment, int code, String msg) {
        return showNetErrorToastOrLogin(fragment, code, msg, false);
    }

    /**
     * 显示错误信息Toast
     *
     * @param code     根据错误码来
     * @param msg
     * @param isFinish 是否结束当前页面
     */
    public static boolean showNetErrorToastOrLogin(BaseFragment fragment, int code, String msg, boolean isFinish) {
        if (TextUtils.isEmpty(msg)) {
            return false;
        }
        if (isNetError(code)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
            return true;
        } else if (isServiceError(code)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.server_somewhere_please_try_again));
            return true;
        } else if (isNeedLogin(code)) {
            ToastUtil.showShortToast(fragment.getActivity(), msg);
            CommonUtils.intentLoginFragment(fragment, -1);
            if (isFinish) {
                fragment.getActivity().finish();
            }
            return true;
        }
        return false;
    }

    /**
     * 显示错误信息Toast - 会显示网络错误页
     *
     * @param code 根据错误码来
     * @param msg
     */
    public static boolean showNetErrorPageOrLogin(BaseFragment fragment, int code, String msg) {
        return showNetErrorPageOrLogin(fragment, code, msg, false);
    }

    /**
     * 显示错误信息Toast -会显示错误页
     *
     * @param fragment
     * @param code
     * @param msg
     * @param isFinish
     * @return
     */
    public static boolean showNetErrorPageOrLogin(BaseFragment fragment, int code, String msg, boolean isFinish) {
        if (TextUtils.isEmpty(msg)) {
            return false;
        }
        if (isNetError(code) || isServiceError(code) ) {
            showNetErrView(fragment);
            return true;
        } else if (isNeedLogin(code)) {
            ToastUtil.showShortToast(fragment.getActivity(), msg);
            CommonUtils.intentLoginFragment(fragment, -1);
            if (isFinish) {
                fragment.getActivity().finish();
            }
            return true;
        }
        return false;
    }


    /**
     * 弹出错误码的错误信息
     *
     * @param code
     * @return
     */
    public static boolean isCanShowMsg(int code) {
        switch (code) {
            case TOKEN_INVALID:
            case NEED_RELOGIN:
            case PHONE_EXIST:
            case PHONE_NO_EXIST:
            case ERROR_FORMAT:
            case INVITER_ACCOUNT_NO_EXIST:
            case ERROR_PASSWORD:
            case ID_CARD_NO_MATCH_FOR_AUTH:
            case MESSAGE_STATUS_INVALID:
            case MESSAGE_VALIDA_CODE_INVALID:
            case LOGIN_INVALID:
            case RPC_CONFIG_NO_EXIST:
            case TRADE_PASSWORD_EXIST:
            case ACCOUNT_ERROR:
                return true;
            default:
                return false;
        }
    }

    /**
     * 是否服务器异常
     *
     * @param code
     * @return
     */
    public static boolean isServiceError(int code) {
        switch (code) {
            case ARGUMENT_ERROR:
            case RPC_RESPONSE_ERROR:
            case SYSTEM_UNKONW_ERROR:
            case CLASS_NO_FOUND:
            case DEFAULT_ERROR:
            case LOGIC_CALL_FAILED:
                return true;
            default:
                return false;
        }
    }

    /**
     * 是否网络异常
     *
     * @param code
     * @return
     */
    public static boolean isNetError(int code) {
        switch (code) {
            case NET_ERROR_CODE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 显示网络异常，尝试刷新的界面
     *
     * @param fragment
     */
    public static void showNetErrView(BaseFragment fragment) {
        fragment.showNetErrorPage();
    }

    /**
     * 是否需要登录
     *
     * @param code
     * @return
     */
    public static boolean isNeedLogin(int code) {
        switch (code) {
            case TOKEN_INVALID:
            case NEED_RELOGIN:
                UserUtils.getInstance().deleteUser();
                SharedPreferencesUtil.clear(ZMApplication.getZMContext(), ConstantUtils.SHAREDPREFERENCES_NAME);
                EventBus.getDefault().post(new LoginSuccessEvent(ZMApplication.getZMContext().getString(R.string.login)));
                return true;
            default:
                return false;
        }
    }

    /**
     * 需要重新登陆
     *
     * @param code
     */
    public static void needReLogin(BaseFragment fragment, int code) {
        switch (code) {
            case TOKEN_INVALID:
            case NEED_RELOGIN:
                CommonUtils.intentLoginFragment(fragment, -1);
                break;
            default:
        }
    }
}
