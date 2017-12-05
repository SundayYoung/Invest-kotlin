package com.weiyankeji.zhongmei.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.ListView

import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout
import com.weiyankeji.library.utils.ExtendUtil
import com.weiyankeji.library.utils.ImageLoaderUtil
import com.weiyankeji.library.utils.ToastUtil
import com.weiyankeji.zhongmei.R
import com.weiyankeji.zhongmei.ui.adapter.InvestHomeAdapter
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeSkuList
import com.weiyankeji.zhongmei.ui.mpresenter.InvestHomePresenter
import com.weiyankeji.zhongmei.ui.mview.InvestHomeView
import com.weiyankeji.zhongmei.utils.CommonUtils
import com.weiyankeji.zhongmei.utils.ConstantUtils
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils
import com.weiyankeji.zhongmei.utils.StatisticalUtils

import butterknife.BindView
import kotlinx.android.synthetic.main.fragment_invest.*

/**
 * Created by liuhaiyang on 2017/8/21.
 */

class InvestFragment : BaseMvpFragment<InvestHomeView, InvestHomePresenter>(), InvestHomeView, InvestHomeAdapter.OnItemButtonClickListener, OnRefreshListener {

//    @BindView(R.id.stl_invest)
//    internal var mSwipeToLoadLayout: SwipeToLoadLayout? = null

//    @BindView(R.id.swipe_target)
//    internal var mElvList: ExpandableListView? = null

    private var mIvBrannerImg: ImageView? = null

    private var mIsFirst = true
    private var mAdapter: InvestHomeAdapter? = null

    private var mHeiht: Float = 0.toFloat()

    //是否加载过数据
    private var mAlreadyLoad: Boolean = false

    /**
     * expand list y value
     *
     * @return
     */
    val scrollY: Int
        get() {
            val c = swipe_target!!.getChildAt(0) ?: return 0
            val firstVisiblePosition = swipe_target!!.firstVisiblePosition
            val top = c.top
            return -top + firstVisiblePosition * c.height
        }

    override fun setContentLayout(): Int {
        return R.layout.fragment_invest
    }

    override fun finishCreateView(bundle: Bundle?) {
        setTitle(R.string.bottomview_main_invest)
        stl_invest!!.setOnRefreshListener(this)
        stl_invest!!.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET)

        swipe_target!!.setGroupIndicator(null)
        swipe_target!!.setOnGroupClickListener { _, _, groupPosition, _ ->
            val group = mAdapter!!.getGroup(groupPosition)
            val parm = Bundle()
            parm.putString(ConstantUtils.KEY_TITLE, group!!.title)
            parm.putInt(ConstantUtils.KEY_TYPE, group.type)
            startFragment(InvestListFragment::class.java, parm)
            true
        }

        swipe_target!!.setOnChildClickListener { _, _, groupPos, childPos, _ ->
            val child = mAdapter!!.getChild(groupPos, childPos)
            val parm = Bundle()
            parm.putString(ConstantUtils.KEY_ID, child!!.sku_id)
            startFragment(InvestProductDetailFragment::class.java, parm)
            statistical(child.sku_type, childPos + 1)
            false
        }

        mAdapter = InvestHomeAdapter(mContext, this)
        swipe_target!!.setAdapter(mAdapter)

        errorPageView.setOnErrorFunctionClickListener { mPresenter.loadData(true) }

        mHeiht = CommonUtils.getHeighScale(mContext)

        showContentView(false)

    }

    fun statistical(type: Int, index: Int) {
        if (type == ConstantUtils.INVEST_SKU_TYPE_STIPULATE) {
            StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_INVEST_REGULAR, index)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && mPresenter != null && mIsFirst) {
            mPresenter.loadData(true)
            mIsFirst = false
        }
    }

    override fun initPresenter(): InvestHomePresenter {
        return InvestHomePresenter()
    }

    override fun showLoading() {
        setLoading(true)
    }

    override fun hideLoading() {
        setLoading(false)
        if (stl_invest!!.isRefreshing) {
            stl_invest!!.isRefreshing = false
        }
    }

    override fun setData(response: InvestHomeResponse) {
        showContentView(true)
        mAlreadyLoad = true
        if (!ExtendUtil.listIsNullOrEmpty(response.group)) {
            mAdapter!!.updateAdapter(response.group!!)
            expandAllGroup()
        }
        setBanner(response)
    }

    override fun netError(code: Int, msg: String) {
        if (!mAlreadyLoad) {
            if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
                ToastUtil.showShortToast(mContext.applicationContext, msg)
            }
        } else {
            if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
                ToastUtil.showShortToast(mContext.applicationContext, msg)
            }
        }

    }

    private fun expandAllGroup() {
        var i = 0
        val size = mAdapter!!.groupCount
        while (i < size) {
            swipe_target!!.expandGroup(i)
            i++
        }
    }

    private fun setBanner(response: InvestHomeResponse?) {
        if (response == null || ExtendUtil.listIsNullOrEmpty(response.banners)) {
            return
        }
        if (mIvBrannerImg == null) {
            mIvBrannerImg = ImageView(mContext)
        }
        mIvBrannerImg!!.scaleType = ImageView.ScaleType.CENTER_CROP

        ImageLoaderUtil.loadImageFromUrl(mContext, response.banners!![0].img_url, mIvBrannerImg, R.drawable.invest_banner_default)
        val params = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeiht.toInt())
        mIvBrannerImg!!.layoutParams = params
        if (swipe_target!!.headerViewsCount == 0) {
            swipe_target!!.addHeaderView(mIvBrannerImg)
        }

        swipe_target!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(absListView: AbsListView, i: Int) {}

            override fun onScroll(absListView: AbsListView, i: Int, i1: Int, i2: Int) {
                mIvBrannerImg!!.scrollTo(0, (-scrollY * 0.5).toInt())
            }
        })
    }

    override fun onItemButtonClick(itemList: InvestDetailBean) {
        val param = Bundle()
        param.putString(ConstantUtils.KEY_ID, itemList.sku_id)
        startFragment(InvestProductDetailFragment::class.java, param)
    }

    override fun onRefresh() {
        mPresenter.loadData(false)
    }
}
