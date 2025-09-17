package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.model.CitySection
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A use case that groups a list of cities alphabetically by the first letter of their name.
 *
 * This class transforms a flat list of `CityModel` objects into a structured list of `CitySection` objects,
 * where each section represents a letter of the alphabet and contains the cities starting with that letter.
 *
 * The grouping is case-sensitive and based on the first character of the city's display name.
 * The resulting sections are sorted alphabetically by their representative letter. Within each section,
 * the cities are sorted first by name, then by country, ensuring a stable and predictable order in the UI.
 *
 * Example:
 * Input: `[City(name="London"), City(name="Los Angeles"), City(name="Abu Dhabi")]`
 * Output:
 * - `CitySection(letter='A', items=[City(name="Abu Dhabi")])`
 * - `CitySection(letter='L', items=[City(name="London"), City(name="Los Angeles")])`
 */

@Singleton
class GroupCitiesUseCase @Inject constructor() {

    operator fun invoke(cities: List<CityModel>): List<CitySection> {
        if (cities.isEmpty()) return emptyList()

        // Group by first letter of *display* name (not normalized),
        // then sort keys and values for stable UI.
        val grouped = cities.groupBy { it.name.first() }
        return grouped.toSortedMap()
            .map { (letter, items) ->
                CitySection(
                    letter = letter,
                    items = items.sortedWith(compareBy(CityModel::name, CityModel::country))
                )
            }
    }
}