package com.weiyankeji.zhongmei.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyankeji.library.log.LogUtils;
import com.weiyankeji.library.net.client.BaseServerResponse;
import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.DensityUtil;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ScreenUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.activity.MainActivity;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.fragment.CommonAuthFragment;
import com.weiyankeji.zhongmei.ui.fragment.CommonWebViewFragment;
import com.weiyankeji.zhongmei.ui.fragment.LoginFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestTagsBean;
import com.weiyankeji.zhongmei.utils.net.SignInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.CertificatePinner;
import retrofit2.Call;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * @aythor: lilei
 * time: 2017/9/4  下午7:59
 * function:
 */

public class CommonUtils {

    /**
     * SSL Pinning 获取证书
     * @return certificata
     */
    public static CertificatePinner getCertificata() {

        Certificate ca = null;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = ZMApplication.getZMContext().getResources().openRawResource(R.raw.zm);

            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        String certPin = "";
        if (ca != null) {
            certPin = CertificatePinner.pin(ca);
        }
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(UrlConfig.RELEASE_BASE_URL, certPin)
                .build();

        return certificatePinner;
    }

    /**
     * 网络请求数据
     * @param url
     * @param request
     * @param callBack
     */
    public static void requestData(String url, Object request, RestBaseCallBack callBack) {
        RestService mRestService = RestApiProvider.getInstance().withInterceptor(new SignInterceptor(ZMApplication.getZMContext()), CommonUtils.getCertificata()).builder().getApiService(RestService.class);
        Call<BaseServerResponse> mCall;
        mCall = mRestService.postData(url, request);
        mCall.enqueue(callBack);
    }

