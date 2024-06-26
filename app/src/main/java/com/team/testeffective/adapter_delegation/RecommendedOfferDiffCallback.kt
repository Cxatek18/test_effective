package com.team.testeffective.adapter_delegation

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.models.RecommendedOffer

class RecommendedOfferDiffCallback : DiffUtil.ItemCallback<RecommendedOffer>() {

    override fun areItemsTheSame(oldItem: RecommendedOffer, newItem: RecommendedOffer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecommendedOffer, newItem: RecommendedOffer): Boolean {
        return oldItem == newItem
    }
}