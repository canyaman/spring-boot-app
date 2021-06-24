package me.yaman.can.demo.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono
import javax.validation.Valid

interface CrudController<I, M> {
    @GetMapping("/{id}")
    fun readModel(@PathVariable id: I): Mono<M>

    @PostMapping("/")
    fun createModel(@Valid @RequestBody item: M): Mono<M>

    @PutMapping("/{id}")
    fun updateModel(@PathVariable id: I, @Valid @RequestBody item: M): Mono<M>

    @DeleteMapping("/{id}")
    fun deleteModel(@PathVariable id: I): Mono<Void>
}
