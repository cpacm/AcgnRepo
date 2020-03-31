package com.cpacm.libarch.ui.widgets

import com.google.android.material.appbar.AppBarLayout

/**
 *
 *
 * 用于监听收缩栏的收缩状态
 *
 * @author cpacm 2018/2/9
 */

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    private var mCurrentState = State.IDLE

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        val offset = Math.abs(i) * 1.0f / appBarLayout.totalScrollRange
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, offset)
            }
            mCurrentState = State.EXPANDED
        } else if (Math.abs(i) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, offset)
            }
            mCurrentState = State.COLLAPSED
        } else {
            onStateChanged(appBarLayout, State.IDLE, offset)
            mCurrentState = State.IDLE
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State, offset: Float)
}
