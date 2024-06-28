package net.chasmine.ninjalauncher.intent

sealed class LauncherIntent {
    object LoadApps : LauncherIntent()
    data class GroupApps(val appIds: List<String>) : LauncherIntent()
    data class AddHomeScreen(val name: String) : LauncherIntent()
    data class RemoveHomeScreen(val id: String) : LauncherIntent()
    data class ReorderHomeScreens(val order: List<String>) : LauncherIntent()
    data class RenameHomeScreen(val id: String, val newName: String) : LauncherIntent()
    object EnterNinjaMode : LauncherIntent()
    object ExitNinjaMode : LauncherIntent()
}
