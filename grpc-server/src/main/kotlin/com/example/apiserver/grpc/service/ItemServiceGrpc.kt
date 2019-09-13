package com.example.apiserver.grpc.service

import com.example.apiserver.domain.model.InputItem
import com.example.apiserver.domain.service.ItemService
import com.example.apiserver.grpc.proto.service.*
import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import com.salesforce.grpc.contrib.spring.GrpcServerFactory
import com.salesforce.grpc.contrib.spring.GrpcServerHost
import com.salesforce.grpc.contrib.spring.GrpcService
import com.salesforce.grpc.contrib.spring.SimpleGrpcServerFactory
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.protobuf.services.ProtoReflectionService
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@GrpcService
class ItemServiceGrpc(private val itemService: ItemService) : ReactorItemProtoServiceGrpc.ItemProtoServiceImplBase() {

    @Bean(initMethod = "start")
    fun grpcServerHost(): GrpcServerHost {

        return GrpcServerHost(50051)
    }

    @Bean
    fun grpcServerFactory(): GrpcServerFactory {
        return ReflectableGrpcServerFactory()
    }
    override fun createItem(request: Mono<ItemCreateRequest>?): Mono<ItemResponse> {
        return request!!.map {
            val item = itemService.create(
                    InputItem(null, it.name, it.price)
            )

            val grpcResponse = ItemResponse.newBuilder()
            grpcResponse.id = item.id
            grpcResponse.name = item.name
            grpcResponse.price = item.price.toInt()
            grpcResponse.createdAt = toTimestampBuilder(item.createdAt)
            grpcResponse.updatedAt = toTimestampBuilder(item.updatedAt)

            grpcResponse.build()
        }
    }

    override fun listItem(request: Mono<Empty>?): Mono<ItemListResponse> {
        return request!!.map {
            val grpcResponse = ItemListResponse.newBuilder()

            val itemList = itemService.list()

            itemList.forEach {
                val itemResponse = ItemResponse.newBuilder()
                itemResponse.id = it.id
                itemResponse.name = it.name
                itemResponse.price = it.price.toInt()
                itemResponse.createdAt = toTimestampBuilder(it.createdAt)
                itemResponse.updatedAt = toTimestampBuilder(it.updatedAt)

                grpcResponse.addItemList(itemResponse)
            }

            grpcResponse.build()
        }
    }

    override fun getItem(request: Mono<ItemGetRequest>?): Mono<ItemResponse> {
        return request!!.map {
            val item = itemService.get(it.id)

            if (item == null) {
                throw Status.NOT_FOUND.asRuntimeException()
            }

            val grpcResponse = ItemResponse.newBuilder()
            grpcResponse.id = item.id
            grpcResponse.name = item.name
            grpcResponse.price = item.price.toInt()
            grpcResponse.createdAt = toTimestampBuilder(item.createdAt)
            grpcResponse.updatedAt = toTimestampBuilder(item.updatedAt)

            grpcResponse.build()
        }
    }

    override fun updateItem(request: Mono<ItemUpdateRequest>?): Mono<ItemResponse> {
        return request!!.map {
            val item = itemService.update(
                    InputItem(it.id, it.name, it.price)
            )

            val grpcResponse = ItemResponse.newBuilder()
            grpcResponse.id = item.id
            grpcResponse.name = item.name
            grpcResponse.price = item.price.toInt()
            grpcResponse.createdAt = toTimestampBuilder(item.createdAt)
            grpcResponse.updatedAt = toTimestampBuilder(item.updatedAt)

            grpcResponse.build()
        }
    }

    override fun deleteItem(request: Mono<ItemDeleteRequest>?): Mono<Empty> {
        return request!!.map {
            itemService.delete(it.id)
            Empty.newBuilder().build()
        }
    }

    fun toTimestampBuilder(localDateTime: LocalDateTime, timezone: String = "UTC"): Timestamp {
        val zoneOffset = ZoneId.of(timezone).rules.getOffset(Instant.EPOCH)
        return Timestamp.newBuilder().setSeconds(localDateTime.toEpochSecond(zoneOffset)).setNanos(localDateTime.nano).build()
    }

    class ReflectableGrpcServerFactory : SimpleGrpcServerFactory() {
        override protected fun setupServer(builder: ServerBuilder<*>) {
            super.setupServer(builder)
            builder.addService(ProtoReflectionService.newInstance())
        }
    }
}