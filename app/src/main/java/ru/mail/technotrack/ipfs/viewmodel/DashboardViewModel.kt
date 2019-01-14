package ru.mail.technotrack.ipfs.viewmodel

import androidx.lifecycle.ViewModel
import ru.mail.technotrack.ipfs.ui.fragment.State

class DashboardViewModel : ViewModel() {

    internal lateinit var state: State

    fun getState(): State {
        if (!::state.isInitialized) {
            setState(State.START)
        }
        return state
    }

    fun setState(state: State) {
        this.state = state
    }
}
