package com.example.apiserver.domain.model

import java.time.LocalDateTime

data class Item (
        val id: String,
        val name: String,
        val price: Number,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
)