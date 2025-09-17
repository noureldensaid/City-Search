package com.klivvr.citysearch.core.base

/**
 * A sealed interface representing the state of a response, typically from an asynchronous operation
 * like a network request. It can either be a [Success] or an [Error].
 *
 * This class is designed to encapsulate the result of an operation, making it easy to handle
 * both successful outcomes and failures in a type-safe way.
 *
 * @param D The type of the data expected in a successful response. The `out` variance
 *          allows for covariance, meaning a `ResponseState<Subtype>` can be used where a
 *          `ResponseState<Supertype>` is expected.
 */
sealed interface ResponseState<out D> {
    data class Success<out D>(val data: D) : ResponseState<D>
    data class Error(val exception: Throwable) : ResponseState<Nothing>
}