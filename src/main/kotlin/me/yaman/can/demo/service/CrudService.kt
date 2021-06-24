package me.yaman.can.demo.service

import reactor.core.publisher.Mono

interface CrudService<L, M> {
    fun getItem(id: L): Mono<M>
    fun saveItem(item: M): Mono<M>
    fun updateItem(item: M): Mono<M>
    fun removeItem(id: L): Mono<Void>
}
