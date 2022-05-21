package com.psq.myjetpck.view

import android.os.Bundle
import android.view.View
import com.psq.arch.base.BaseFragment
import com.psq.myjetpck.R
import com.psq.myjetpck.databinding.FragmentMainBinding
import com.gturedi.views.StatefulLayout
import com.psq.myjetpck.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {


    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.btnGetUsers.setOnClickListener {
            mViewModel.getUsers()
        }
    }

    override fun getContentView(): Int = R.layout.fragment_main
    override fun getStatefulLayout(): StatefulLayout = mDataBinding.statefulLayout
    override fun isVisibleStatefulLayout(): Boolean = true
    override fun onErrorStateClickListener(view: View) {
        mViewModel.getUsers()
    }

}