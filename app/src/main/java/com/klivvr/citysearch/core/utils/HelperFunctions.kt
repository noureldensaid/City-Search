package com.klivvr.citysearch.core.utils

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.text.NumberFormat
import java.util.Locale

fun String.toFlagEmoji(): String {
    val cc = trim().uppercase(Locale.ROOT)
    return if (cc.length == 2) {
        cc.map { char -> Character.toChars(0x1F1E6 + (char.code - 'A'.code)) }
            .joinToString("") { String(it) }
    } else "🏳️"
}

fun Int.toReadableString(locale: Locale = Locale.getDefault()): String {
    return NumberFormat.getInstance(locale).format(this)
}

@Composable
fun skipInteraction() = object : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}