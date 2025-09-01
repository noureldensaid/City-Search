package com.klivvr.citysearch.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klivvr.citysearch.core.presentation.ui.theme.ButtonBackground
import com.klivvr.citysearch.core.presentation.ui.theme.GreyText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefaultSearchTextField(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    initialSearchText: String = "",
    placeholder: String = "Search...",
    debounceTime: Long = 120L,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = GreyText,
            modifier = Modifier.size(18.dp)
        )
    },
    trailingIcon: @Composable ((onClear: () -> Unit) -> Unit)? = { onClear ->
        IconButton(
            onClick = onClear,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear search",
                tint = Color.Black,
            )
        }
    },
    searchBackground: Color = ButtonBackground,
    cursorColor: Color = Color.DarkGray,
    textColor: Color = Color.DarkGray,
    placeholderColor: Color = Color.DarkGray,
    iconTint: Color = Color.DarkGray,
    cornerRadius: Dp = 10.dp,
    height: Dp = 40.dp,
    enabled: Boolean = true,
    onImeSearch: (() -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    var searchText by rememberSaveable { mutableStateOf(initialSearchText) }
    var isSearchFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    // Sync initial text if needed
    LaunchedEffect(initialSearchText) {
        searchText = initialSearchText
    }

    // Debounce logic
    val debounceJob = remember { mutableStateOf<Job?>(null) }
    val latestOnSearch = rememberUpdatedState(onSearch)

    LaunchedEffect(searchText) {
        debounceJob.value?.cancel()
        debounceJob.value = coroutineScope.launch {
            delay(debounceTime)
            latestOnSearch.value(searchText)
        }
    }

    // Focus callback
    LaunchedEffect(isSearchFocused) {
        onFocusChanged?.invoke(isSearchFocused)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(searchBackground)
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = searchText,
            enabled = enabled,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isSearchFocused = it.isFocused
                },
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = textColor,
                fontWeight = FontWeight.Normal
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onImeSearch?.invoke() ?: onSearch(searchText)
                }
            ),
            cursorBrush = SolidColor(cursorColor),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CompositionLocalProvider(LocalContentColor provides iconTint) {
                        leadingIcon?.invoke()
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = if (leadingIcon != null) 8.dp else 0.dp)
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = placeholderColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }

                    if (searchText.isNotEmpty() && trailingIcon != null) {
                        CompositionLocalProvider(LocalContentColor provides iconTint) {
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                trailingIcon {
                                    searchText = ""
                                    onSearch("")
                                    focusRequester.requestFocus()
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun DefaultSearchTextFieldPreview() {
    DefaultSearchTextField(
        onSearch = { searchQuery ->

        },
    )
}