package br.barizza.barizzaminigames.state

object GameState {
    var pluginState: States = States.IDLE
    var protectTheChickenStates: ProtectTheChickenStates = ProtectTheChickenStates.WAITING
    enum class States {
        IDLE,
        PROTECT_THE_CHICKEN,
    }

    enum class ProtectTheChickenStates {
        WAITING
    }
}