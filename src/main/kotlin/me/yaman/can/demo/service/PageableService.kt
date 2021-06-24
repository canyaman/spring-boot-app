package me.yaman.can.demo.service

import me.yaman.can.demo.support.PageCount
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PageableService<M> {
    fun countItems(): Mono<PageCount>
    fun listItems(pageRequest: PageRequest): Flux<M>
}
