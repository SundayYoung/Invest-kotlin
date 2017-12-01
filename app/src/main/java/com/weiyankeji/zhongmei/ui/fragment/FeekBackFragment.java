package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;

import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.payment.FeedBackRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.FeekBackPresenter;
import com.weiyankeji.zhongmei.ui.mview.FeekBackView;


import butterknife.BindView;

/**
 * 用户反馈界面
 *
 * @aythor: zhangfangfei
 */

public class FeekBackFragment extends BaseMvpFragment<FeekBackView, FeekBackPresenter> implements FeekBackView {
    private static final float SCALE_SIZE = 2.4f;
    private static final int NUMBER = 250;
    private TextWatcher mTextWatcher;

    @BindView(R.id.edit_feek)
    EditText mEtFeek;
    @BindView(R.id.et_feek_back_phone_number)
    EditText mEtFeekPhoneNumber;
    @BindView(R.id.tv_feek_back)
    TextView mTvFeek;
    @BindView(R.id.bt_feek_back_commit)
    Button mBtFeek;


    @Override
    public int setContentLayout() {
        return R.layout.fragment_feekback;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(R.string.feek_back_title);
        initEtSize();
        addListener();
    }

    @Override
    public FeekBackPresenter initPresenter() {
        return new FeekBackPresenter();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {

        setLoading(false);
    }

    @Override
    public void showCommitSuccess(String msg) {
        ToastUtil.showShortToast(getActivity().getApplicationContext(), R.string.feek_back_successful);
        getActivity().onBackPressed();

    }

    @Override
    public void showCommitFail(String msg) {
        ToastUtil.showShortToast(getActivity().getApplicationContext(), msg);
        showBtnEnableState(true);
    }

    private void addListener( ) {
        addBackListener();
        addEtListener();
    }

    private void addEtListener() {
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                int length = s.length();
                mTvFeek.setText(NUMBER - length + "/" + String.valueOf(NUMBER));

                if (!TextUtils.isEmpty(mEtFeek.getText())) {
                    showBtnEnableState(true);
                } else {
                    showBtnEnableState(false);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mEtFeek.addTextChangedListener(mTextWatcher);
        mBtFeek.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           showBtnEnableState(false);
                                           loadData();
                                       }
                                   }
        );
    }

    /**
     * 根据View宽度设置高度
     */
    private void initEtSize() {
        int width = mEtFeek.getMeasuredWidth();
        mEtFeek.setHeight((int) (width / SCALE_SIZE));
    }

    private void loadData() {
        String content = mEtFeek.getText().toString().trim();
        String contact = mEtFeekPhoneNumber.getText().toString().trim();
        mPresenter.loadData(new FeedBackRequest(content, contact));
    }

    private void showBtnEnableState(boolean enable) {
        if (enable) {
            mBtFeek.setEnabled(true);
            mBtFeek.setBackgroundResource(R.drawable.gradient_button_bg);
            mBtFeek.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            mBtFeek.setEnabled(false);
            mBtFeek.setBackgroundResource(R.drawable.shape_round_button_bg_gray_e1);
            mBtFeek.setTextColor(ContextCompat.getColor(mContext, R.color.gray_c3));
        }
    }


}