package com.nosmurf.domain.executor;

import rx.Scheduler;

public interface PostExecutionThread {

    Scheduler getScheduler();

}
