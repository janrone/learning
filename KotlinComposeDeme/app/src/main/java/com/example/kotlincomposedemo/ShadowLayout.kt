package com.example.kotlincomposedemo

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.compose.ui.platform.ComposeView


class ShadowLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attributeSet, defStyleAttr) {
    init {
        background = resources.getDrawable(R.mipmap.icon_shadow_bg)
        setPadding(14, 14, 14, 14)
        val frameLayout = ComposeView(context)
        addView(frameLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    // 获取 FrameLayout 容器
    fun getComposeContainer(): ComposeView {
        return getChildAt(0) as ComposeView
    }
}