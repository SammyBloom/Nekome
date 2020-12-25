package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.trending.dto.TrendingResponseDto
import com.chesire.nekome.trending.api.ErrorT
import com.chesire.nekome.trending.api.TrendingApi
import com.chesire.nekome.trending.api.TrendingDomain
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the [TrendingApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuTrending @Inject constructor(
    private val trendingService: KitsuTrendingService,
    private val map: TrendingItemDtoMapper
) : TrendingApi {

    override suspend fun getTrendingAnime(): Result<List<TrendingDomain>, ErrorT> {
        return try {
            val response = trendingService.getTrendingAnimeAsync()
            if (response.isSuccessful) {
                response.body()?.let { trending ->
                    Ok(trending.data.map { map.toTrendingDomain(it) })
                } ?: Err(ErrorT.InvalidCredentials)
            } else {
                Err(ErrorT.CouldNotReach)
            }
        } catch (ex: Exception) {
            Err(ErrorT.CouldNotReach)
        }
    }

    override suspend fun getTrendingManga(): Resource<List<TrendingDomain>> {
        return try {
            parseResponse(trendingService.getTrendingMangaAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<TrendingResponseDto>): Resource<List<TrendingDomain>> {
        return if (response.isSuccessful) {
            response.body()?.let { trending ->
                Resource.Success(trending.data.map { map.toTrendingDomain(it) })
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
