package com.ukdev.carcadasalborghetti.model

data class Video(
        override val title: String,
        override val length: String,
        override var position: Int,
        override val uri: String
) : Media(title, length, position, uri)