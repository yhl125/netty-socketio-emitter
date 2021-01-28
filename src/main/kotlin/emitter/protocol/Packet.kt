package emitter.protocol

data class Packet(
    var type: Int = 2,
    val event: String = "",
    val data: Any? = null,
    var nsp: String = "/",
    var rooms: List<String> = listOf()
)