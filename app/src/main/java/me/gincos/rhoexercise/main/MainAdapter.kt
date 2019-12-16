package me.gincos.rhoexercise.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import org.koin.standalone.KoinComponent
import kotlinx.android.synthetic.main.list_item.view.*
import me.gincos.rhoexercise.R
import me.gincos.rhoexercise.network.responses.Status

class MainAdapter :
    ListAdapter<Status, StatusViewHolder>(diffUtil),
    KoinComponent {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return StatusViewHolder(view)
    }


    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class StatusViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(status: Status) {
        containerView.itemTitle.text = status.text
        containerView.userTv.text =
            status.user?.name ?: containerView.context.getString(R.string.unknown_user)
    }

}

val diffUtil = object : DiffUtil.ItemCallback<Status>() {
    override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean {
        return oldItem.id_str == newItem.id_str
    }

    override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean {
        return oldItem == newItem
    }
}