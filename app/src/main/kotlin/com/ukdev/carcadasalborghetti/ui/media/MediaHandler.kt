package com.ukdev.carcadasalborghetti.ui.media

import androidx.lifecycle.LiveData
import com.ukdev.carcadasalborghetti.data.tools.CrashReportManager
import com.ukdev.carcadasalborghetti.domain.entities.Media
import com.ukdev.carcadasalborghetti.framework.tools.FileHelper

abstract class MediaHandler(
        protected val mediaHelper: MediaHelper,
        protected val crashReportManager: CrashReportManager,
        protected val fileHelper: FileHelper
) {

    abstract suspend fun play(media: Media)

    abstract suspend fun share(media: Media)

    fun stop() {
        mediaHelper.stop()
    }

    fun isPlaying(): LiveData<Boolean> {
        return mediaHelper.isPlaying()
    }

}