package com.weiyankeji.zhongmei.ui.mmodel.home;

import com.weiyankeji.zhongmei.ui.mmodel.bean.BannerBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.GroupBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NewsBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NoticeBean;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/24.
 */

public class HomeResponse {
    public NoticeBean notice;
    public List<BannerBean> banners;
    public List<NewsBean> news;
    public List<GroupBean> group;
    public int is_first_login; //是否为首次登陆. 0:否, 1:是
}
