package com.omelchenkoaleks.stopwatch.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.omelchenkoaleks.stopwatch.StopwatchListener
import com.omelchenkoaleks.stopwatch.databinding.StopwatchItemBinding
import com.omelchenkoaleks.stopwatch.model.Stopwatch

class StopwatchAdapter(
    private val listener: StopwatchListener
) : ListAdapter<Stopwatch, StopwatchViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StopwatchItemBinding.inflate(layoutInflater, parent, false)
        return StopwatchViewHolder(
            binding,
            listener,
            binding.root.context.resources
        ) // раздуваем вью и возвращаем созданный ViewHolder
    }

    /*
        метод вызывается в момент создания item
        А ======== в моменты переосздания(айтем вышел за пределы экрана, затем вернулся) и
        в моменты обновления айтемов - занимается DiffUtil
     */
    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
        holder.bind(getItem(position)) // метод вызывается для конкретного item
    }

    private companion object {

        /*
            реализация DiffUtil помогает RecyclerView понять какой айтем изменился(был удален или
            добавлен) и контент какого айтема изменился
         */
        private val itemComparator = object : DiffUtil.ItemCallback<Stopwatch>() {

            override fun areItemsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }

            /*
                P.S. В адаптере, в имплементации DiffUtil мы добавили метод getChangePayload.
                В данном случае это утовка, чтобы айтем не бликовал
                (проигрывается анимация для всего айтема), когда мы нажимаем на кнопки.
                Нормальная реализация payload выходит за рамки этого простого примера.
             */
            override fun getChangePayload(oldItem: Stopwatch, newItem: Stopwatch) = Any()
        }
    }
}