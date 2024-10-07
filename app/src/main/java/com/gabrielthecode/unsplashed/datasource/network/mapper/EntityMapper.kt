package com.gabrielthecode.unsplashed.datasource.network.mapper

interface EntityMapper<Entity, DomainModel> {
	fun mapToDomain(entity: Entity): DomainModel
	fun mapToEntity(domainModel: DomainModel): Entity
}
