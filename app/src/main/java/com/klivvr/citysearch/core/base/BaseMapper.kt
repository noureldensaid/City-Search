package com.klivvr.citysearch.core.base

/**
 * A generic base interface for mapping one object type to another.
 *
 * @param <F> The source type to map from.
 * @param <T> The target type to map to.
 */
interface BaseMapper<F, T> {
    fun map(from: F): T
}