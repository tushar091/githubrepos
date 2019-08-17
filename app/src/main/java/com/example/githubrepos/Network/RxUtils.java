package com.example.githubrepos.Network;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    public static <T> ObservableTransformer<T, T> applyNetworkExecutor() {
        return observable -> observable
                .subscribeOn(Schedulers.from(ThreadPoolManager.getInstance().getNetworkExecutor()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
