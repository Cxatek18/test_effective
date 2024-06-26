package com.team.testeffective.adapter_delegation

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.team.domain.models.Ticket
import com.team.testeffective.databinding.TicketItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
fun ticketDelegate() = adapterDelegateViewBinding<Ticket, Ticket, TicketItemBinding>(
    { layoutInflater, root ->
        TicketItemBinding.inflate(
            layoutInflater, root, false
        )
    }
) {

    bind {
        with(binding) {
            ticketPrice.text = "${item.price.value} ₽"
            if (item.badge != null) {
                ticketBadge.visibility = View.VISIBLE
                ticketBadge.text = item.badge
            }
            ticketDepartureAirport.text = item.departure.airport
            ticketArrivalAirport.text = item.arrival.airport

            ticketTime.text = "${convertDate(item.departure.date)} -" +
                    " ${convertDate(item.arrival.date)}"

            val timeDifference = findingTimeDifference(
                item.arrival.date,
                item.departure.date
            )

            if (!item.hasTransfer) {
                if (timeDifference["days"]!! >= 0) {
                    ticketTimeWay.text = "${timeDifference["hours"]?.let { it1 -> abs(it1) }}" +
                            "ч в пути / Без пересадок"
                }
            } else {
                if (timeDifference["days"]!! >= 0) {
                    ticketTimeWay.text = "${timeDifference["hours"]?.let { it1 -> abs(it1) }}" +
                            "ч в пути"
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun convertDate(dateText: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateText, formatter)
    return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}

@RequiresApi(Build.VERSION_CODES.O)
private fun findingTimeDifference(timeFirst: String, timeSecond: String): Map<String, Int> {
    val listResult = mutableMapOf<String, Int>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    try {
        val dateTime1 = LocalDateTime.parse(timeFirst, formatter)
        val dateTime2 = LocalDateTime.parse(timeSecond, formatter)

        val duration = java.time.Duration.between(dateTime1, dateTime2)

        val hours = duration.toHours().toInt()
        val days = duration.toDays().toInt()

        listResult["hours"] = hours
        listResult["days"] = days

    } catch (e: Exception) {
        Log.d("Error", e.toString())
    }

    return listResult
}