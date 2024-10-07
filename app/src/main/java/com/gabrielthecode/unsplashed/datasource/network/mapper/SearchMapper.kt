package com.gabrielthecode.unsplashed.datasource.network.mapper

import com.gabrielthecode.unsplashed.core.domain.PhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.PhotoLinksDomainModel
import com.gabrielthecode.unsplashed.core.domain.ProfileImageDomainModel
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.domain.UrlsDomainModel
import com.gabrielthecode.unsplashed.core.domain.UserDomainModel
import com.gabrielthecode.unsplashed.core.domain.UserLinksDomainModel
import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse
import javax.inject.Inject

class SearchMapper @Inject constructor() :
	EntityMapper<SearchResponse, SearchDomainModel> {
	override fun mapToDomain(entity: SearchResponse): SearchDomainModel {
		val photoList = entity.results?.map { result ->
			PhotoDomainModel(
				id = result.id,
				createdAt = result.createdAt,
				width = result.width,
				height = result.height,
				color = result.color,
				blurHash = result.blurHash,
				likes = result.likes,
				likedByUser = result.likedByUser,
				description = result.description,
				currentUserCollections = result.currentUserCollections,
				user = UserDomainModel(
					id = result.user.id,
					username = result.user.username,
					name = result.user.name,
					firstName = result.user.firstName,
					lastName = result.user.lastName,
					instagramUsername = result.user.instagramUsername,
					twitterUsername = result.user.twitterUsername,
					portfolioUrl = result.user.portfolioUrl,
					profileImage = ProfileImageDomainModel(
						small = result.user.profileImage?.small,
						medium = result.user.profileImage?.medium,
						large = result.user.profileImage?.large
					),
					links = UserLinksDomainModel(
						self = result.user.links?.self,
						html = result.user.links?.html,
						photos = result.user.links?.photos,
						likes = result.user.links?.likes
					)
				),
				urls = UrlsDomainModel(
					raw = result.urls.raw,
					full = result.urls.full,
					regular = result.urls.regular,
					small = result.urls.small,
					thumb = result.urls.thumb
				),
				links = PhotoLinksDomainModel(
					self = result.links?.self,
					html = result.links?.html,
					download = result.links?.download
				)
			)
		}

		return SearchDomainModel(
			results = photoList,
			total = entity.total,
			totalPages = entity.totalPages
		)
	}

	override fun mapToEntity(domainModel: SearchDomainModel): SearchResponse {
		throw NotImplementedError("Mapping from TokenDomainModel to TokenResponseBody is not implemented.")
	}
}



