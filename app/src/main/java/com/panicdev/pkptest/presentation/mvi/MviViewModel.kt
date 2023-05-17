package com.panicdev.pkptest.presentation.mvi

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

abstract class MviViewModel<ACTION : MviAction, STATE : Any, SIDE_EFFECT : Any>(initialState: STATE) :
    ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    override val container = container<STATE, SIDE_EFFECT>(initialState)

    abstract fun dispatch(action: ACTION)
}
