package net.chasmine.ninjalauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.chasmine.ninjalauncher.intent.LauncherIntent
import net.chasmine.ninjalauncher.model.AppModel
import net.chasmine.ninjalauncher.model.HomeScreenModel

class LauncherViewModel : ViewModel() {
    private val _apps = MutableStateFlow<List<AppModel>>(emptyList())
    val apps: StateFlow<List<AppModel>> get() = _apps

    private val _homeScreens = MutableStateFlow<List<HomeScreenModel>>(emptyList())
    val homeScreens: StateFlow<List<HomeScreenModel>> get() = _homeScreens

    private val _isNinjaMode = MutableStateFlow(false)
    val isNinjaMode: StateFlow<Boolean> get() = _isNinjaMode

    private val _intentChannel = Channel<LauncherIntent>(Channel.UNLIMITED)
    val intentFlow = _intentChannel.receiveAsFlow()

    init {
        loadApps()
        loadHomeScreens()
    }

    private fun loadApps() {
        _apps.value = listOf(
            AppModel(id = "1", name = "App 1", packageName = "com.example.app1", icon = ""),
            AppModel(id = "2", name = "App 2", packageName = "com.example.app2", icon = "")
        )
    }

    private fun loadHomeScreens() {
        _homeScreens.value = listOf(
            HomeScreenModel(id = "1", name = "Home Screen 1"),
            HomeScreenModel(id = "2", name = "Home Screen 2")
        )
    }

    fun groupApps(selectedApps: List<AppModel>) {
        sendIntent(LauncherIntent.GroupApps(selectedApps.map { it.id }))
    }

    fun addHomeScreen(name: String) {
        val newHomeScreen = HomeScreenModel(id = (_homeScreens.value.size + 1).toString(), name = name)
        _homeScreens.value = _homeScreens.value + newHomeScreen
        sendIntent(LauncherIntent.AddHomeScreen(name))
    }

    fun reorderHomeScreens(newOrder: List<HomeScreenModel>) {
        _homeScreens.value = newOrder
        sendIntent(LauncherIntent.ReorderHomeScreens(newOrder.map { it.id }))
    }

    fun renameHomeScreen(id: String, newName: String) {
        val updatedHomeScreens = _homeScreens.value.map {
            if (it.id == id) it.copy(name = newName) else it
        }
        _homeScreens.value = updatedHomeScreens
        sendIntent(LauncherIntent.RenameHomeScreen(id, newName))
    }

    fun enterNinjaMode() {
        _isNinjaMode.value = true
        sendIntent(LauncherIntent.EnterNinjaMode)
    }

    fun exitNinjaMode() {
        _isNinjaMode.value = false
        sendIntent(LauncherIntent.ExitNinjaMode)
    }

    private fun sendIntent(intent: LauncherIntent) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }
}

