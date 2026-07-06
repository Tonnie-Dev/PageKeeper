package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDrawerDestination
import com.tonyxlab.pagekeeper.presentation.theme.BgActive
import com.tonyxlab.pagekeeper.presentation.theme.BgMain
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.Icons
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.RoundedCornerShape100
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlin.math.exp

@Composable
fun LibraryNavigationRail(
    selectedDestination: LibraryDrawerDestination,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onImportBookClick: () -> Unit,
    exitSearch: () -> Unit,
    onDestinationClick: (LibraryDrawerDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val railWidth by animateDpAsState(
            targetValue = if (expanded) ExpandedRailWidth else CollapsedRailWidth,
            label = "libraryRailWidth"
    )

    Column(
            modifier = modifier
                    .width(railWidth)
                    .fillMaxHeight()
                    .background(BgMain)
                    .clickable{ exitSearch() }
                    .padding(
                            top = MaterialTheme.spacing.spaceExtraLarge,
                            start = if (expanded)
                                MaterialTheme.spacing.spaceTen
                            else
                                MaterialTheme.spacing.spaceTen * 2,
                            end = if (expanded)
                                MaterialTheme.spacing.spaceMedium
                            else
                                MaterialTheme.spacing.spaceDefault
                    ),
            horizontalAlignment = if (expanded)
                Alignment.Start
            else
                Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { onExpandedChange(expanded.not()) }) {
            Icon(
                    painter = painterResource(
                            id = if (expanded)
                                R.drawable.ic_menu_back
                            else
                                R.drawable.ic_menu
                    ),
                    contentDescription = stringResource(id = R.string.cds_text_menu),
                    tint = Icons
            )
        }

        ImportBookButton(
                expanded = expanded,
                onClick = onImportBookClick
        )

        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.spaceExtraLarge),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = if (expanded) Alignment.Start else Alignment.CenterHorizontally
        ) {
            RailDestinationItem(
                    label = stringResource(id = R.string.nav_drawer_library),
                    iconRes = if (selectedDestination == LibraryDrawerDestination.Library) {
                        R.drawable.books_filled
                    } else {
                        R.drawable.books_outlined
                    },
                    selected = selectedDestination == LibraryDrawerDestination.Library,
                    expanded = expanded,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Library) }
            )
            RailDestinationItem(
                    label = stringResource(id = R.string.nav_drawer_favorites),
                    iconRes = if (selectedDestination == LibraryDrawerDestination.Favorites) {
                        R.drawable.ic_star_filled
                    } else {
                        R.drawable.ic_star_outlined
                    },
                    selected = selectedDestination == LibraryDrawerDestination.Favorites,
                    expanded = expanded,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Favorites) }
            )
            RailDestinationItem(
                    label = stringResource(id = R.string.nav_drawer_finished),
                    iconRes = if (selectedDestination == LibraryDrawerDestination.Finished) {
                        R.drawable.ic_finished_filled
                    } else {
                        R.drawable.ic_finished_outlined
                    },
                    selected = selectedDestination == LibraryDrawerDestination.Finished,
                    expanded = expanded,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Finished) }
            )
        }
    }
}

@Composable
private fun ImportBookButton(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    Box(
            modifier = Modifier
                    .animateContentSize()
                    .padding(top = MaterialTheme.spacing.spaceMedium)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Primary)
                    .clickable(onClick = onClick)
                    .then(
                            if (expanded) {
                                Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.spaceMedium)
                            } else {
                                Modifier
                                        .size(MaterialTheme.spacing.spaceExtraLarge)
                            }
                    ),
            contentAlignment = Alignment.Center
    ) {
        Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.ic_import_book),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(id = R.string.button_text_import_book)
            )

            AnimatedVisibility(visible = expanded) {
                Text(
                        text = stringResource(id = R.string.button_text_import_book),
                        style = MaterialTheme.typography.BodyMediumMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun RailDestinationItem(
    @DrawableRes
    iconRes: Int,
    label: String,
    selected: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val itemModifier = if (expanded) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.width(96.dp)
    }

    Box(
            modifier = Modifier
                    .clickable(
                            onClick = onClick,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                    ),
            contentAlignment = Alignment.Center
    ) {
        if (expanded) {
            Box(
                    modifier = itemModifier
                            .clip(MaterialTheme.shapes.RoundedCornerShape100)
                            .background(if (selected) BgActive else BgMain)
                            .padding(all = MaterialTheme.spacing.spaceMedium),
                    contentAlignment = Alignment.Center
            ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    RailIcon(
                            iconRes = iconRes,
                            label = label
                    )
                    Text(
                            text = label,
                            style = MaterialTheme.typography.BodyMediumMedium,
                            color = TextSecondary,
                            maxLines = 1
                    )
                }
            }
        } else {
            Column(
                    modifier = Modifier
                            .padding(vertical = MaterialTheme.spacing.spaceDoubleDp * 3),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                        modifier = itemModifier
                                .clip(shape = MaterialTheme.shapes.large)
                                .background(if (selected) BgActive else BgMain)
                                .size(56.dp, 32.dp)
                                .padding(
                                        horizontal = MaterialTheme.spacing.spaceMedium,
                                        vertical = MaterialTheme.spacing.spaceExtraSmall
                                ),
                        contentAlignment = Alignment.Center
                ) {
                    RailIcon(
                            iconRes = iconRes,
                            label = label
                    )
                }
                Text(
                        text = label,
                        style = MaterialTheme.typography.BodyMediumMedium,
                        color = TextSecondary,
                        maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun RailIcon(
    @DrawableRes
    iconRes: Int,
    label: String,
) {
    Icon(
            modifier = Modifier.size(MaterialTheme.spacing.spaceTwelve * 2),
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Icons
    )
}

private val CollapsedRailWidth = 96.dp
private val ExpandedRailWidth = 200.dp

@Preview(showBackground = true)
@Composable
private fun LibraryNavigationRailPreview() {
    PageKeeperTheme {
        LibraryNavigationRail(
                selectedDestination = LibraryDrawerDestination.Library,
                expanded = true,
                onExpandedChange = {},
                exitSearch = {},
                onImportBookClick = {},
                onDestinationClick = {}
        )
    }
}
