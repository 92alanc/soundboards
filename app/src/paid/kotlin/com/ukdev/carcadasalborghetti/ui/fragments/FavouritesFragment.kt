package com.ukdev.carcadasalborghetti.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.ukdev.carcadasalborghetti.R
import com.ukdev.carcadasalborghetti.domain.entities.MediaType
import com.ukdev.carcadasalborghetti.ui.adapter.FavouritesAdapter
import com.ukdev.carcadasalborghetti.ui.adapter.MediaAdapter
import com.ukdev.carcadasalborghetti.ui.media.FavouritesHandler
import kotlinx.android.synthetic.main.fragment_audio.*
import org.koin.android.ext.android.inject

class FavouritesFragment : MediaListFragment(R.layout.fragment_audio, MediaType.BOTH) {

    override val mediaHandler by inject<FavouritesHandler>()
    override val adapter: MediaAdapter = FavouritesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener { mediaHandler.stop() }
    }

    override fun showFab() {
        fab.isVisible = true
    }

    override fun hideFab() {
        fab.isVisible = false
    }

}