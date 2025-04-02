package com.jetsynthesys.rightlife.ai_package.model

data class AddToolRequest(
    var toolId :String?,
    var userId : String?,
    var moduleName : String?,
    var moduleId : String?,
    var subtitle : String?,
    var moduleType : String?,
    var categoryId : String?,
    var isSelectedModule : Boolean?,
)