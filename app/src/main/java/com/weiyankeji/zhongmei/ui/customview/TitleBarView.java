package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.utils.TextLengthFilter;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MenuBean;

public class TitleBarView {

    private Context mContext;

    private RelativeLayout mBackground;
    private LinearLayout mLlBack;
    private LinearLayout mBarLayout;
    private TextView mNameText;
    private ImageView mBackImage;

    private OnItemClickBarListener mOnItemClick;
    private OnItemLongClickBarListener mOnItemLongClick;

    private View mView;

    public TitleBarView(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_title_bar, null);
        mBackground = mView.findViewById(R.id.title_background);
        mNameText = mView.findViewById(R.id.title_name);
        mBackImage = mView.findViewById(R.id.title_back_image);

        mLlBack = mView.findViewById(R.id.title_back_layout);
        mBarLayout = mView.findViewById(R.id.title_bar);
    }

    public View getTitleBarView() {
        return mView;
    }

    /**
     * 设置标题
     *
     * @param name
     */
    public void setTitle(CharSequence name) {
        if (!TextUtils.isEmpty(name)) {
            if (TextLengthFilter.getLength(name.toString()) > 26) {
                mNameText.setTextSize(16);
            }
            mNameText.setText(name);
        }
    }

    /**
     * 设置标题字体颜色
     *
     * @param resid
     */
    public void setTitleColor(int resid) {
        mNameText.setTextColor(mContext.getResources().getColor(resid));
    }

    /**
     * 设置返回按钮的图片
     *
     * @param resid
     */
    public void setBackImage(int resid) {
        mLlBack.setVisibility(View.VISIBLE);
        mBackImage.setImageResource(resid);
    }

    /**
     * 添加返回事件
     */
    public void setOnBackListener(View.OnClickListener listener) {
        mLlBack.setVisibility(View.VISIBLE);
        mLlBack.setBackgroundResource(R.drawable.item_background_borderless);
        mLlBack.setOnClickListener(listener);
    }

    public void setBackgroundResource(int resid) {
        mBackground.setBackgroundResource(resid);
    }


    public RelativeLayout getBackgroupLayout() {
        return mBackground;
    }

    public LinearLayout getBarLayout() {
        return mBarLayout;
    }


    /**
     * 添加右边按钮
     *
     * @param onItemClick
     * @param beans
     */
    public void addBar(OnItemClickBarListener onItemClick, MenuBean... beans) {
        addBar(beans);
        setOnItemClickBarListener(onItemClick);
    }

    /**
     * 添加右边按钮
     *
     * @param beans
     */
    public void addBar(MenuBean... beans) {
        mBarLayout.removeAllViews();
        for (int i = 0; i < beans.length; i++) {
            beans[i].setId(i);
            mBarLayout.addView(addBar(beans[i]));
        }
    }

    /**
     * 清除右边按钮
     */
    public void removeBar() {
        mBarLayout.removeAllViews();
    }

    /**
     * 改变右边按钮
     *
     * @param beans
     */
    public void changeBar(MenuBean... beans) {
        for (int i = 0; i < mBarLayout.getChildCount(); i++) {
            View view = mBarLayout.getChildAt(i);
            TextView name = (TextView) view.findViewById(R.id.bar_name);
            if (beans[i].getColorId() != 0) {
                name.setTextColor(mContext.getResources().getColor(beans[i].getColorId()));
            }
            name.setText(beans[i].getTitle());
        }
    }

    /**
     * 菜单显示
     */
    public void setBarVisable(boolean isVisable) {
        if (isVisable) {
            mBarLayout.setVisibility(View.VISIBLE);
        } else {
            mBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示菜单
     */
    public boolean getBarVisable() {
        if (mBarLayout.getVisibility() == View.GONE || mBarLayout.getVisibility() == View.INVISIBLE) {
            return false;
        }
        return true;
    }

    public MenuBean getMenuBean() {
        return new MenuBean();
    }

    private View addBar(final MenuBean bean) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_title_bar, null);

        ImageView icon = (ImageView) view.findViewById(R.id.bar_icon);
        final TextView name = (TextView) view.findViewById(R.id.bar_name);

        if (bean.getIcon() != -1) {
            icon.setImageResource(bean.getIcon());
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }

        if (bean.getColorId() != 0) {
            name.setTextColor(mContext.getResources().getColor(bean.getColorId()));
        }

        if (!TextUtils.isEmpty(bean.getTitle())) {
            name.setText(bean.getTitle());
            name.setVisibility(View.VISIBLE);
        }


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onClick(v, bean.getId());
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClick != null) {
                    mOnItemLongClick.onLongClick(v, bean.getId());
                }
                return false;
            }
        });
        return view;
    }

    public void setOnItemClickBarListener(OnItemClickBarListener onItemClick) {
//		mLlRefresh.setVisibility(View.GONE);
        this.mOnItemClick = onItemClick;
    }

    public void setOnItemLongClickBarListener(OnItemLongClickBarListener onItemLongClick) {
        this.mOnItemLongClick = onItemLongClick;
    }


    public interface OnItemClickBarListener {
        void onClick(View v, int postion);
    }

    public interface OnItemLongClickBarListener {
        void onLongClick(View v, int postion);
    }


}
