package com.joshi.mvvmwithjetpackcompose.model

data class CharactersApiResponse(
    val code: String?, val status: String?, val attributioonText: String?, val data: CharacterData?
)

data class CharacterData(
    val total: Int?, val results: List<CharacterResult>?
)

data class CharacterResult(
    val id: Int?,
    val name: String?,
    val description: String?,
    val resourceURI: String?,
    val urls: List<CharacterResultUrl>,
    val thumbnail: CharacterThumbnail?,
    val comics: CharacterComics?
)

data class CharacterResultUrl(
    val type: String?, val urk: String?
)

data class CharacterThumbnail(
    val path: String?, val extension: String?
)


data class CharacterComics(
    val items: List<CharacterComicsItems>?
)

data class CharacterComicsItems(
    val resourceURI: String?,
    val name: String?
)