package com.example.mvisamplecoroutines.ui.activity.wallpaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvisamplecoroutines.databinding.ItemImageBackgroundBinding
import kotlinx.android.synthetic.main.item_image_background.view.*


class ImageBackgroundDiffUtil : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return true
    }
}

class ImageBackgroundAdapter :
    ListAdapter<Any, RecyclerView.ViewHolder>(ImageBackgroundDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemImageBackgroundBinding.inflate(LayoutInflater.from(parent.context))
        return ItemImageBackground(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemImageBackground)?.bind(position)
    }

    class ItemImageBackground(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.textView.text = position.toString()
        }
    }
}