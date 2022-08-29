package com.fansmall.widget.refresh

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.fansmall.widget.R
import com.fansmall.widget.databinding.CustomHeaderLayoutBinding
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 * Date 2022/1/11 3:32 下午
 *
 *
 * 2022年01月17日：Core中添加默认背景色为白色。
 *
 * 2022年01月18日：添加黑白模式切换
 *
 * 示例：refreshL.setRefreshHeader(CustomHeader(context, WhiteHeader /**这里设置动画模式（黑或白）*/ ).also { it.setBackgroundColor(Color.TRANSPARENT) /**这里设置header背景色*/ })
 *
 * @author Tson
 */
@SuppressLint("RestrictedApi")
class CustomHeader : RelativeLayout, RefreshHeader {
    private var headerMode: HeaderColorMode = BlackHeader
    private val itemBaseView by lazy {
        inflate(context, R.layout.custom_header_layout, null).also {
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private lateinit var  binding:CustomHeaderLayoutBinding



    private fun initGif() {
        // 使用两个view隐藏显示，会比重新setAnimation("xxx.json")更流畅，实际体验：手机性能越差，对比越明显
         if (headerMode == BlackHeader) {
             binding.whiteLa.gone()
             binding.blackLa.visible()
         } else {
             binding.whiteLa.visible()
             binding.blackLa.gone()
         }
    }

    private fun autoHide(show: Boolean) {
        if (headerMode == BlackHeader) {
            binding.blackLa.visibility = if (show) View.VISIBLE else View.GONE
        } else {
            binding.whiteLa.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, mode: HeaderColorMode) : this(context, null) {
        headerMode = mode
        addView(itemBaseView)
        binding = CustomHeaderLayoutBinding.bind(itemBaseView)
        binding.firstIv.setImageResource(if (headerMode == BlackHeader) R.drawable.loading_first else R.drawable.loading_first_white)
        initGif()
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getView(): View = this
    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate
    override fun setPrimaryColors(vararg colors: Int) {}
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {}
    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {}
    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {}
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {}
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int = 0
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    override fun isSupportHorizontalDrag(): Boolean = false
    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.None -> binding.firstIv.gone().run { autoHide(false) }
            RefreshState.PullDownToRefresh -> binding.firstIv.visible().run { autoHide(false) }
            RefreshState.PullDownCanceled -> binding.firstIv.visible().run { autoHide(false) }
            RefreshState.ReleaseToRefresh -> binding.firstIv.visible().run { autoHide(false) }
            RefreshState.RefreshReleased -> binding.firstIv.gone().run { autoHide(true) }
            RefreshState.Refreshing -> binding.firstIv.gone().run { autoHide(true) }
            RefreshState.RefreshFinish -> binding.firstIv.gone().run { autoHide(true) }
            else -> {}
        }
    }
}
fun View?.gone() { this?.visibility = View.GONE }
fun View?.visible() { this?.visibility = View.VISIBLE }
sealed class HeaderColorMode
object BlackHeader : HeaderColorMode()
object WhiteHeader : HeaderColorMode()