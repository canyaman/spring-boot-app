package me.yaman.can.demo.support

import kotlin.math.ceil

data class PageCount(
    val totalPages: Long,
    val totalElements: Long,
    val defaultPageSize: Int = DEFAULT_PAGE_SIZE,
    val maxPageSize: Int = defaultPageSize * 5,
    val defaultOrderBy: String = "id",
    val pageSizes: List<Int> = listOf(defaultPageSize / 2, defaultPageSize, maxPageSize / 2, maxPageSize),
    val orderProperties: List<String> = listOf("id", "name")
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = DEFAULT_PAGE_SIZE * 5
        fun of(count: Long, size: Int = DEFAULT_PAGE_SIZE) = PageCount(ceil(count.toDouble() / size.toDouble()).toLong(), count, defaultPageSize = size)
    }
}
