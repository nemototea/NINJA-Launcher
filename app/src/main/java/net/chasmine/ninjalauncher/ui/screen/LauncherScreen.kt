package net.chasmine.ninjalauncher.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.chasmine.ninjalauncher.model.AppModel
import net.chasmine.ninjalauncher.model.HomeScreenModel
import net.chasmine.ninjalauncher.viewmodel.LauncherViewModel

@Composable
fun LauncherScreen(
    viewModel: LauncherViewModel = viewModel(),
    onNinjaModeToggle: () -> Unit
) {
    val apps by viewModel.apps.collectAsState(initial = emptyList())
    val selectedApps = remember { mutableStateListOf<AppModel>() }
    val homeScreens by viewModel.homeScreens.collectAsState(initial = emptyList())
    val isNinjaMode by viewModel.isNinjaMode.collectAsState()
    var screenBeingEdited by remember { mutableStateOf<HomeScreenModel?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "App List", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(apps) { app ->
                AppItem(app = app, isSelected = selectedApps.contains(app)) {
                    if (selectedApps.contains(app)) {
                        selectedApps.remove(app)
                    } else {
                        selectedApps.add(app)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.groupApps(selectedApps) }) {
            Text(text = "Group Selected Apps")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Home Screens", style = MaterialTheme.typography.h4)

        LazyColumn {
            items(homeScreens) { screen ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { screenBeingEdited = screen }
                        .padding(8.dp)
                ) {
                    Text(text = screen.name, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.reorderHomeScreens(homeScreens - screen + screen) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.addHomeScreen("New Screen") }) {
            Text(text = "Add Home Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNinjaModeToggle) {
            Text(text = if (isNinjaMode) "Exit NINJA Mode" else "Enter NINJA Mode")
        }
    }

    if (screenBeingEdited != null) {
        EditHomeScreenDialog(
            homeScreen = screenBeingEdited!!,
            onDismiss = { screenBeingEdited = null },
            onSave = { id, newName ->
                viewModel.renameHomeScreen(id, newName)
                screenBeingEdited = null
            }
        )
    }
}

@Composable
fun AppItem(app: AppModel, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(16.dp)
    ) {
        Text(text = app.name, modifier = Modifier.weight(1f))
        Checkbox(checked = isSelected, onCheckedChange = { onSelect() })
    }
}

@Composable
fun EditHomeScreenDialog(homeScreen: HomeScreenModel, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var newName by remember { mutableStateOf(homeScreen.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Home Screen") },
        text = {
            Column {
                TextField(value = newName, onValueChange = { newName = it })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(homeScreen.id, newName) }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
