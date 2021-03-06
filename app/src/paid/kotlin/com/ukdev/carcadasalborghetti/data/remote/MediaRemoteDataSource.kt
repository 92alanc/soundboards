package com.ukdev.carcadasalborghetti.data.remote

import com.ukdev.carcadasalborghetti.domain.entities.Media
import com.ukdev.carcadasalborghetti.domain.entities.MediaType
import java.io.InputStream

interface MediaRemoteDataSource {
    suspend fun listMedia(mediaType: MediaType): List<Media>
    suspend fun download(mediaId: String): InputStream
}