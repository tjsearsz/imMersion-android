package com.immersion.immersionandroid.domain

data class User(val email: String, val password: String, var isBusinessOwner: Boolean = false)
