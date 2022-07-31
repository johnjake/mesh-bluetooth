package app.bluetooth.utilities.extension

import androidx.room.TypeConverter
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@TypeConverter
fun <T : Any> castToMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(obj)?.let { value ->
            when {
                value::class.isData -> castToMap(value)
                else -> value
            }
        }
    }
}

@TypeConverter
fun <T : Any> castToMapConstructor(obj: T): Map<String, Any?> {
    val kClass = obj::class as KClass<T>
    val constructorPropName = kClass.primaryConstructor?.parameters?.map { it.name } ?: run {
        return castToMap(obj)
    }
    return kClass.memberProperties.mapNotNull { prop ->
        prop.name.takeIf { name -> name in constructorPropName }?.let { cons ->
            cons to prop.get(obj)?.let { value ->
                if (value::class.isData) {
                    castToMap(obj)
                } else value
            }
        }
    }.toMap()
}