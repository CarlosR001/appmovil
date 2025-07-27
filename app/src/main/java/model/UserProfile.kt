package com.carlos.movilwordkapp.model

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val imageUrls: List<String> // ANTES: imageUrl: String
)