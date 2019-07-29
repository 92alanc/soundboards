package com.ukdev.carcadasalborghetti.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ukdev.carcadasalborghetti.R
import com.ukdev.carcadasalborghetti.adapter.MediaAdapter
import com.ukdev.carcadasalborghetti.adapter.VideoAdapter
import com.ukdev.carcadasalborghetti.handlers.VideoHandler
import com.ukdev.carcadasalborghetti.model.MediaType
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class VideoFragment : MediaListFragment(MediaType.VIDEO, ITEM_SPAN_PORTRAIT, ITEM_SPAN_LANDSCAPE) {

    override val mediaHandler by inject<VideoHandler>{ parametersOf(this) }
    override val adapter: MediaAdapter = VideoAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_list, container, false)
    }

    override fun onStartPlayback() {

    }

    override fun onStopPlayback() {

    }

    companion object {
        private const val ITEM_SPAN_PORTRAIT = 2
        private const val ITEM_SPAN_LANDSCAPE = 3
    }

}