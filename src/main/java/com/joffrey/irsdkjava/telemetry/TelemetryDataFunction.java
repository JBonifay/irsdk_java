package com.joffrey.irsdkjava.telemetry;

import java.util.function.BiFunction;

public interface TelemetryDataFunction<T, U, V, X, R> extends BiFunction {

    R apply(T t, U u, V v, X x);


}
