package com.demo.data.repository.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.demo.data.entities.MovieData
import com.demo.domain.util.Result

private const val STARTING_PAGE_INDEX = 1

class SearchMoviePagingSource(
    val query: String,
    val remote: MovieDataSource.Remote
) : PagingSource<Int, MovieData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return when (val result = remote.search(query, page, params.loadSize)) {
            is Result.Success -> LoadResult.Page(
                data = result.data.distinctBy { movieData -> movieData.id },
                nextKey = if (result.data.isEmpty()) null else page + 1,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            )

            is Result.Error -> LoadResult.Error(result.error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }


}