package com.psq.myjetpck.view

import android.os.Bundle
import android.view.View
import com.psq.arch.base.BaseActivity
import com.psq.myjetpck.databinding.ActivityMainBinding
import com.gturedi.views.StatefulLayout
import com.psq.myjetpck.viewmodel.MainActivityModel
import com.psq.myjetpck.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun getContentView(): Int = R.layout.activity_main
    override fun getStatefulLayout(): StatefulLayout? = null
    override fun isVisibleStatefulLayout(): Boolean = true
    override fun onErrorStateClickListener(view: View) {
    }
}