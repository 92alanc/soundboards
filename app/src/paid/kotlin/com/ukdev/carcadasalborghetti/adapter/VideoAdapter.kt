package com.ukdev.carcadasalborghetti.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ukdev.carcadasalborghetti.R
import com.ukdev.carcadasalborghetti.model.Video

class VideoAdapter : MediaAdapter<Video>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder<Video> {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(itemView)
    }

}
