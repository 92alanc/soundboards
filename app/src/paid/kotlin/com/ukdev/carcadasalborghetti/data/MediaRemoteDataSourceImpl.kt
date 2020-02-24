package com.ukdev.carcadasalborghetti.data

import com.ukdev.carcadasalborghetti.api.DIR_AUDIO
import com.ukdev.carcadasalborghetti.api.DIR_VIDEO
import com.ukdev.carcadasalborghetti.api.requests.MediaRequest
import com.ukdev.carcadasalborghetti.api.tools.ApiProvider
import com.ukdev.carcadasalborghetti.model.Media
import com.ukdev.carcadasalborghetti.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class MediaRemoteDataSourceImpl(private val apiProvider: ApiProvider) : MediaRemoteDataSource {

    override suspend fun listMedia(mediaType: MediaType): List<Media> {
        val dir = if (mediaType == MediaType.AUDIO)
            DIR_AUDIO
        else
            DIR_VIDEO
        val request = MediaRequest(dir)
        val api = apiProvider.getDropboxService()

        return withContext(Dispatchers.IO) {
            api.listMedia(request).entries
        }
    }

    override suspend fun getStreamLink(mediaId: String): String {
        val request = MediaRequest(mediaId)
        val api = apiProvider.getDropboxService()

        return withContext(Dispatchers.IO) {
            api.getStreamLink(request).link
        }
    }

    override suspend fun download(mediaId: String): InputStream {
        val request = MediaRequest(mediaId)
        val api = apiProvider.getDownloadService()

        return withContext(Dispatchers.IO) {
            api.download(request).byteStream()
        }
    }

}