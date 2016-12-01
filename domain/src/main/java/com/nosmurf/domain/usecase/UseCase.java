package com.nosmurf.domain.usecase;

import com.nosmurf.domain.executor.PostExecutionThread;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public abstract class UseCase {

    private final PostExecutionThread postExecutionThread;

    private Subscription subscription = Subscriptions.empty();

    public UseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void execute(Subscriber subscriber) {
        subscription = getObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    protected abstract Observable getObservable();

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
