package br.barizza.barizzaminigames.protectthechicken.container

import br.barizza.barizzaminigames.protectthechicken.entity.EntityHolder

object ChickenOwnersContainer {
    val connectedList = mutableListOf<EntityHolder>()
    val disconnectedList = mutableListOf<EntityHolder>()
}