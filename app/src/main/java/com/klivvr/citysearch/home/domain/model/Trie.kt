package com.klivvr.citysearch.home.domain.model


/**
 * A Trie (prefix tree) data structure optimized for searching city names.
 *
 * This implementation is designed to provide efficient prefix-based search over a list of [CityModel] objects.
 * Instead of storing the city objects directly within the trie nodes, it stores integer indices
 * that correspond to the city's position in the original list provided at construction. This approach
 * avoids circular references and keeps the domain models immutable.
 *
 * The primary operations are [insert] to build the trie and [search] to find all cities
 * matching a given prefix. Search results are automatically sorted to ensure a stable and
 * predictable UI display.
 *
 * It is expected that the words inserted into the trie are pre-processed (e.g., lowercased).
 *
 * @param cities The complete list of [CityModel] objects that this trie will index. This list
 *               is held by reference to retrieve city data from the found indices.
 */
class Trie(private val cities: List<CityModel>) {

    private class Node(
        val children: MutableMap<Char, Node> = HashMap(),
        var isEndOfWord: Boolean = false,
        // store indices into `cities` list; may contain multiple cities for same word
        val cityIndices: MutableList<Int> = mutableListOf()
    )

    private val root = Node()

    /**
     * Insert a word (already normalized/lowercased) with its city index.
     *
     * @param word The normalized search term (e.g., city name in lowercase).
     *             Must not be empty.
     * @param index The index of the city in the [cities] list.
     *              Must be a valid index within the cities list bounds.
     * @throws IllegalArgumentException if word is empty or index is out of bounds.
     */
    fun insert(word: String, index: Int) {
        require(word.isNotEmpty()) {
            "Cannot insert empty word into Trie"
        }
        require(index in cities.indices) {
            "Invalid city index: $index. Must be between 0 and ${cities.size - 1}"
        }

        var node = root
        for (ch in word) {
            node = node.children.getOrPut(ch) { Node() }
        }
        node.isEndOfWord = true
        node.cityIndices.add(index)
    }

    /**
     * Search by prefix (assumed already trimmed/lowercased).
     * Returns cities sorted by name then country to keep UI stable.
     *
     * @param prefix The search prefix (e.g., "par" to find "Paris", "Parma", etc.).
     *               Must not be empty - use empty check before calling.
     * @return A list of matching [CityModel] objects, sorted by name and country.
     *         Returns empty list if no matches found or prefix is invalid.
     */
    fun search(prefix: String): List<CityModel> {
        if (prefix.isEmpty()) return emptyList()

        var node = root
        for (ch in prefix) {
            node = node.children[ch] ?: return emptyList()
        }

        // collect all indices under this subtree
        val indices = collectIndices(node, mutableListOf())
        if (indices.isEmpty()) return emptyList()

        // map indices to models and sort
        return indices
            .asSequence()
            .map { cities[it] }
            .sortedWith(compareBy(CityModel::name, CityModel::country))
            .toList()
    }

    /**
     * Recursively collects all city indices from this node and its descendants.
     */
    private fun collectIndices(node: Node, out: MutableList<Int>): MutableList<Int> {
        if (node.isEndOfWord && node.cityIndices.isNotEmpty()) {
            out.addAll(node.cityIndices)
        }
        for (child in node.children.values) {
            collectIndices(child, out)
        }
        return out
    }

    /**
     * Returns the number of cities indexed in this Trie.
     * Useful for debugging and monitoring.
     */
    fun size(): Int = cities.size
}