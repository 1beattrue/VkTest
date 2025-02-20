package edu.mirea.onebeattrue.vktest.data.mapper

import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoDto
import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlin.collections.map

fun VideoDto.toDbModel(page: Int): VideoDbModel = VideoDbModel(
    id = id,
    width = width,
    height = height,
    thumbnail = image,
    duration = duration,
    author = author.name,
    videoUrl = videoFiles.first().link,
    previewPicture = videoPictures.first().picture,
    page = page
)

fun List<VideoDto>.toDbModels(page: Int): List<VideoDbModel> = map { it.toDbModel(page) }

fun VideoDbModel.toEntity(): Video = Video(
    id = id,
    width = width,
    height = height,
    thumbnail = thumbnail,
    duration = duration,
    author = author,
    videoUrl = videoUrl,
    previewPicture = previewPicture,
    page = page
)

fun List<VideoDbModel>.toEntities(): List<Video> = map { it.toEntity() }