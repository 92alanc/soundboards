package com.ukdev.carcadasalborghetti.handlers

import com.ukdev.carcadasalborghetti.helpers.FileHelper
import com.ukdev.carcadasalborghetti.helpers.MediaHelper
import com.ukdev.carcadasalborghetti.model.Media
import com.ukdev.carcadasalborghetti.utils.CrashReportManager
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.InputStream

class AudioHandlerTest {

    @MockK lateinit var mockMediaHelper: MediaHelper
    @MockK lateinit var mockCrashReportManager: CrashReportManager
    @MockK lateinit var mockFileHelper: FileHelper

    private lateinit var audioHandler: AudioHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        audioHandler = AudioHandler(mockMediaHelper, mockCrashReportManager, mockFileHelper)
    }

    @Test
    fun shouldPlayAudio() = runBlocking {
        audioHandler.play(Media("Title", mockk()))

        verify(exactly = 0) { mockCrashReportManager.logException(any()) }
    }

    @Test
    fun whenAnExceptionIsThrownWhilePlayingAudio_shouldLogToCrashReport() = runBlocking {
        every { mockMediaHelper.playAudio(any()) } throws Throwable()

        audioHandler.play(mockk())

        verify { mockCrashReportManager.logException(any()) }
    }

    @Test
    fun shouldShareAudio() = runBlocking {
        coEvery { mockFileHelper.getByteStream(any()) } returns mockk()

        audioHandler.share(Media("1", mockk()))

        coVerify { mockFileHelper.shareFile(any<InputStream>(), any()) }
    }

    @Test
    fun whenAnExceptionIsThrownWhileSharingAudio_shouldLogToCrashReport() = runBlocking {
        coEvery { mockFileHelper.getByteStream(any()) } throws Throwable()

        audioHandler.share(mockk())

        verify { mockCrashReportManager.logException(any()) }
    }

}