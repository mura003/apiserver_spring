package com.example.apiserver.domain.service

import com.example.apiserver.domain.model.InputItem
import com.example.apiserver.domain.model.Item
import com.example.apiserver.domain.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemService(private val repository: ItemRepository) {

    fun create(inputItem: InputItem): Item {
        return repository.create(inputItem)
    }

    fun update(inputItem: InputItem): Item {

        return repository.update(inputItem)
    }

    fun delete(id: String) {
        repository.delete(id)
    }

    fun get(id: String): Item? {
        return repository.get(id)
    }

    fun list(): List<Item> {
        return repository.list()
    }
}