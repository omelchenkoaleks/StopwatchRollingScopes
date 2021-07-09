package com.omelchenkoaleks.stopwatch.recycler

import android.os.CountDownTimer
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.stopwatch.databinding.StopwatchItemBinding
import com.omelchenkoaleks.stopwatch.model.Stopwatch

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    /*
        экземпляр stopwatch приходит из метода адаптера и содержит актуальные
        параметры для данного элемента списка
     */
    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()

        if (stopwatch.isStarted) {
            startTimer(stopwatch)
        }
    }

    private fun startTimer(stopwatch: Stopwatch) {
        timer?.cancel() // это необходимо потому что RecyclerView переиспользует ViewHolders и один таймер может наложиться на другой
        timer = getCountDownTimer(stopwatch)
        timer?.start()
    }

    private fun getCountDownTimer(stopwatch: Stopwatch): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                stopwatch.currentMs += interval
                binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
            }
        }
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60
        val ms = this % 1000 / 10

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {
        private const val START_TIME = "00:00:00:00"
        private const val UNIT_TEN_MS = 10L
        private const val PERIOD  = 1000L * 60L * 60L * 24L // Day
    }
}