package com.jetsynthesys.rightlife.ui.settings.pojo

data class Plan(
    val name: String,
    val description: String,
    val price: String,
    var isSelected: Boolean = false
)