package com.ukdev.carcadasalborghetti.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry

fun mainActivity(block: MainActivityRobot.() -> Unit): MainActivityRobot {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    ActivityScenario.launch(MainActivity::class.java)

    return MainActivityRobot().apply(block)
}

class MainActivityRobot {

    infix fun assert(assertion: MainActivityAssertions.() -> Unit) {
        MainActivityAssertions().run(assertion)
    }

}

class MainActivityAssertions {



}