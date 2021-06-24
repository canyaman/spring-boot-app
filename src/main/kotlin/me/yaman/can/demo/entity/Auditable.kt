package me.yaman.can.demo.entity

import java.time.Instant

interface Auditable<T> {
    val createdDate: Instant?
    val createdBy: T?
    val modifiedDate: Instant?
    val modifiedBy: T?
}
