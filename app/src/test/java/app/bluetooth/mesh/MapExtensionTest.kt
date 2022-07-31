package app.bluetooth.mesh

import app.bluetooth.domain.data.Products
import app.bluetooth.utilities.extension.castToMap
import org.junit.Test

class MapExtensionTest {

    @Test
    fun `test object class to map`() {
        val product = Products(
            category = "beverage",
            name = "San Miguel Beer",
            imageUrl = "",
            price = 4.55,
            description = "best beer"
        )
        val convertedMap = castToMap(product)
        println(convertedMap)
    }
}