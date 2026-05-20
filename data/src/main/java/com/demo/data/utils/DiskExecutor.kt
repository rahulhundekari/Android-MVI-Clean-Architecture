package com.demo.data.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskExecutor : Executor {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun execute(p0: Runnable?) {
        executor.execute(p0)
    }

}