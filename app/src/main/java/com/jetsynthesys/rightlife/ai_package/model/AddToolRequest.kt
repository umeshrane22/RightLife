package com.jetsynthesys.rightlife.ai_package.model

data class AddToolRequest(
    var moduleId : String?,
    var title : String?,
    var moduleType : String?,
    var isSelectedModule : Boolean?,
)
