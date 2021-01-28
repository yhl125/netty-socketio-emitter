package emitter

import com.fasterxml.jackson.databind.ObjectMapper
import emitter.protocol.Packet
import org.msgpack.jackson.dataformat.MessagePackFactory
import org.redisson.api.RedissonClient

class Emitter(private val redis: RedissonClient) {

    private var key: String = "socket.io#"
    private var rooms = mutableListOf<String>()
    private var namespace: String? = null

    /**
     * Limit emission to a certain `room`.
     *
     * @param {String} room
     */
    fun to(room: String): Emitter {
        if (!this.rooms.contains(room)) {
            this.rooms.add(room)
        }
        return this
    }

    /**
     * Limit emission to certain `namespace`.
     *
     * @param {String} namespace
     */
    fun of(namespace: String): Emitter {
        this.namespace = namespace
        return this
    }

    fun emit(event: String, vararg data: String): Emitter {
        return this.emit(Packet(PacketType.EVENT.value, event, listOf(data)))
    }

    fun emit(event: String, data: Any): Emitter {
        return this.emit(Packet(PacketType.EVENT.value, event, data))
    }

    fun emit(event: String, byteArray: ByteArray): Emitter {
        return this.emit(Packet(PacketType.BINARY_EVENT.value, event, byteArray))
    }

    /**
     * Send the packet.
     */
    private fun emit(packet: Packet): Emitter {
        if (this.namespace.isNullOrEmpty()) {
            packet.nsp = "/"
        } else packet.nsp = this.namespace!!

        packet.rooms = this.rooms

        val messageMapper = ObjectMapper(MessagePackFactory())

        var topic = "${this.key}${this.namespace}#}"
        if (rooms.size == 1) {
            topic += "${this.rooms.first()}#"
        }

        redis.getTopic(topic).publishAsync(messageMapper.writeValueAsBytes(packet))
        // reset state
        this.rooms = mutableListOf()
        this.key = "socket.io#"
        this.namespace = null
        return this
    }
}