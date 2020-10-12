package com.testinject.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.item_viewpager.*
import kotlinx.android.synthetic.main.item_viewpager.view.*

/**
 * Project Name: My Application
 * File Name:    ViewPageFragment.java
 * ClassName:    ViewPageFragment
 *
 * Description: viewpager 碎片.
 *
 * @author  yh.liu
 * @date    2020年10月12日 17:58
 *
 * Copyright (c) 2020年, 4399 Network CO.ltd. All Rights Reserved.
 */
class ViewPageFragment : Fragment() {

    companion object {
        const val COLORS = "colors"
        const val POSITION = "position"
    }

    private var mColors: List<Int>? = null
    private var mPosition = 0

    public fun newInstance(colors: List<Int>, position: Int): ViewPageFragment {
        val args = Bundle()
        args.putSerializable(COLORS, colors as ArrayList<Int?>)
        args.putInt(POSITION, position)
        val fragment = ViewPageFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColors = arguments!!.getSerializable(COLORS) as List<Int>?
            mPosition = arguments!!.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_viewpager, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clContainer.setBackgroundResource(mColors!![mPosition])
        tvTitle.setText("item_fragment$mPosition")
    }
}