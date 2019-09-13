package com.example.apiserver.rest.server

import com.example.apiserver.domain.model.InputItem
import com.example.apiserver.domain.service.ItemService
import com.example.apiserver.swagger.ItemsApi
import com.example.apiserver.swagger.model.ItemListResponse
import com.example.apiserver.swagger.model.ItemRequest
import com.example.apiserver.swagger.model.ItemResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemsApiRestServer(
        private val itemService: ItemService
) : ItemsApi {

    override fun listItem(): ResponseEntity<ItemListResponse> {
        val itemList = itemService.list()
        val response = ItemListResponse().items(itemList.map {item ->
            ItemResponse()
                    .id(item.id)
                    .name(item.name)
                    .price(item.price.toInt().toBigDecimal())
                    .createdAt(item.createdAt)
                    .updatedAt(item.updatedAt)
        })

        return ResponseEntity<ItemListResponse>(
                response, HttpStatus.OK
        )
    }

    override fun getItem(itemId: String?): ResponseEntity<ItemResponse> {
        val item = itemService.get(itemId!!)

        if (item == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val response = ItemResponse()
                .id(item.id)
                .name(item.name)
                .price(item.price.toInt().toBigDecimal())
                .createdAt(item.createdAt)
                .updatedAt(item.updatedAt)

        return ResponseEntity<ItemResponse>(response, HttpStatus.OK)
    }

    override fun createItem(item: ItemRequest?): ResponseEntity<ItemResponse> {

        if (item == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val item = itemService.create(InputItem(null, item.name, item.price))

        val response = ItemResponse()
                .id(item.id)
                .name(item.name)
                .price(item.price.toInt().toBigDecimal())
                .createdAt(item.createdAt)
                .updatedAt(item.updatedAt)

        return ResponseEntity<ItemResponse>(response, HttpStatus.CREATED)
    }

    override fun updateItem(itemId: String?, item: ItemRequest?): ResponseEntity<ItemResponse> {

        if (item == null || itemId == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val item = itemService.update(InputItem(itemId, item.name, item.price))

        val response = ItemResponse()
                .id(item.id)
                .name(item.name)
                .price(item.price.toInt().toBigDecimal())
                .createdAt(item.createdAt)
                .updatedAt(item.updatedAt)

        return ResponseEntity<ItemResponse>(response, HttpStatus.OK)
    }

    override fun deleteItem(itemId: String?): ResponseEntity<Void> {

        if (itemId == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        itemService.delete(itemId)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}