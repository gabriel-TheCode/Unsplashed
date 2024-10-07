package com.gabrielthecode.unsplashed.core.domain

data class SearchDomainModel(
	val results: List<PhotoDomainModel>?,
	val total: Int,
	val totalPages: Int
)

data class PhotoDomainModel(
	val blurHash: String?,
	val color: String?,
	val createdAt: String?,
	val currentUserCollections: List<Any>?,
	val description: String?,
	val height: Int,
	val id: String,
	val likedByUser: Boolean?,
	val likes: Int?,
	val links: PhotoLinksDomainModel?,
	val urls: UrlsDomainModel,
	val user: UserDomainModel,
	val width: Int
)

data class PhotoLinksDomainModel(
	val download: String?,
	val html: String?,
	val self: String?
)

data class UrlsDomainModel(
	val full: String,
	val raw: String?,
	val regular: String?,
	val small: String,
	val thumb: String
)

data class UserDomainModel(
	val firstName: String?,
	val id: String,
	val instagramUsername: String?,
	val lastName: String?,
	val links: UserLinksDomainModel?,
	val name: String?,
	val portfolioUrl: String?,
	val profileImage: ProfileImageDomainModel?,
	val twitterUsername: String?,
	val username: String?
)

data class UserLinksDomainModel(
	val html: String?,
	val likes: String?,
	val photos: String?,
	val self: String?
)

data class ProfileImageDomainModel(
	val large: String?,
	val medium: String?,
	val small: String?
)
