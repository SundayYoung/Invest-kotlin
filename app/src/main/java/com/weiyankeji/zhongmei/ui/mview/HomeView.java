package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.BannerBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.GroupBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NewsBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NoticeBean;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/18.
 */

public interface HomeView extends BaseView {
    /**
     * 加载Banner
     *
     * @param banners
     */
    void loadBanner(List<BannerBean> banners);

    void loadFinish();

    /**
     * 显示理财区域的内容
     *
     * @param bean
     */
    void loadInvest(GroupBean bean);

    /**
     * 加载新闻热点
     *
     * @param itemBeans
     */
    void loadNews(List<NewsBean> itemBeans);

    /**
     * 加载新消息
     *
     * @param bean
     */
    void loadNotice(NoticeBean bean);


//    void loadHome(HomeResponse response);

    //add v1.1
    void showDialog(boolean isShow);

}
