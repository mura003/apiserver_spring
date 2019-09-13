package com.example.apiserver.domain.repository

import com.example.apiserver.domain.model.InputItem
import com.example.apiserver.domain.model.Item
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class ItemRepository {
    // とりあえずInMemory
    val repository = mutableMapOf<String, Item>()

    fun get(id: String): Item? {
        return repository[id]
    }

    fun create(inputItem: InputItem): Item {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID().toString()
        val item = Item(
                id,
                inputItem.name,
                inputItem.price,
                now, now
        )
        repository[id] = item

        return item
    }

    fun update(inputItem: InputItem): Item {
        val now = LocalDateTime.now()
        var target = repository[inputItem.id]
        var item: Item? = null

        if (target == null) {
            item = Item(
                    inputItem.id!!, inputItem.name, inputItem.price, now, now
            )
        } else {
            item = Item(
                    inputItem.id!!, inputItem.name, inputItem.price, target.createdAt, now
            )
        }

        repository[item.id] = item

        return item
    }

    fun list(): List<Item> {
        return repository.map { it.value }
    }

    fun delete(id: String) {
        repository.remove(id)
    }
}