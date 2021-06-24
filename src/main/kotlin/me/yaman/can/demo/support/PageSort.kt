package me.yaman.can.demo.support

import org.springframework.data.domain.Sort

class PageSort {
    companion object {
        fun by(sortStr: String): Sort? {
            val orders: List<Sort.Order> = sortStr.split(",").mapNotNull { orderStr ->
                val orderTuple = orderStr.split(":")
                val property = orderTuple.firstOrNull()
                val direction = orderTuple.lastOrNull()?.let { Sort.Direction.valueOf(it) }
                if (property == null) {
                    null
                } else {
                    when (direction) {
                        Sort.Direction.ASC -> Sort.Order.asc(property)
                        Sort.Direction.DESC -> Sort.Order.desc(property)
                        else -> Sort.Order.by(property)
                    }
                }
            }
            return when (orders.count()) {
                in 1..Int.MAX_VALUE -> orders.let { Sort.by(it) }
                else -> Sort.unsorted()
            }
        }
    }
}
