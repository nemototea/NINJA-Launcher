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

    private val _homeScreensModel = MutableStateFlow<List<HomeScreenModel>>(emptyList())
    val homeScreensModel: StateFlow<List<HomeScreenModel>> get() = _homeScreensModel

    private val _isNinjaMode = MutableStateFlow(false)
    val isNinjaMode: StateFlow<Boolean> get() = _isNinjaMode

    private val _intentChannel = Channel<LauncherIntent>(Channel.UNLIMITED)
    val intentFlow = _intentChannel.receiveAsFlow()

    init {
        loadApps()
        loadHomeScreens()
    }

    private fun loadApps() {
        // ダミーデータをロード
        _apps.value = listOf(
            AppModel(id = "1", name = "App 1", packageName = "com.example.app1", icon = ""),
            AppModel(id = "2", name = "App 2", packageName = "com.example.app2", icon = ""),
            // さらにアプリを追加
        )
    }

    private fun loadHomeScreens() {
        // ダミーデータをロード
        _homeScreensModel.value = listOf(
            HomeScreenModel(id = "1", name = "Home Screen 1"),
            HomeScreenModel(id = "2", name = "Home Screen 2")
            // さらにホームスクリーンを追加
        )
    }

    fun groupApps(selectedApps: List<AppModel>) {
        // グループ化のロジックを追加
        sendIntent(LauncherIntent.GroupApps(selectedApps.map { it.id }))
    }

    fun addHomeScreen(name: String) {
        // 新しいホームスクリーンを追加
        val newHomeScreenModel = HomeScreenModel(id = (_homeScreensModel.value.size + 1).toString(), name = name)
        _homeScreensModel.value += newHomeScreenModel
        sendIntent(LauncherIntent.AddHomeScreen(name))
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
