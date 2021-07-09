package com.omelchenkoaleks.stopwatch.recycler

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.stopwatch.R
import com.omelchenkoaleks.stopwatch.StopwatchListener
import com.omelchenkoaleks.stopwatch.databinding.StopwatchItemBinding
import com.omelchenkoaleks.stopwatch.model.Stopwatch

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    /*
        NOTE:
        Такой подход, когда ViewHolder обрабатывает только визуальное представление айтема,
        который пришел ему в методе bind, и ничего не меняет напрямую,
        а все колбэки обрабатываются снаружи (в нашем случае через listener) -
        является предпочтительным.
        Тут мы можем указать на проблему данного приложения.
        Если создать достаточное количество таймеров, и после скролла,
        запущенный таймер окажется за экраном, то таймер может остановится,
        и продолжит работу, только когда опять окажется видимым.
        Это происходит потому, что ViewHolder переиспользуется.
        Поэтому нужно быть аккуратным когда меняешь состоние айтема внутри ViewHolder’a -
        как в нашем случае с использованием CountDownTimer.
     */

    /*
        экземпляр stopwatch приходит из метода адаптера и содержит актуальные
        параметры для данного элемента списка
     */
    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()

        if (stopwatch.isStarted) {
            startTimer(stopwatch)
        } else {
            stopTimer(stopwatch)
        }

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.isStarted) {
                listener.stop(stopwatch.id, stopwatch.currentMs)
            } else {
                listener.start(stopwatch.id)
            }
        }

        binding.restartButton.setOnClickListener { listener.reset(stopwatch.id) }

        binding.deleteButton.setOnClickListener { listener.delete(stopwatch.id) }
    }

    private fun startTimer(stopwatch: Stopwatch) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_pause_24)
        binding.startPauseButton.setImageDrawable(drawable)

        timer?.cancel()
        timer = getCountDownTimer(stopwatch)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(stopwatch: Stopwatch) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_play_arrow_24)
        binding.startPauseButton.setImageDrawable(drawable)

        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
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
        private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }
}