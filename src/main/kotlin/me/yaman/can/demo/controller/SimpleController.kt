package me.yaman.can.demo.controller

import me.yaman.can.demo.model.SimpleModel
import me.yaman.can.demo.service.SimpleService
import me.yaman.can.demo.support.PageCount
import me.yaman.can.demo.support.PageSort
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.ValidationException

@RestController
@RequestMapping(path = ["/simple"])
@Validated
class SimpleController(val simpleService: SimpleService) : PageableController<SimpleModel>, CrudController<Long, SimpleModel> {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/count")
    override fun count(): Mono<PageCount> {
        return simpleService.countItems()
    }

    @GetMapping("/list")
    override fun listModels(page: Int, size: Int, sort: String?): Flux<SimpleModel> {
        return simpleService.listItems(PageRequest.of(page, size, sort?.let { PageSort.by(it) } ?: Sort.unsorted()))
    }

    @GetMapping("/{id}")
    override fun readModel(@PathVariable id: Long): Mono<SimpleModel> {
        return simpleService.getItem(id)
    }

    @PostMapping("/")
    override fun createModel(@Valid @RequestBody item: SimpleModel): Mono<SimpleModel> {
        return simpleService.saveItem(item)
    }

    @PutMapping("/{id}")
    override fun updateModel(@PathVariable id: Long, @Valid @RequestBody item: SimpleModel): Mono<SimpleModel> {
        if (id != item.id) { throw ValidationException("path and body id parameter are not matched.") }
        return simpleService.updateItem(item)
    }

    @DeleteMapping("/{id}")
    override fun deleteModel(@PathVariable id: Long): Mono<Void> {
        return simpleService.removeItem(id)
    }
}
