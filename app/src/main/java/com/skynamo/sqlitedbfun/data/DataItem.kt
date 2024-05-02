package com.skynamo.sqlitedbfun.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "dataitem")
data class DataItem (
    @DatabaseField(generatedId = true)
    val id: Int? = null,
    @DatabaseField
    val username: String,
    @DatabaseField
    val email: String,
    @DatabaseField
    val age: Int,
    @DatabaseField
    val isActive: Boolean
) {
    // No-arg constructor required by ORMLite
    constructor() : this(null, "", "", 0, false)
}