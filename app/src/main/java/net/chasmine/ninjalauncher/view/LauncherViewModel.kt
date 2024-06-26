package net.chasmine.ninjalauncher.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.chasmine.ninjalauncher.intent.LauncherIntent

class LauncherViewModel : ViewModel() {
    private val _intentChannel = Channel<LauncherIntent>(Channel.UNLIMITED)
    val intentFlow = _intentChannel.receiveAsFlow()

    fun sendIntent(intent: LauncherIntent) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }
}