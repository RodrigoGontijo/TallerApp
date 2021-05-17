package com.example.tallerapp


import androidx.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class RxSchedulerRule: TestRule {
    private val immediateScheduler = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Scheduler.Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
        }
    }

    @NonNull
    override fun apply(@NonNull base: Statement, @NonNull description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitComputationSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitSingleSchedulerHandler { immediateScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}