package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse;
import com.weiyankeji.zhongmei.ui.mview.InvestDetailView;


/**
 * 投资 项目详情
 * Created by liuhaiyang on 2017/8/28.
 */

public class InvestDetailPresenter extends BasePresenter<InvestDetailView> {

    public void loadData(InvestWithIdRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.INVEST_DETAIL, request);
        mCall.enqueue(new RestBaseCallBack<InvestDetailResponse>() {
            @Override
            public void onResponse(InvestDetailResponse response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }

                mView.setData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.netError(code, msg);
                }
            }
        });
    }

    /**
     * 获取募集剩余时间
     */
    public String getLastTime(long lastTime) {
        String strTime;
        if (lastTime <= 0) {
            strTime = ZMApplication.getZMContext().getString(R.string.invest_home_collect_over);
        } else if (lastTime < 60) { //小于 1 分钟
            strTime = ZMApplication.getZMContext().getString(R.string.invest_detail_one_minute);
        } else if (lastTime < 60 * 60) { // 小于 1 小时
            strTime = String.format(ZMApplication.getZMContext().getString(R.string.invest_detail_format_minute), lastTime / 60);
        } else if (lastTime < 24 * 60 * 60) { // 小于一天
            strTime = String.format(ZMApplication.getZMContext().getString(R.string.invest_detail_format_hour_minute), lastTime / (60 * 60), lastTime % (60 * 60) / 60);
        } else {
            strTime = String.format(ZMApplication.getZMContext().getString(R.string.invest_detail_format_day_hour), lastTime / (24 * 60 * 60), lastTime % (24 * 60 * 60) / (60 * 60));
        }
        return strTime;
    }


    /**
     * 是否大于一天
     *
     * @param lastTime
     * @return
     */
    public boolean isLastTimeMoreThanADay(long lastTime) {
        return lastTime >= 24 * 60 * 60;
    }

    /**
     * 获取日期时间串
     *
     * @param lastTime
     * @return
     */
    public String getDayText(long lastTime) {
        return timeBuidler(getDay(lastTime));
    }

    /**
     * 获取剩余分钟
     *
     * @param lastTime
     * @return
     */
    public String getHourText(long lastTime) {
        long hour = getHour(lastTime);
        return timeBuidler(hour);
    }


    /**
     * 获取剩余分钟
     *
     * @param lastTime
     * @return
     */
    public String getMinuteText(long lastTime) {
        long minute = getMinute(lastTime);
        if (getHour(lastTime) == 0 && minute == 0) {
            minute = 1;
        }
        return timeBuidler(minute);
    }


    // private
    private String timeBuidler(long time) {
        StringBuilder sb = new StringBuilder();
        if (time < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(time));
        return sb.toString();
    }

    private long getDay(long lastTime) {
        return lastTime / (24 * 60 * 60);
    }

    private long getHour(long lastTime) {
        return (lastTime - (getDay(lastTime) * 24 * 60 * 60)) / (60 * 60);
    }

    private long getMinute(long lastTime) {
        long day = getDay(lastTime);
        long hour = getHour(lastTime);
        return (lastTime - day * 24 * 60 * 60 - hour * 60 * 60) / 60;
    }

}