    /**
     * 身份证验证
     *
     * @param num
     * @return
     */
    public static boolean checkIDCard(String num) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }

    /**
     * 密码明文、密文切换
     *
     * @param mEtPass
     * @param isExpress
     * @return
     */
    public static boolean expressCiphertext(EditText mEtPass, boolean isExpress) {
        if (!isExpress) {
            mEtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isExpress = true;
        } else {
            mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isExpress = false;
        }
        mEtPass.setSelection(mEtPass.getText().toString().length());
        return isExpress;
    }

    /**
     * 密码：8-20数字与字母组合
     *
     * @param pass
     * @return
     */
    public static boolean checkPass(String pass) {
        if (pass == null) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(pass).matches();
    }

    /**
     * 密码明文、密文切换
     *
     * @param mIbButton
     * @param mIsDesensi true密文、false明文
     * @return
     */
    public static boolean setIsDesensi(EditText editText, ImageButton mIbButton, boolean mIsDesensi) {
        if (mIsDesensi) {
            mIbButton.setImageResource(R.drawable.login_icon_eye_close);
        } else {
            mIbButton.setImageResource(R.drawable.login_icon_eye_open);
        }
        return CommonUtils.expressCiphertext(editText, mIsDesensi);
    }

    /**
     * 标签颜色 可修改背景颜色和字体大小
     *
     * @param context
     * @param root
     * @param labelList
     * @param backColor
     * @param size
     */

    public static void buildLabelIcons(Context context, LinearLayout root, List<InvestTagsBean> labelList, int backColor, int size) {
        if (root == null) {
            return;
        }
        root.removeAllViews();
        if (ExtendUtil.listIsNullOrEmpty(labelList)) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.setMargins(0, 0, DensityUtil.dp2px(context, 5), 0);
        for (InvestTagsBean label : labelList) {
            if (StringUtil.isNullOrEmpty(label.title)) {
                continue;
            }
            TextView textView = getTextView(context, Color.parseColor(label.color), backColor, Color.parseColor(label.color), 2, layoutParams, label.title, size, 5, 2);
            root.addView(textView);
        }
    }

    /**
     * 标签颜色
     *
     * @param context   上下文
     * @param root      父布局
     * @param labelList 标签信息: 文本内容，type
     */
    public static void buildLabelIcons(Context context, LinearLayout root, List<InvestTagsBean> labelList) {
        if (root == null) {
            return;
        }
        root.removeAllViews();
        if (ExtendUtil.listIsNullOrEmpty(labelList)) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.setMargins(DensityUtil.dp2px(context, 5), 0, 0, 0);
        for (InvestTagsBean label : labelList) {
            if (StringUtil.isNullOrEmpty(label.title)) {
                continue;
            }
            int color = Color.parseColor(label.color);
            TextView textView = getTextView(context, color, 2, layoutParams, label.title, 9); //圆角半径为4
            root.addView(textView);
        }
    }

    /**
     * 标签颜色 可修改字体和边框颜色
     *
     * @param context
     * @param root
     * @param labelList
     * @param dcolor    ：边框颜色
     */
    public static void buildLabelIcons(Context context, LinearLayout root, List<InvestTagsBean> labelList, int dcolor) {
        if (root == null) {
            return;
        }
        root.removeAllViews();
        if (ExtendUtil.listIsNullOrEmpty(labelList)) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.setMargins(DensityUtil.dp2px(context, 5), 0, 0, 0);
        for (InvestTagsBean label : labelList) {
            if (StringUtil.isNullOrEmpty(label.title)) {
                continue;
            }
            TextView textView = getTextView(context, dcolor, 2, layoutParams, label.title, 9); //圆角半径为4
            root.addView(textView);
        }
    }


    public static TextView getTextView(Context context, int textColor, int radius, LinearLayout.LayoutParams layoutParams, String text, int textSize) {
        return getTextView(context, textColor, R.color.white, textColor, radius, layoutParams, text, textSize);
    }


    /**
     * 构造标签样式
     * 标签背景、边框自定义
     *
     * @param context
     * @param textColor
     * @param backgroundColor
     * @param strokeColor
     * @param radius
     * @param layoutParams
     * @param text
     * @param textSize
     * @return
     */
    public static TextView getTextView(Context context, int textColor, int backgroundColor, int strokeColor, int radius, LinearLayout.LayoutParams layoutParams, String text, int textSize) {
        TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.shape_lable_round_rect_orange);
        changeLabelColor(context, textView, strokeColor, backgroundColor, 2, radius);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(DensityUtil.dp2px(context, 2), DensityUtil.dp2px(context, 2), DensityUtil.dp2px(context, 2), DensityUtil.dp2px(context, 2));
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setIncludeFontPadding(false);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return textView;
    }

    /**
     * @param paddLR 左右边距
     * @param paddTB 上下边距
     */
    public static TextView getTextView(Context context, int textColor, int backgroundColor, int strokeColor, int radius, LinearLayout.LayoutParams layoutParams, String text, int textSize, int paddLR, int paddTB) {
        TextView textView = getTextView(context, textColor, backgroundColor, strokeColor, radius, layoutParams, text, textSize);
        textView.setPadding(DensityUtil.dp2px(context, paddLR), DensityUtil.dp2px(context, paddTB), DensityUtil.dp2px(context, paddLR), DensityUtil.dp2px(context, paddTB));
        return textView;
    }

    /**
     * 修改标签的边框和背景
     *
     * @param context
     * @param source
     * @param strokeColor
     * @param backColor
     * @param width
     * @param radius
     */
    public static void changeLabelColor(Context context, View source, int strokeColor, int backColor, int width, float radius) {
        GradientDrawable gradient = (GradientDrawable) source.getBackground();
        if (gradient == null) {
            return;
        }
        gradient.setStroke(width, strokeColor);
        gradient.setCornerRadius(radius);
        gradient.setColor(ContextCompat.getColor(context, backColor));
    }

    /**
     * 改变部分文字的大小
     *
     * @param sourceString 源字符串
     * @param textSizeSp   改变的字体大小
     * @param start        起始字符位置
     * @param end          结束字符位置
     * @return 字符串
     */
    public static Spannable changeStringSize(Context context, String sourceString, int textSizeSp, int start, int end, boolean isChangeStyle) {
        if (sourceString == null) {
            return new SpannableString("");
        }
        Spannable spannable = new SpannableString(sourceString);
        if (start < 0 || start >= end || end > sourceString.length()) {
            return spannable; //起始位置有误返回原字符串
        }
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtil.dp2px(context, textSizeSp)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isChangeStyle) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 改变部分文字的颜色
     *
     * @param sourceString 源字符串
     * @param color        改变的颜色
     * @param start        起始字符位置
     * @param end          结束字符位置
     * @return 字符串e
     */
    public static Spannable changeStringColor(@Nullable String sourceString, @ColorInt int color, int start, int end) {
        if (sourceString == null) {
            return new SpannableString("");
        }
        Spannable spannable = new SpannableString(sourceString);
        if (start < 0 || start >= end || end > sourceString.length()) {
            return spannable; //起始位置有误返回原字符串
        }
        spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannable;
    }

