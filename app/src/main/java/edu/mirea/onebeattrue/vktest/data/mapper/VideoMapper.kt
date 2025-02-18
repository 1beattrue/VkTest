package edu.mirea.onebeattrue.vktest.data.mapper

import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoDto
import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoResponseDto
import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlin.collections.map

fun VideoDto.toDbModel(
    page: Int,
    perPage: Int,
    prevPage: String?,
    nextPage: String?,
): VideoDbModel = VideoDbModel(
    id = id,
    thumbnail = image,
    duration = duration,
    author = author.name,
    videoFile = videoFiles.first().link,
    previewPicture = videoPictures.first().picture,
    page = page,
    perPage = perPage,
    prevPage = prevPage,
    nextPage = nextPage
)

fun VideoResponseDto.toDbModels(): List<VideoDbModel> = videos.map {
    it.toDbModel(page, perPage, prevPage, nextPage)
}

fun VideoDbModel.toEntity(): Video = Video(
    id = id,
    thumbnail = thumbnail,
    duration = duration,
    author = author,
    videoFile = videoFile,
    previewPicture = previewPicture
)