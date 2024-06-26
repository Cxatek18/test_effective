package com.team.testeffective.adapter_delegation

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.models.Ticket

class TicketDiffCallback : DiffUtil.ItemCallback<Ticket>() {

    override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem == newItem
    }
}