package com.example.githubrepos.Network;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    public static final int NETWORK_THREAD_POOL_SIZE = 8;

    // A managed pool of background threads
    private final Executor mThreadPoolExecutor;
    private final Executor mNetworkExecutor;

    // A single instance of ThreadPoolManager, used to implement the singleton pattern
    private static volatile ThreadPoolManager sInstance = null;

    /**
     * Constructs the work queues and thread pools.
     */
    private ThreadPoolManager() {
        /*
         * Using Async Task executor
         */
        mThreadPoolExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        mNetworkExecutor = Executors.newFixedThreadPool(NETWORK_THREAD_POOL_SIZE);
    }

    /**
     * Returns the ThreadPoolManager object
     *
     * @return The global ThreadPoolManager object
     */
    public static ThreadPoolManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolManager();
                }
            }
        }
        return sInstance;
    }

    public Executor getExecutor() {
        return mThreadPoolExecutor;
    }

    public Executor getNetworkExecutor() {
        return mNetworkExecutor;
    }

    public void addTask(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}

