package com.nqmgaming.shoseshop.data.model.signUp

data class UserSignUp(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val address: String,
    val phoneNumber: String
)

data class SignUpResponse(
    val data: UserSignUp,
    val accessToken: String
)