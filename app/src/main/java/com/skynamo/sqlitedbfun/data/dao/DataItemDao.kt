package com.skynamo.sqlitedbfun.data.dao

import android.content.Context
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.stmt.UpdateBuilder
import com.skynamo.sqlitedbfun.data.DataItem

class DataItemDao(context: Context) {

    val databaseHelper: DatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper::class.java)
    private val dataItemDao: Dao<DataItem, Int> = DaoManager.createDao(databaseHelper.connectionSource, DataItem::class.java)

    fun createDataItem(user: DataItem) {
        dataItemDao.create(user)
    }

    fun getAllDataItems(): List<DataItem> {
        return dataItemDao.queryForAll()
    }

    fun deleteUser(user: DataItem) {
        dataItemDao.delete(user)
    }

    fun updateBuilder() : UpdateBuilder<DataItem, Int> {
        return dataItemDao.updateBuilder()
    }

    fun close() {
        OpenHelperManager.releaseHelper()
    }
}