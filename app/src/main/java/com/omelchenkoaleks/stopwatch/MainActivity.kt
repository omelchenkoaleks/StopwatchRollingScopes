package com.omelchenkoaleks.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omelchenkoaleks.stopwatch.databinding.ActivityMainBinding
import com.omelchenkoaleks.stopwatch.model.Stopwatch
import com.omelchenkoaleks.stopwatch.recycler.StopwatchAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // в этом списке будут хранится стейты секундомеров
    private val stopwatches = mutableListOf<Stopwatch>()

    private val stopwatchAdapter = StopwatchAdapter()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            stopwatches.add(Stopwatch(nextId++, 0, true))
            stopwatchAdapter.submitList(stopwatches.toList())
        }
    }
}