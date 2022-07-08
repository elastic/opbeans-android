package co.elastic.apm.opbeans.app.data.source.customer.tools

object LocationBuilder {

    fun build(city: String, country: String): String {
        return "$city, $country"
    }
}