package com.example.materyaldesig.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.materyaldesig.R
import com.example.materyaldesig.databinding.ActivityRecyclerItemEarthBinding
import com.example.materyaldesig.databinding.ActivityRecyclerItemMarsBinding

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Data, Boolean>>,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            TYPE_EARTH ->
                EarthViewHolder(inflater.inflate(
                    R.layout.activity_recycler_item_earth,
                    parent,
                    false
                ) as View
                )
            TYPE_MARS ->
                MarsViewHolder(inflater.inflate(
                    R.layout.activity_recycler_item_mars,
                    parent,
                    false
                ) as View
                )
            else -> HeaderViewHolder(inflater.inflate(
                R.layout.activity_recycler_item_header,
                parent,
                false
            ) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
/*        when (getItemViewType(position)) {
            TYPE_EARTH -> {
                holder as EarthViewHolder
                holder.bind(data[position])
            }
            TYPE_MARS -> {
                holder as MarsViewHolder
                holder.bind(data[position])
            }
            else  -> {
                holder as HeaderViewHolder
                holder.bind(data[position])
            }
        }
 */
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
           position == 0 -> TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(
        Data("Mars", ""),
        false
    )

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {
        private var _binding: ActivityRecyclerItemEarthBinding? = null
        private val binding get() = _binding!!

        override fun bind(data: Pair<Data, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.descriptionTextView.text = data.first.someDescription
                binding.wikiImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
            }
        }

        //Здесь (и далее) не даёт переопределить onDestroy, чтоб обнулить _binding:
        // _binding = null
        //Как быть?
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {
        private var _binding: ActivityRecyclerItemMarsBinding? = null
        private val binding get() = _binding!!

        override fun bind(data: Pair<Data, Boolean>) {
            with(binding) {
                marsImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
                addItemImageView.setOnClickListener {
                    addItem()
                }
                removeItemImageView.setOnClickListener {
                    removeItem()
                }
                moveItemDown.setOnClickListener {
                    moveDown()
                }
                moveItemUp.setOnClickListener {
                    moveUp()
                }
                marsDescriptionTextView.visibility =
                    if (data.second) View.VISIBLE else View.GONE
                marsTextView.setOnClickListener {
                    toggleText()
                }
                dragHandleImageView.setOnTouchListener { _, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this@MarsViewHolder)
                    }
                    false
                }
            }
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also {
                currentPosition ->
                    data.removeAt(currentPosition).apply {
                        data.add(currentPosition + 1, this)
                    }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also {
                currentPosition ->
                    data.removeAt(currentPosition).apply {
                        data.add(currentPosition - 1, this)
                    }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<Data, Boolean>) {
            itemView.setOnClickListener {
                onListItemClickListener.onItemClick(data.first)
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Data)
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }
}