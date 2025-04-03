package com.jetsynthesys.rightlife.ai_package.model

data class ModuleResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: List<ModuleData>
)
data class ModuleData(
    val _id: String,
    val moduleName: String,
    val moduleType: String,
    val moduleId: String,
    val categoryId: String,
    val subtitle: String,
    val userId: String,
    val isSelectedModule: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
