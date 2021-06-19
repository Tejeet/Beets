package com.tejeet.beets.ui.discover.RecyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tejeet.beets.R
import com.tejeet.beets.ui.discover.data.modelClass.DataItem
import com.tejeet.beets.ui.discover.data.modelClass.Images
import com.tejeet.beets.utils.Constants

class euroAdapter(val dataItemList: MutableList<DataItem?>) : RecyclerView.Adapter<EuroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EuroViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.trending1_item_layout,parent,false)
        return  EuroViewHolder(view)
    }

    override fun onBindViewHolder(holder: EuroViewHolder, position: Int) {

       Glide.with(holder.IvImage).asGif()
           .load(dataItemList.get(position)?.images?.original?.url)
           .placeholder(Constants.getRandomDrawableColor())
           .into(holder.IvImage)

    }

    override fun getItemCount(): Int {

        return  dataItemList.size

    }
}


class EuroViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){

val IvImage = itemView.findViewById<ImageView>(R.id.recyclerImage1)


}