package edu.mirea.onebeattrue.vktest.data.mapper

import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoDto
import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlin.collections.map

fun VideoDto.toDbModel(page: Int): VideoDbModel = VideoDbModel(
    id = id,
    thumbnail = image,
    duration = duration,
    author = author.name,
    videoFile = videoFiles.first().link,
    previewPicture = videoPictures.first().picture,
    page = page
)

fun List<VideoDto>.toDbModels(page: Int): List<VideoDbModel> = map { it.toDbModel(page) }

fun VideoDbModel.toEntity(): Video = Video(
    id = id,
    thumbnail = thumbnail,
    duration = duration,
    author = author,
    videoFile = videoFile,
    previewPicture = previewPicture,
    page = page
)

fun List<VideoDbModel>.toEntities(): List<Video> = map { it.toEntity() }