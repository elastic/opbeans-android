package co.elastic.apm.opbeans.app.data.models

import java.util.Date

data class Order(
    val id: Int,
    val customerName: String,
    val createdAt: Date,
    val displayDate: String
)