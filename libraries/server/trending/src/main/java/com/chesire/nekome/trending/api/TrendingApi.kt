package com.chesire.nekome.trending.api

import com.chesire.nekome.core.Resource
import com.github.michaelbull.result.Result

/**
 * Methods relating to getting information about trending topics.
 */
interface TrendingApi {
    /**
     * Retrieves the current trending anime.
     */
    suspend fun getTrendingAnime(): Result<List<TrendingDomain>, ErrorT>

    /**
     * Retrieves the current trending manga.
     */
    suspend fun getTrendingManga(): Resource<List<TrendingDomain>>
}

public sealed class ErrorT {
    object InvalidCredentials: ErrorT()
    object CouldNotReach: ErrorT()
}
