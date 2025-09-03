package com.klivvr.citysearch.core.utils

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

/**
 * Converts a two-letter ISO 3166-1 alpha-2 country code into its corresponding flag emoji.
 *
 * This extension function takes a string, trims whitespace, and converts it to uppercase.
 * If the resulting string is a valid two-letter country code, it calculates the Unicode
 * regional indicator symbols to form the flag emoji. For example, "US" becomes "🇺🇸".
 *
 * If the input string is not a two-letter code after trimming, it returns a default
 * white flag emoji "🏳️".
 *
 * @return The corresponding flag emoji as a [String], or a white flag "🏳️" if the input is invalid.
 * @receiver The string representing the country code (e.g., "GB", "fr", " de ").
 */
fun String.toFlagEmoji(): String {
    val cc = trim().uppercase(Locale.ROOT)
    return if (cc.length == 2) {
        cc.map { char -> Character.toChars(0x1F1E6 + (char.code - 'A'.code)) }
            .joinToString("") { String(it) }
    } else "🏳️"
}

/**
 * Formats an integer into a locale-specific readable string.
 *
 * For example, in the US locale, the integer `1000000` would be formatted as `"1,000,000"`.
 *
 * @param locale The locale to use for formatting. Defaults to the system's default locale.
 * @return A string representation of the number, formatted according to the specified locale.
 */
fun Int.toReadableString(locale: Locale = Locale.getDefault()): String {
    return NumberFormat.getInstance(locale).format(this)
}

/**
 * A [MutableInteractionSource] that skips all interactions. This is useful for disabling
 * visual feedback on UI elements, such as ripples on click, when no interaction effect
 * is desired. It provides an empty flow of interactions and no-op implementations for
 * emitting them.
 *
 * @return An object implementing [MutableInteractionSource] that ignores all interactions.
 */
@Composable
fun skipInteraction() = object : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}

/**
 * A composable function that collects a [Flow] of events and executes a callback for each event.
 *
 * This function is designed to handle one-time events (like showing a Snackbar or navigating) from a
 * ViewModel within a Composable. It safely collects the flow by tying it to the lifecycle of the
 * composable. The collection starts when the composable enters the `STARTED` state and stops when it
 * enters the `STOPPED` state, automatically restarting if the composable comes back to the foreground.
 *
 * This helps prevent executing event-based actions when the UI is not visible, avoiding crashes or
 * unwanted behavior. The collection is performed on the main thread.
 *
 * @param T The type of the event.
 * @param flow The [Flow] of events to observe.
 * @param key1 An optional key to restart the effect if it changes.
 * @param key2 Another optional key to restart the effect if it changes.
 * @param onEvent The callback lambda to be executed for each event emitted by the [flow].
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}