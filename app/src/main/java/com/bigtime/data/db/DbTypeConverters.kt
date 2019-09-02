

package com.bigtime.data.db

import androidx.room.TypeConverter
import com.bigtime.utils.logger.Log

/**
 * Created by Shijil Kadambath on 03/08/2018
 *Email : shijilkadambath@gmail.com
 */

object DbTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Log.e(ex.message, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}
