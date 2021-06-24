package me.yaman.can.demo.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table
data class Simple(
    @Id
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Column(nullable = false,length = 4096)
    val name: String,
    val counter: Long,
    @CreatedDate
    override val createdDate: Instant? = null,
    @Column(length = 4096)
    @CreatedBy
    override val createdBy: String? = null,
    @LastModifiedDate
    override val modifiedDate: Instant? = null,
    @Column(length = 4096)
    @LastModifiedBy
    override val modifiedBy: String? = null,
    @Version
    val version: Int = 0
) : Auditable<String>
