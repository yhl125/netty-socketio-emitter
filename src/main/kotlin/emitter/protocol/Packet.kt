package emitter.protocol

data class Packet(
    var type: Int = 2,
    val event: String = "",
    val data: Any? = null,
    var namespace: String = "/",
    var rooms: List<String> = listOf()
)