//    /**
//     * 返回日期单位描述
//     *
//     * @param type
//     * @return
//     */
//    public static String getDateTypeText(int type) {
//        String dateType = "";
//        if (type == ConstantUtils.PERIOD_TYPE_DAY) {
//            dateType = ZMApplication.getZMContext().getString(R.string.day);
//        } else if (type == ConstantUtils.PERIOD_TYPE_MONTH) {
//            dateType = ZMApplication.getZMContext().getString(R.string.one_month);
//        }
//        return dateType;
//    }


    /**
     * 返回标的类型描述
     *
     * @param type 1：定制 2：定期 3：网贷
     * @return
     */
    public static String getPeriodTypeText(int type) {
        String dateType = "";
        if (type == ConstantUtils.INVEST_SKU_TYPE_CUSTOM) {
            dateType = ZMApplication.getZMContext().getString(R.string.invest_home_period_costom);
        } else if (type == ConstantUtils.INVEST_SKU_TYPE_STIPULATE) {
            dateType = ZMApplication.getZMContext().getString(R.string.regular_basis);
        }
        return dateType;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (cardId.length() < 16 || cardId.length() > 20) {
            return false;
        }
        return true;
    }

    /**
     * 检查短信验证码是否合法
     *
     * @param code
     * @return
     */

    public static boolean checkMessageCode(String code) {
        return checkMessageCode(code, false);
    }

    /**
     * 检查短信验证码是否合法
     *
     * @param code
     * @param isShowToast 是否显示错误Toast提示
     * @return
     */
    public static boolean checkMessageCode(String code, boolean isShowToast) {
        if (TextUtils.isEmpty(code)) {
            if (isShowToast) {
                ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.toast_vercode_erro_rule));
            }
            return false;
        }
        return true;
    }


    /**
     * 验证手机号
     *
     * @param mobile
     * @return
     */
    public static boolean checkPhoneNumber(String mobile) {
        return checkPhoneNumber(mobile, false);
    }

    /**
     * 验证手机号
     *
     * @param mobile
     * @return
     */
    public static boolean checkPhoneNumber(String mobile, boolean isShowToast) {
        if (!ExtendUtil.isPhoneNumber(mobile)) {
            if (isShowToast) {
                ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.toast_mobile_erro_rule));
            }
            return false;
        }
        return true;
    }

    /**
     * @param activity
     * @param type
     */
    public static void intentMsgValida(Activity activity, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, type);
        activity.startActivityForResult(FragmentContainerActivity.getFragmentContainerActivityIntent(activity, CommonAuthFragment.class, bundle), ConstantUtils.INTENT_REQUEST);
    }

    /**
     * 跳转网页
     *
     * @param fragment
     * @param title
     * @param url
     */
    public static void intentWebView(BaseFragment fragment, String title, String url) {
        intentWebView(fragment, 0, title, url);
    }

    /**
     * 跳转网页
     *
     * @param fragment
     * @param type     类型 - 不用就传递0
     * @param title
     * @param url
     */
    public static void intentWebView(BaseFragment fragment, int type, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_URL, url);
        bundle.putString(ConstantUtils.KEY_TITLE, title);
        bundle.putInt(ConstantUtils.KEY_TYPE, type);
        fragment.startFragment(CommonWebViewFragment.class, bundle);
    }


    /**
     * 跳登录页
     *
     * @param fragment
     * @param type
     */
    public static void intentLoginFragment(BaseFragment fragment, int type) {
        Intent intent = FragmentContainerActivity.getFragmentContainerActivityIntent(fragment.getActivity(), LoginFragment.class);
        intent.putExtra(ConstantUtils.KEY_TYPE, type);
        fragment.getActivity().startActivity(intent);
    }

    /**
     * 判断是否有虚拟键盘
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;

    }

    /**
     * activity中显示小红点逻辑
     *
     * @param activity    mainactivity
     * @param time        网络请求来的时间
     * @param currentItem 当前页面
     * @param imageView   顶部的view
     */
    public static void messageNotificationOfActivity(MainActivity activity, Long time, int currentItem, int defaultCurrent, ImageView imageView) {
        String uuid = "";
        SharedPreferences mShare = SharedPreferencesUtil.getSharedPreferences(activity, ConstantUtils.SHAREDPREFERENCES_NAME);
        if (!UserUtils.isUserLogin()) {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MINE, false);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, false);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_TIME, 0L);
        }
        long mMessageTime = (Long) SharedPreferencesUtil.get(mShare, ConstantUtils.SP_MESSAGE_TIME, 0L);
        boolean isChange = (time != mMessageTime);
        //时间不一样或者是在显示。
        if (isChange || ((Boolean) SharedPreferencesUtil.get(mShare, ConstantUtils.SP_MESSAGE_MINE, false))) {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MINE, true);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_TIME, time);
        } else {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MINE, false);
        }
        if (isChange || ((Boolean) SharedPreferencesUtil.get(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, false))) {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, true);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_TIME, time);
        } else {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, false);
        }
        setNotificationPointOfActivity(activity, mShare, currentItem, defaultCurrent, imageView);

    }

    /**
     * setNotificationPointOfActivity（messageNotificationOfActivity中调用）
     * 只是拿取显示
     *
     * @param activity
     * @param mShare
     * @param imageView
     */
    public static void setNotificationPointOfActivity(MainActivity activity, SharedPreferences mShare, int currentItem, int defaultCurrent, ImageView imageView) {
        String uuid = "";
        if (!UserUtils.isUserLogin()) {
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MINE, false);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, false);
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_TIME, 0L);
        }
        //拿取顶部是否显示
        boolean mIsMessage = (Boolean) SharedPreferencesUtil.get(mShare, ConstantUtils.SP_MESSAGE_MINE, false);
        //顶部是否显示
        boolean mIsTopMessage = (Boolean) SharedPreferencesUtil.get(mShare, ConstantUtils.SP_MESSAGE_MESSAGE, false);
        //底部消息
        if (mIsMessage && (currentItem != defaultCurrent)) {
            activity.showNotification();
        } else {
            activity.hideNotification();
           /* SharedPreferencesUtil.put(mShare, uuid + ConstantUtils.SP_MESSAGE_TIME, time);*/
            SharedPreferencesUtil.put(mShare, ConstantUtils.SP_MESSAGE_MINE, false);
        }
        if (imageView != null) {
            //顶部消息
            if (mIsTopMessage) {
                imageView.setImageResource(R.drawable.my_icon_message_remind);

            } else {
                imageView.setImageResource(R.drawable.my_icon_message);
            }

        }

    }


    /**
     * tabhost设置margin
     *
     * @param context
     * @param tabs
     * @param margin
     */
    public static void setIndicator(Context context, TabLayout tabs, int margin) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
        }
        if (tabStrip == null) {
            return;
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
        }

        if (ll_tab == null) {
            return;
        }

        int mar = DensityUtil.dp2px(context, margin);
        for (int i = 0, childCount = ll_tab.getChildCount(); i < childCount; i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = mar;
            params.rightMargin = mar;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    //只能输入两位小数
    public static void twoDecimal(String s, EditText editText) {

        if (s.contains(".")) {
            if (s.length() - 1 - s.indexOf(".") > 2) {
                s = s.substring(0, s.indexOf(".") + 3);
                editText.setText(s);
                editText.setSelection(s.length());
            }
        }
        if (s.substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        if (s.startsWith("0")
                && s.trim().length() > 1) {
            if (!s.substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }

    }

    /**
     * Edittext禁止复制粘贴
     *
     * @param editText
     */
    public static void setEditCallBack(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }


    /**
     * 获取更加 375 * 110 设计 宽高比
     *
     * @param context
     * @return
     */
    public static float getHeighScale(Context context) {
        return ScreenUtil.getScreenWidth(context) * 110 / 360;
    }
}
