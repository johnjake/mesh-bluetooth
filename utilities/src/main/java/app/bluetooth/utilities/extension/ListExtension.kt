package app.bluetooth.utilities.extension

import androidx.room.TypeConverter
import app.bluetooth.utilities.EMPTY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@TypeConverter
inline fun <reified T : Any?> T.castToJson(): String {
    return Gson().toJson(this) ?: EMPTY
}

@TypeConverter
inline fun <reified T : Any?> List<T>.castToString(): String {
    require(this.isNotEmpty()) { THROW_LIST_EXCEPTION }
    return Gson().toJson(this)
}

@TypeConverter
inline fun <reified T : Any?> String.castToList(): List<T> {
    require(this.isNotEmpty()) { THROW_STRING_EXCEPTION }
    return Gson().fromJson(this, object : TypeToken<List<T?>?>() {}.type)
}

@TypeConverter
inline fun <reified T : Any?> String.castToMutableList(): MutableList<T> {
    require(this.isNotEmpty()) { THROW_STRING_EXCEPTION }
    return Gson().fromJson(this, object : TypeToken<MutableList<T?>?>() {}.type)
}

@TypeConverter
inline fun <reified T : Any?> String.castToClass(): T? {
    require(this.isNotEmpty()) { THROW_LIST_EXCEPTION }
    return Gson().fromJson(this, T::class.java) ?: null
}

inline fun <reified T : Any> List<T>.castToListUpdate(newValue: T, block: (T) -> Boolean): List<T> {
    require(this.isNotEmpty()) { THROW_LIST_EXCEPTION }
    return map {
        if (block(it)) newValue else it
    }
}

@TypeConverter
fun String.castToStringList(): List<String> {
    require(this.isNotEmpty()) { THROW_STRING_EXCEPTION }
    return this.split(",").toList()
}

const val THROW_LIST_EXCEPTION = "Not a valid Array!"
const val THROW_STRING_EXCEPTION = "Parameter must not be empty!"
