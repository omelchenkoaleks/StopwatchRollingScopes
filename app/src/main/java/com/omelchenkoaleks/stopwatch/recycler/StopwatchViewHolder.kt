package com.omelchenkoaleks.stopwatch.recycler

import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.stopwatch.databinding.StopwatchItemBinding
import com.omelchenkoaleks.stopwatch.model.Stopwatch

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    /*
        экземпляр stopwatch приходит из метода адаптера и содержит актуальные
        параметры для данного элемента списка
     */
    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
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
    }
}