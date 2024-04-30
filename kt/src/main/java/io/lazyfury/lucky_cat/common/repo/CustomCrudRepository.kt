package io.lazyfury.lucky_cat.common.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface CustomCrudRepository<T,ID>:CrudRepository<T,ID>,JpaSpecificationExecutor<T> {
    fun findAll(page: Pageable):Page<T>
    override fun findAll(spec: Specification<T>, page: Pageable):Page<T>
    override fun count(spec: Specification<T>):Long
}