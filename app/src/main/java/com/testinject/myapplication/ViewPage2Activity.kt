package com.testinject.myapplication

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.activity_view_page2.*


class ViewPage2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_page2)
        viewPage2.adapter=ViewPagerAdapter()
        viewPage2Fragment.adapter=ViewPageFragmentAdapter(this)
    }

   private class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

       val colors:ArrayList<Int> = ArrayList()
       init {
           colors.add(android.R.color.white);
           colors.add(android.R.color.holo_purple);
           colors.add(android.R.color.holo_blue_dark);
           colors.add(android.R.color.holo_green_light);
       }


       internal class ViewPagerViewHolder(itemView: View) : ViewHolder(itemView) {
            var mTvTitle: TextView
            var mContainer: ConstraintLayout

            init {
                mContainer = itemView.findViewById(R.id.clContainer)
                mTvTitle = itemView.findViewById(R.id.tvTitle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            return ViewPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager,parent,false))
        }

        override fun getItemCount(): Int {
            return colors.size
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            holder.mTvTitle.setText("item$position")
            holder.mContainer.setBackgroundResource(colors[position])
        }
    }

    private class ViewPageFragmentAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

        val colors:ArrayList<Int> = ArrayList()
        init {
            colors.add(android.R.color.white);
            colors.add(android.R.color.holo_purple);
            colors.add(android.R.color.holo_blue_dark);
            colors.add(android.R.color.holo_green_light);
        }


        override fun getItemCount(): Int {
            return colors.size
        }

        override fun createFragment(position: Int): Fragment {
            return ViewPageFragment().newInstance(colors,position)
        }

    }

}