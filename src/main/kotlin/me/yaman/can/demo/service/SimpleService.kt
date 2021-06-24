package me.yaman.can.demo.service

import me.yaman.can.demo.entity.Simple
import me.yaman.can.demo.model.SimpleModel
import me.yaman.can.demo.repository.SimpleRepository
import me.yaman.can.demo.support.PageCount
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class SimpleService(val simpleRepository: SimpleRepository) :
    PageableService<SimpleModel>,
    CrudService<Long, SimpleModel> {
    override fun getItem(id: Long): Mono<SimpleModel> {
        return simpleRepository.findById(id).map { it.toModel() }
    }

    override fun saveItem(item: SimpleModel): Mono<SimpleModel> {
        val entity = item.copy(id = null).toEntity()
        return simpleRepository.save(entity)
            .map { it.toModel() }
    }

    override fun updateItem(item: SimpleModel): Mono<SimpleModel> {
        return simpleRepository.findById(item.id!!)
            .map { stored -> stored.toUpdate(item.toEntity()) }
            .flatMap { simpleRepository.save(it) }
            .map { it.toModel() }
    }

    override fun removeItem(id: Long): Mono<Void> {
        return simpleRepository.deleteById(id)
    }

    override fun listItems(pageRequest: PageRequest): Flux<SimpleModel> {
        return simpleRepository.findAll(pageRequest.sort).map { it.toModel() }
    }

    override fun countItems(): Mono<PageCount> {
        return simpleRepository.count().map { PageCount.of(count = it) }
    }
}

fun Simple.toModel(): SimpleModel = SimpleModel(this.id, this.name, this.counter, this.createdDate ?: Instant.now(), this.modifiedDate)
fun SimpleModel.toEntity(): Simple = Simple(this.id, this.name, this.counter)
fun Simple.toUpdate(newItem: Simple): Simple = Simple(this.id, newItem.name, newItem.counter, this.createdDate, this.createdBy, null, null, this.version)
