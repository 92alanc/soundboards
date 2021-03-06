package com.ukdev.carcadasalborghetti.data.repository

import com.google.common.truth.Truth.assertThat
import com.ukdev.carcadasalborghetti.data.entities.GenericError
import com.ukdev.carcadasalborghetti.data.entities.NetworkError
import com.ukdev.carcadasalborghetti.data.entities.Success
import com.ukdev.carcadasalborghetti.data.local.MediaLocalDataSource
import com.ukdev.carcadasalborghetti.data.remote.MediaRemoteDataSource
import com.ukdev.carcadasalborghetti.data.tools.CrashReportManager
import com.ukdev.carcadasalborghetti.domain.entities.Media
import com.ukdev.carcadasalborghetti.domain.entities.MediaType
import com.ukdev.carcadasalborghetti.domain.entities.Operation
import com.ukdev.carcadasalborghetti.framework.local.db.FavouritesDatabase
import com.ukdev.carcadasalborghetti.framework.remote.api.tools.IOHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class MediaRepositoryImplTest {

    @MockK lateinit var mockCrashReportManager: CrashReportManager
    @MockK lateinit var mockRemoteDataSource: MediaRemoteDataSource
    @MockK lateinit var mockLocalDataSource: MediaLocalDataSource
    @MockK lateinit var mockFavouritesDatabase: FavouritesDatabase

    private lateinit var repository: MediaRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        repository = MediaRepositoryImpl(
                mockCrashReportManager,
                mockRemoteDataSource,
                mockLocalDataSource,
                mockFavouritesDatabase,
                IOHelper(mockCrashReportManager)
        )
    }

    @Test
    fun shouldGetMediaList() = runBlocking {
        val expected = listOf(
                Media("1", "media 1", MediaType.AUDIO),
                Media("2", "media 2", MediaType.AUDIO),
                Media("3", "media 3", MediaType.AUDIO)
        )
        coEvery { mockRemoteDataSource.listMedia(any()) } returns expected

        val result = repository.getMedia(MediaType.AUDIO)

        assertThat(result).isInstanceOf(Success::class.java)
        require(result is Success)
        assertThat(result.body).isEqualTo(expected)
    }

    @Test
    fun whenAHttpExceptionIsThrown_shouldLogToCrashReport() = runBlocking {
        coEvery {
            mockRemoteDataSource.listMedia(any())
        } throws HttpException(mockk(relaxed = true))

        repository.getMedia(MediaType.AUDIO)

        verify { mockCrashReportManager.logException(any<HttpException>()) }
    }

    @Test
    fun whenAHttpExceptionIsThrown_shouldReturnGenericError() = runBlocking {
        coEvery {
            mockRemoteDataSource.listMedia(any())
        } throws HttpException(mockk(relaxed = true))
        coEvery { mockLocalDataSource.listMedia(any()) } throws Throwable()

        val result = repository.getMedia(MediaType.AUDIO)

        assertThat(result).isInstanceOf(GenericError::class.java)
    }

    @Test
    fun whenAnIOExceptionIsThrown_shouldLogToCrashReport() = runBlocking {
        coEvery { mockRemoteDataSource.listMedia(any()) } throws IOException()

        repository.getMedia(MediaType.AUDIO)

        verify { mockCrashReportManager.logException(any<IOException>()) }
    }

    @Test
    fun whenAnIOExceptionIsThrown_shouldReturnNetworkError() = runBlocking {
        coEvery { mockRemoteDataSource.listMedia(any()) } throws IOException()
        coEvery { mockLocalDataSource.listMedia(any()) } throws Throwable()

        val result = repository.getMedia(MediaType.AUDIO)

        assertThat(result).isInstanceOf(NetworkError::class.java)
    }

    @Test
    fun whenARandomExceptionIsThrown_shouldLogToCrashReport() = runBlocking {
        coEvery { mockRemoteDataSource.listMedia(any()) } throws Throwable()

        repository.getMedia(MediaType.AUDIO)

        verify { mockCrashReportManager.logException(any()) }
    }

    @Test
    fun whenARandomExceptionIsThrown_shouldReturnGenericError() = runBlocking {
        coEvery { mockRemoteDataSource.listMedia(any()) } throws Throwable()
        coEvery { mockLocalDataSource.listMedia(any()) } throws Throwable()

        val result = repository.getMedia(MediaType.AUDIO)

        assertThat(result).isInstanceOf(GenericError::class.java)
    }

    @Test
    fun whenRemoteDataSourceThrowsAnException_shouldGetMediaListFromCache() = runBlocking {
        val expected = listOf(
                Media("1", "media 1", MediaType.AUDIO),
                Media("2", "media 2", MediaType.AUDIO),
                Media("3", "media 3", MediaType.AUDIO)
        )
        coEvery { mockRemoteDataSource.listMedia(any()) } throws IOException()
        coEvery { mockLocalDataSource.listMedia(any()) } returns expected

        repository.getMedia(MediaType.AUDIO)

        coVerify { mockRemoteDataSource.listMedia(any()) }
        coVerify { mockLocalDataSource.listMedia(any()) }
    }

    @Test
    fun shouldSaveMediaToFavourites() = runBlocking {
        val media = Media("1", "media 1", MediaType.AUDIO)

        repository.saveToFavourites(media)

        coVerify { mockFavouritesDatabase.insert(media) }
    }

    @Test
    fun shouldRemoveMediaFromFavourites() = runBlocking {
        val media = Media("1", "media 1", MediaType.AUDIO)

        repository.removeFromFavourites(media)

        coVerify { mockFavouritesDatabase.delete(media) }
    }

    @Test
    fun whenMediaIsInFavourites_availableOperationsShouldBeShareAndRemoveFromFavourites() {
        val media = Media("1", "media 1", MediaType.AUDIO)

        coEvery { mockFavouritesDatabase.count(media.id) } returns 1

        val operations = runBlocking {
            repository.getAvailableOperations(media)
        }

        assertThat(operations).containsExactly(Operation.SHARE, Operation.REMOVE_FROM_FAVOURITES)
    }

    @Test
    fun whenMediaIsNotInFavourites_availableOperationsShouldBeShareAndAddToFavourites() {
        val media = Media("1", "media 1", MediaType.AUDIO)

        coEvery { mockFavouritesDatabase.count(media.id) } returns 0

        val operations = runBlocking {
            repository.getAvailableOperations(media)
        }

        assertThat(operations).containsExactly(Operation.SHARE, Operation.ADD_TO_FAVOURITES)
    }

}