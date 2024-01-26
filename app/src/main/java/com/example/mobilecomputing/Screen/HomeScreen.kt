package com.example.mobilecomputing.Screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.ProfileTopAppBar
import com.example.mobilecomputing.R
import com.example.mobilecomputing.data.Profile
import com.example.mobilecomputing.data.getBitmapFromImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToProfileEntry: () -> Unit,
    navigateToProfileUpdate: (Profile) -> Unit,
    navigateToAppSettings: () -> Unit,
    viewModel: AppViewModel
) {
    val homeUiState by viewModel.profileUiStates.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val sensorValuesUIState by viewModel.sensorValuesUIState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProfileTopAppBar(
                title = "Home",
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            Column(

                ) {
                FloatingActionButton(
                    onClick = navigateToProfileEntry,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Character"
                    )
                }
                FloatingActionButton(
                    onClick = navigateToAppSettings,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "App Settings"
                    )
                }
            }
        }
    ) {innerPadding ->
        HomeBody(
            profileList = homeUiState.profileList,
            onProfileClick = navigateToProfileUpdate,
            modifier = modifier
                .padding(innerPadding)
            //.fillMaxSize()
        )
    }
    /*Column {
        Text(text = "Light Value: " + sensorValuesUIState.x.toString())
    }*/

}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    profileList: List<Profile>,
    onProfileClick: (Profile) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (profileList.isEmpty()) {
            Text(
                text = "Empty! Tap + to add new character.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            ProfileListScreen(
                profiles = profileList,
                onClick = { onProfileClick(it) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileListScreen(
    profiles: List<Profile>,
    onClick: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(profiles) { profile ->
            ProfileCard(profile = profile, onClick = { onClick(profile)} )
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    profile: Profile,
    onClick: (Profile) -> Unit,
) {
    Row(
        modifier = modifier.padding(all = 8.dp),
    ) {

        var isImageExpanded by remember { mutableStateOf(false) }
        val imageBitmap = when(profile.imageData) {
            null -> getBitmapFromImage(LocalContext.current, R.drawable.user_placeholder).asImageBitmap()
            else -> profile.imageData.asImageBitmap()
        }
        Image(
            bitmap = imageBitmap,
            contentDescription = profile.imageDescription,
            modifier = Modifier
                .clickable { isImageExpanded = !isImageExpanded }
                .size(if (isImageExpanded) 160.dp else 80.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = ""
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = profile.name,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.clickable { onClick( profile ) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = profile.info,
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .clickable { isExpanded = !isExpanded },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}