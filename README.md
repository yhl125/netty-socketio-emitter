# netty-socketio-emitter

This project uses [jackson-dataformat-msgpack][jackson-dataformat-msgpack] and [redisson][redisson]

## Usage

### emit

```kotlin
val redissonClient = Redisson.create(redisConfig)
val emitter = Emitter(redissonClient)

// sending to all clients in "testRoom" room
emitter.to("testRoom").emit("testEvent", Any)
emitter.to("testRoom").emit("testEvent", ByteArray)

// sending to all clients in 'test' namespace
emitter.of("test").emit("testEvent", /* ... */)

// sending to all clients in 'test' namespace in "testRoom" room
emitter.of("test").to("testRoom").emit("testEvent", /* ... */)
```

### listener

#### socketIOServer uses [netty-socketio][netty-socketio]

```kotlin
redissonClient.getPatternTopic("socket.io#/#*").addListener(ByteArray::class.java) { _, _, msg ->
    val packet = ObjectMapper(MessagePackFactory()).readValue(msg, Packet::class.java)
    packet.rooms.forEach { room ->
        socketIOServer
            .getNamespace(packet.namespace)
            .getRoomOperations(room)
            .sendEvent(packet.event, packet.data)
    }
}
```

## Gradle(kts)

```
repositories {
	...
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	implementation("com.github.yhl125:netty-socketio-emitter:0.1.2")
}
```

## Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

```
<dependency>
    <groupId>com.github.yhl125</groupId>
    <artifactId>netty-socketio-emitter</artifactId>
    <version>0.1.2</version>
</dependency>
```

[jackson-dataformat-msgpack]: https://github.com/msgpack/msgpack-java/tree/develop/msgpack-jackson

[redisson]: https://github.com/redisson/redisson

[netty-socketio]: https://github.com/mrniko/netty-socketio