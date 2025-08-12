package com.sangyoon.userapp.model

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val products: List<Products> = emptyList()
)

data class Products(
    val id: Long? = null,
    val name: String,
    val price: Double
)