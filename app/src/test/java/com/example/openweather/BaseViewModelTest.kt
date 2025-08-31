package com.example.openweather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

open class BaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // see below

    @OptIn(ExperimentalCoroutinesApi::class)
    class MainDispatcherRule(
        val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {

        override fun starting(description: Description?) {
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description?) {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun runBlockingTest(
        block: suspend TestScope.() -> Unit
    ) = runTest { block() }
}