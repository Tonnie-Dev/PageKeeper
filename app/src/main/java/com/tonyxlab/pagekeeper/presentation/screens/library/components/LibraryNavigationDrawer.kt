package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.RoundedCornerShape20
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint as IconTint

@Composable
fun LibraryNavigationDrawer(
    selectedDestination: LibraryDrawerDestination,
    onCloseClick: () -> Unit,
    onImportBookClick: () -> Unit,
    onDestinationClick: (LibraryDrawerDestination) -> Unit,
    modifier: Modifier = Modifier,
) {

    val (libSelected, favSelected, finishedSelected) =
        Triple(
                first = selectedDestination == LibraryDrawerDestination.Library,
                second = selectedDestination == LibraryDrawerDestination.Favorites,
                third = selectedDestination == LibraryDrawerDestination.Finished
        )

    ModalDrawerSheet(
            modifier = modifier
                    .width(DrawerWidth)
                    .fillMaxHeight()
                    .clip(DrawerShape),
            drawerShape = DrawerShape,
            drawerContainerColor = BgMain
    ) {
        Column(
                modifier = Modifier.padding(
                        top = MaterialTheme.spacing.spaceExtraLarge,
                        start = MaterialTheme.spacing.spaceTen * 2,
                        end = MaterialTheme.spacing.spaceTen * 2
                )
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                        painter = painterResource(id = R.drawable.ic_menu_back),
                        contentDescription = stringResource(id = R.string.cds_text_menu),
                        tint = IconTint
                )
            }

            Box(
                    modifier = Modifier
                            .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.RoundedCornerShape20
                            )
                            .clickable { onImportBookClick() }
                            .padding(MaterialTheme.spacing.spaceMedium)

            ) {

                Row(
                        horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                            modifier = Modifier.size(22.dp),
                            painter = painterResource(id = R.drawable.ic_import_book),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = null
                    )

                    Text(
                            text = stringResource(id = R.string.button_text_import_book),
                            style = MaterialTheme.typography.BodyMediumMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }

            Spacer(modifier = Modifier.size(MaterialTheme.spacing.spaceExtraLarge))

            DrawerItem(
                    label = stringResource(id = R.string.nav_drawer_library),
                    iconRes = if (libSelected) R.drawable.books_filled else R.drawable.books_outlined,
                    selected = libSelected,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Library) }
            )
            DrawerItem(
                    label = stringResource(id = R.string.nav_drawer_favorites),
                    iconRes = if (favSelected) R.drawable.ic_star_filled else R.drawable.ic_star_outlined,
                    selected = favSelected,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Favorites) }
            )
            DrawerItem(
                    label = stringResource(id = R.string.nav_drawer_finished),
                    iconRes = if (finishedSelected) R.drawable.ic_mark_finished else R.drawable.ic_finished_outlined,
                    selected = finishedSelected,
                    onClick = { onDestinationClick(LibraryDrawerDestination.Finished) }
            )
        }
    }
}

@Composable
private fun DrawerItem(
    @DrawableRes
    iconRes: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
            modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall),
            label = {
                Text(
                        text = label,
                        style = MaterialTheme.typography.BodyMediumMedium
                )
            },
            selected = selected,
            onClick = onClick,
            icon = {
                Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = label
                )
            },
            shape = RoundedCornerShape(percent = 100),
            colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = BgActive,
                    unselectedContainerColor = BgMain,
                    selectedIconColor = IconTint,
                    unselectedIconColor = IconTint,
                    selectedTextColor = TextSecondary,
                    unselectedTextColor = TextSecondary
            )
    )
}

val DrawerWidth = 280.dp
private val DrawerShape = RoundedCornerShape(
        topEnd = 24.dp,
        bottomEnd = 24.dp
)

@Preview(showBackground = true)
@Composable
private fun LibraryNavigationDrawerPreview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            LibraryNavigationDrawer(
                    selectedDestination = LibraryDrawerDestination.Library,
                    onCloseClick = {},
                    onImportBookClick = {},
                    onDestinationClick = {}
            )
        }
    }
}
