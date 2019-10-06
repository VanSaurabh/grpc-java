# gRPC-java
This is a simple gRPC project in Java. Several gRPC services have been implemented in java

# Technology stack

```bash
Java 8
Gradle
gRPC
Protocol Buffers
```
# APIs Implemented

```python
1. Unary gRPC: Sum RPC Unary API - The function takes a Request message that 
has two integers, and returns a Response that represents the sum of them.

2. Server Streaming gRPC: PrimeNumberDecomposition RPC Server Streaming API - The
function takes a Request message that has one integer, and returns a stream of 
Responses that represent the prime number decomposition of that number.

3. Client Streaming gRPC: ComputeAverage RPC Client Streaming API - The function
takes a stream of Request message that has one integer, and returns a Response
with a double that represents the computed average.

4. Bi-Directional Streaming gRPC: FindMaximum RPC Bi-Directional Streaming API - The 
function takes a stream of Request message that has one integer, and returns a stream 
of Responses that represent the current maximum between all these integers

```
