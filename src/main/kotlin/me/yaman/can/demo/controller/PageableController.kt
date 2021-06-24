package me.yaman.can.demo.controller

import me.yaman.can.demo.support.PageCount
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.constraints.Max
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

interface PageableController<M> {
    @GetMapping("/list")
    fun listModels(
        @PositiveOrZero @RequestParam(defaultValue = "0") page: Int = 0,
        @Max(PageCount.MAX_PAGE_SIZE.toLong()) @Positive @RequestParam(defaultValue = "${PageCount.DEFAULT_PAGE_SIZE}") size: Int = PageCount.DEFAULT_PAGE_SIZE,
        // sort: id:DESC,name:ASC
        @RequestParam(required = false) sort: String? = null
    ): Flux<M>

    @GetMapping("/count")
    fun count(): Mono<PageCount>
}
