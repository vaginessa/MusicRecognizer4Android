package com.mrsep.musicrecognizer.feature.library.presentation.library

import androidx.compose.animation.*
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import com.mrsep.musicrecognizer.core.ui.R as UiR
import com.mrsep.musicrecognizer.core.strings.R as StringsR

private enum class TopBarMode { EmptyLibrary, Default, MultiSelection }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryScreenTopBar(
    isLibraryEmpty: Boolean,
    isFilterApplied: Boolean,
    isMultiselectEnabled: Boolean,
    selectedCount: Int,
    totalCount: Int,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    val topBarMode = if (!isLibraryEmpty) {
        if (isMultiselectEnabled) TopBarMode.MultiSelection else TopBarMode.Default
    } else {
        TopBarMode.EmptyLibrary
    }
    val transition = updateTransition(targetState = topBarMode, label = "topBarMode")
    TopAppBar(
        modifier = modifier,
        title = {
            Crossfade(
                targetState = (selectedCount != 0),
                label = "Title"
            ) { selectionTitle ->
                Text(
                    text = if (selectionTitle) {
                        if (selectedCount == 0) "" else "$selectedCount / $totalCount"
                    } else {
                        stringResource(StringsR.string.library).toUpperCase(Locale.current)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            transition.AnimatedContent(
                contentAlignment = Alignment.CenterStart
            ) { mode ->
                when (mode) {
                    TopBarMode.EmptyLibrary,
                    TopBarMode.Default -> {}

                    TopBarMode.MultiSelection -> IconButton(onClick = onDeselectAll) {
                        Icon(
                            painter = painterResource(UiR.drawable.outline_close_24),
                            contentDescription = stringResource(StringsR.string.disable_multi_selection_mode)
                        )
                    }
                }
            }
        },
        actions = {
            transition.AnimatedContent(
                contentAlignment = Alignment.CenterEnd
            ) { mode ->
                when (mode) {
                    TopBarMode.EmptyLibrary -> {}

                    TopBarMode.Default -> Row(horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                painter = painterResource(UiR.drawable.outline_search_24),
                                contentDescription = stringResource(StringsR.string.search_track)
                            )
                        }
                        IconButton(onClick = onFilterClick) {
                            Icon(
                                painter = painterResource(UiR.drawable.outline_filter_list_24),
                                tint = animateColorAsState(
                                    if (isFilterApplied)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        LocalContentColor.current,
                                    label = "FilterIconColor"
                                ).value,
                                contentDescription = stringResource(StringsR.string.filters)
                            )
                        }
                    }

                    TopBarMode.MultiSelection -> Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onSelectAll) {
                            Icon(
                                painter = painterResource(UiR.drawable.outline_select_all_24),
                                contentDescription = stringResource(StringsR.string.select_all)
                            )
                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                painter = painterResource(UiR.drawable.outline_delete_24),
                                contentDescription = stringResource(StringsR.string.delete_selected)
                            )
                        }
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}