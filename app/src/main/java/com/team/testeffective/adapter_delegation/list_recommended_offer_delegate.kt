package com.team.testeffective.adapter_delegation

import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.team.domain.models.RecommendedOffer
import com.team.testeffective.R
import com.team.testeffective.databinding.RecommendedOfferItemBinding

fun recommendedOfferDelegate(
    glide: RequestManager,
) = adapterDelegateViewBinding<RecommendedOffer, RecommendedOffer, RecommendedOfferItemBinding>(
    { layoutInflater, root ->
        RecommendedOfferItemBinding.inflate(
            layoutInflater, root, false
        )
    }
) {

    bind {
        with(binding) {
            recommendedOfferTitle.text = item.title
            recommendedOfferPrice.text = "${item.price.value} ₽ >"
            recommendedOfferTimeRange.text = item.timeRange.joinToString(" ").trim()
        }
        when (item.id) {
            1 -> {
                glide.load(
                    getDrawable(R.drawable.first_circle)
                ).into(binding.recommendedOfferImg)
            }

            10 -> {
                glide.load(
                    getDrawable(R.drawable.second_circle)
                ).into(binding.recommendedOfferImg)
            }

            30 -> {
                glide.load(
                    getDrawable(R.drawable.third_circle)
                ).into(binding.recommendedOfferImg)
            }
        }
    }
}