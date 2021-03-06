package com.ukdev.carcadasalborghetti.ui.di

import com.ukdev.carcadasalborghetti.di.LayerModule
import com.ukdev.carcadasalborghetti.ui.media.*
import com.ukdev.carcadasalborghetti.ui.tools.MenuProvider
import com.ukdev.carcadasalborghetti.ui.tools.MenuProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object PaidUiModule : LayerModule() {

    override val module = module {
        factory {
            AudioHandler(
                    mediaHelper = get(),
                    crashReportManager = get(),
                    fileHelper = get(),
                    remoteDataSource = get(),
                    ioHelper = get()
            )
        }
        factory {
            FavouritesHandler(
                    mediaHelper = get(),
                    crashReportManager = get(),
                    fileHelper = get(),
                    remoteDataSource = get(),
                    ioHelper = get()
            )
        }
        factory {
            VideoHandler(
                    mediaHelper = get(),
                    crashReportManager = get(),
                    fileHelper = get(),
                    remoteDataSource = get(),
                    ioHelper = get()
            )
        }
        factory<VideoHelper> { VideoHelperImpl(context = androidContext()) }
        factory<MenuProvider> { MenuProviderImpl(localDataSource = get()) }
    }

}