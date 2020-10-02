package com.joffrey.irsdkjava.newimpl;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.trackmaptracker.model.TrackmapTracker;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class DataService {

    private final SdkStarter sdkStarter;
    // hot flux

    public final Flux<TrackmapTracker> INTERVAL_SHORT = Flux.interval(Duration.ofMillis(100))
                                                            .map(aLong -> getDataForMemory())
                                                            .publish();

    private TrackmapTracker getDataForMemory() {

        TrackmapTracker trackmapTracker = new TrackmapTracker();

        trackmapTracker.setDriverIdx(1);
        trackmapTracker.setDriverCarNbr(10);
        trackmapTracker.setDriverInitials("123");
        trackmapTracker.setDriverDistPct(70);

        return null;
    }

    // public final Flux<TrackmapTracker> INTERVAL_SHORT = Flux.just(sdkStarter.isReady())
    //                                                         .flatMap().
    //                                                         .flatMap(Flux.interval(Duration.ofMillis(100), Schedulers.parallel())
    //                                                                      .map(aLong -> getDataForMemory()))
    //                                                         .share();

    // public final Flux<AllData> INTERVAL_SHORT = Flux.never()
    //     .mer
    //
    //
    //
    // public void init() {    }
    // public void step1() {    }
    // public void step2() {    }
    // public void step3() {    }
    //
    //
    //
    //
    // private TrackmapTracker getDataForMemory() {
    //
    //     TrackmapTracker trackmapTracker = new TrackmapTracker();
    //
    //     trackmapTracker.setDriverIdx(1);
    //     trackmapTracker.setDriverCarNbr(10);
    //     trackmapTracker.setDriverInitials("123");
    //     trackmapTracker.setDriverDistPct(70);
    //
    //     return null;
    // }
    //
    // public static final Flux<Long> INTERVAL_LONG = Flux.interval(Duration.ofMillis(500));
    //
    // public Flux<Object> dataAFlux = Flux.();
    //
    //
    // public void teceockosekc() {
    //     EmitterProcessor<Integer> processor = EmitterProcessor.create();
    //     processor.publishOn(scheduler1).map(i -> transform(i)).publishOn(scheduler2).doOnNext(i -> processNext(i)).subscribe();
    // }
    //
    //
    // @RestController
    // class testController {
    //
    //     @GetMapping
    //     public Flux<TrackmapTracker> getFlux() {
    //         return INTERVAL_SHORT.map(allData -> allData.position);
    //     }
    //
    // }


}
