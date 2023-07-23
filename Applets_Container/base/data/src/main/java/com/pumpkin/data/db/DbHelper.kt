package com.pumpkin.data.db

import android.app.Application

/**
 * db entrance
 */
object DbHelper {

    fun providesGameDao(application: Application) = MyDataBase.getDataBase(application).gameDao()


}