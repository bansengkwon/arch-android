package com.psq.myjetpck.view

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.psq.arch.base.BaseFragment
import com.psq.myjetpck.R
import com.psq.myjetpck.databinding.FragmentMainBinding
import com.gturedi.views.StatefulLayout
import com.psq.arch.adapter.ArchSingleAdapter
import com.psq.myjetpck.BR
import com.psq.myjetpck.model.Item
import com.psq.myjetpck.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    lateinit var adapter: ArchSingleAdapter<Item>

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mDataBinding.btnGetData.setOnClickListener {
            mViewModel.getUsers()
        }
        adapter = ArchSingleAdapter(
            requireContext(),
            mutableListOf(),
            R.layout.item_test_layout,
            BR.model,
            { _, i, item ->
                showToast("点击了${i},id = ${item.id}")
            }
        )
        mDataBinding.rvItems.adapter = adapter

        mViewModel.data.observe(viewLifecycleOwner) {
            adapter.updateData(it.items)
        }
    }


    override fun getContentView(): Int = R.layout.fragment_main
    override fun getStatefulLayout(): StatefulLayout = mDataBinding.statefulLayout
    override fun onErrorStateClickListener(view: View) {
        mViewModel.getUsers()
    }
}