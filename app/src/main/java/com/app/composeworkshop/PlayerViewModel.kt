package com.app.composeworkshop

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(private val navHostController: NavHostController) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun onAction(uiAction: UiAction) {
        val currentState = _viewState.value
        when (uiAction) {
            is UiAction.LifecycleActionPause -> {
                _viewState.value = currentState.copy(isPlaying = false)
            }
            is UiAction.LifecycleActionResume -> {
                _viewState.value = currentState.copy(isPlaying = true)
            }
            is UiAction.SavePlaybackPosition -> {
                _viewState.value = currentState.copy(playbackPosition = uiAction.position)
            }
            is UiAction.PlayerClick -> {
                _viewState.value = currentState.copy(isManuallyPaused = !currentState.isManuallyPaused)
            }
            is UiAction.EffectsClick -> {
                navHostController.navigate(
                    "${Destinations.EFFECTS}/${uiAction.videoProgress}?duration=${uiAction.duration}")
            }
        }
    }

    @Immutable
    data class ViewState(
        val isManuallyPaused: Boolean = false,
        val isPlaying: Boolean = true,
        val playbackPosition: Long = 0
    )

    sealed class UiAction {
        class EffectsClick(val videoProgress: Long, val duration: Long) : UiAction()
        class SavePlaybackPosition(val position: Long) : UiAction()
        object PlayerClick : UiAction()
        object LifecycleActionResume : UiAction()
        object LifecycleActionPause : UiAction()
    }
}