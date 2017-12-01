package com.weiyankeji.zhongmei.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.fragment.AuthNameFragment;
import com.weiyankeji.zhongmei.ui.fragment.BankCardInfoFragment;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.fragment.CommonAuthFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * @aythor: lilei
 * time: 2017/8/24  下午3:52
 * function:
 */

public class UserUtils {
    private static final String TAG_USER_MODEL = "user";
    private static UserUtils sInstance;
    private User mUser;

    private UserUtils() {
    }

    private static String decoder3DES(byte[] str, SecretKey generateSecret) {
        try {
            // 加密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, generateSecret);
            System.out.println(2);
            byte[] result = cipher.doFinal(str);
            System.out.println(3);

            return new String(result, "utf-8");
        } catch (Exception e) {
            System.out.println("解密出错:" + e.getMessage());
        }
        return null;
    }

    public static synchronized UserUtils getInstance() {
        if (sInstance == null) {
            sInstance = new UserUtils();
        }
        return sInstance;
    }


    public static boolean isUserLogin() {
        return UserUtils.getInstance().getUserObject() != null;
    }

    public User getUser() {
        mUser = getUserObject();
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
        saveUserObject(mUser);
    }

    public User getUserObject() {
        InputStream inStream = null;
        ObjectInputStream objInStream = null;
        User user = null;
        try {
            inStream = ZMApplication.getZMContext().openFileInput(TAG_USER_MODEL);
            objInStream = new ObjectInputStream(inStream);
            user = (User) objInStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objInStream != null) {
                try {
                    objInStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }


    public void saveUserObject(User user) {
        OutputStream outStream = null;
        ObjectOutputStream objOutStream = null;
        try {
            outStream = ZMApplication.getZMContext().openFileOutput(TAG_USER_MODEL, Context.MODE_PRIVATE);
            objOutStream = new ObjectOutputStream(outStream);
            objOutStream.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (objOutStream != null) {
                try {
                    objOutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteUser() {
        ZMApplication.getZMContext().deleteFile(TAG_USER_MODEL);
        mUser = null;
    }

    /**
     * 获取验证类型码
     *
     * @return
     */
    public int getValidaType() {
        mUser = getUserObject();
        return mUser.validate_type;
    }

    /**
     * 是否实名认证
     *
     * @return
     */
    public boolean isRealAuth() {
        if (getValidaType() >= ConstantUtils.ACCOUNT_NAME_AUTH) {
            return true;
        }
        return false;
    }

    /**
     * 是否设置支付密码
     *
     * @return
     */
    public boolean isSetPayPw() {
        if (getValidaType() >= ConstantUtils.ACCOUNT_ALL_VALIDA) {
            return true;
        }
        return false;
    }

    /**
     * 是否绑定银行卡
     *
     * @return
     */
    public boolean isBindBankCard() {
        if (getValidaType() >= ConstantUtils.ACCOUNT_AUTH_AND_CARD) {
            return true;
        }
        return false;
    }

    /**
     * 是否开通支付账户
     *
     * @return
     */
    public boolean isOpenAccount() {
        return isBindBankCard();
    }


    /**
     * 验证类型检查
     *
     * @return 验证类型
     */
    public int checkUserInvidaType(BaseFragment fragment) {
        int type = getValidaType();
        Bundle bundle = new Bundle();
        switch (type) {
            case ConstantUtils.ACCOUNT_NOTHING:
                startValidaTypeActivity(fragment, AuthNameFragment.class, bundle);
                break;
            case ConstantUtils.ACCOUNT_NAME_AUTH:
                bundle.putInt(ConstantUtils.KEY_TYPE, BankCardInfoFragment.TYPE_BIND);
                startValidaTypeActivity(fragment, BankCardInfoFragment.class, bundle);
                break;
            case ConstantUtils.ACCOUNT_AUTH_AND_CARD:
                CommonUtils.intentMsgValida(fragment.getActivity(), CommonAuthFragment.TYPE_SET_PAY_PW);
                break;
            case ConstantUtils.ACCOUNT_ALL_VALIDA:
                break;
            default:
        }
        return type;
    }

    public void startValidaTypeActivity(BaseFragment fromFragment, Class toFragment, Bundle bundle) {
        Intent intent = FragmentContainerActivity.getFragmentContainerActivityIntent(fromFragment.getActivity(), toFragment, bundle);
        fromFragment.startActivityForResult(intent, ConstantUtils.INTENT_RESPONSE);
    }

    /**
     * 除了token，部分更新本地user
     *
     * @param sourceUser
     * @param tagetUser
     */
    public User savaUserExcept(User sourceUser, User tagetUser) {
        if (sourceUser != null && tagetUser != null) {
            sourceUser.setMobile(TextUtils.isEmpty(tagetUser.getMobile()) ? sourceUser.getMobile() : tagetUser.getMobile());
            sourceUser.setAvatar(TextUtils.isEmpty(tagetUser.getAvatar()) ? sourceUser.getAvatar() : tagetUser.getAvatar());
            sourceUser.setDz_auth(tagetUser.getDz_auth());
            sourceUser.setValidate_type(tagetUser.getValidate_type());
            sourceUser.setUsername(TextUtils.isEmpty(tagetUser.getUsername()) ? sourceUser.getUsername() : tagetUser.getUsername());
            sourceUser.setRealname(TextUtils.isEmpty(tagetUser.getRealname()) ? sourceUser.getRealname() : tagetUser.getRealname());
        }
        return sourceUser;
    }

}
