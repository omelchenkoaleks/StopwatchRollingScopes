package com.omelchenkoaleks.stopwatch.model

data class Stopwatch(
    val id: Int, // чтобы отличать items друг от друга
    var currentMs: Long, // количество миллисекунд прошедших со старта
    var isStarted: Boolean // работает ли секундомер или остановлен
)