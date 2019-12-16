package me.gincos.rhoexercise

import android.app.Application
import me.gincos.rhoexercise.koin.appModule
import org.koin.android.ext.android.startKoin

class TweetsExerciseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}