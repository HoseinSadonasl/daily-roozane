package com.abc.daily.domain.util

import androidx.room.TypeConverter
import com.abc.daily.domain.model.note.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TypeConvertors {

    @TypeConverter
    fun fromjson(value: String?): List<Item> {
        val listType = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Item>): String? {
        return Gson().toJson(list)
    }

}