package com.team.testeffective.adapter_delegation

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.models.Offer

class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {

    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem == newItem
    }
}