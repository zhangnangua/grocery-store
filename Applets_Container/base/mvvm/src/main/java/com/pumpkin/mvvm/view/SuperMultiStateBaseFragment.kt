package com.pumpkin.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pumpkin.mvvm.databinding.BaseLayoutBinding
import com.pumpkin.ui.widget.MultiStateView

abstract class SuperMultiStateBaseFragment : BaseFragment() {

    private lateinit var superBinding: BaseLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        superBinding = BaseLayoutBinding.inflate(layoutInflater)
        return superBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }


    open fun setBaseContentForState(
        view: View,
        state: MultiStateView.ViewState,
        switchToState: Boolean = false
    ) {
        val multiplyState = superBinding.root
        multiplyState.addViewByViewState(view, state, switchToState)
    }

    /**
     * set current state
     */
    fun switchState(state: MultiStateView.ViewState) {
        superBinding.root.currentState = state
    }

    /**
     * obtain view by state
     */
    fun getViewByState(state: MultiStateView.ViewState) =
        superBinding.root.obtainView(state)

    /**
     * basic related layout settings.
     */
    fun setPACContentView(v: View) {
        setBaseContentForState(v, MultiStateView.ViewState.CONTENT, true)
    }

    abstract fun initView(view: View)


}