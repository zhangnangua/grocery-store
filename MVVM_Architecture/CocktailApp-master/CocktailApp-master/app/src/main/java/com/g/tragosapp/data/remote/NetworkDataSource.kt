package com.g.tragosapp.data.remote

import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.Cocktail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Gastón Saillén on 03 July 2020
 */
@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val webService: WebService
) {
    suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>> =
        /**
         * 用法错误
         * 1.需要调用close   awaitClose argument is called either when a flow consumer cancels the flow collection or when a callback-based API invokes SendChannel.close manually and is typically used to cleanup the resources after the completion,
         * 2.可能会抛出异常  外层的flow  需要增加catch
         */
        callbackFlow {
            offer(
                Resource.Success(
                    webService.getCocktailByName(cocktailName)?.cocktailList ?: listOf()
                )
            )
            awaitClose { close() }
        }
}