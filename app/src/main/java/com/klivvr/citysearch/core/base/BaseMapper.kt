package com.klivvr.citysearch.core.base

interface BaseMapper<F, T> {
    fun map(from: F): T
}