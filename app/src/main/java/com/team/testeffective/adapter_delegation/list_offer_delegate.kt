package com.team.testeffective.adapter_delegation

import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.team.domain.models.Offer
import com.team.testeffective.R
import com.team.testeffective.databinding.OfferItemBinding

fun offerAdapterDelegate(
    glide: RequestManager,
) = adapterDelegateViewBinding<Offer, Offer, OfferItemBinding>(
    { layoutInflater, root -> OfferItemBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            titleOffer.text = item.title
            townOffer.text = item.town
            priceOffer.text = "от ${item.price.value} ₽"
        }
        when (item.id) {
            1 -> {
                glide.load(
                    getDrawable(R.drawable.first)
                ).into(binding.imageOffer)
            }

            2 -> {
                glide.load(
                    getDrawable(R.drawable.second)
                ).into(binding.imageOffer)
            }

            3 -> {
                glide.load(
                    getDrawable(R.drawable.third)
                ).into(binding.imageOffer)
            }
        }
    }
}