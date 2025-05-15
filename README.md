# Example Usage

This repository exists as an example of what a typical workload for 
my [thesis project](https://github.com/UnfamousThomas/thesis-initial) would look like.

## Structure
In `./server/` You can see a simple Minestom Minecraft server implementation,
that by default blocks deletion for a few minutes.

In `./service/` You have some metric based scaling logic that can be used by a Game Autoscaler.

And finally, in `./deployments/` you can see matching deployments for the created application.

## Server
The **server** module is where the **server** is actually implemented. It is implemented using [Minestom](https://minestom.net/) in Java. It's main functionalities are:
- Asks the [Sidecar](https://unfamousthomas.github.io/thesis-initial/sidecar/) every 5 seconds on a separate thread if the **Servers** shutdown has been requested
- If shutdown requested, uses [Minestom Events](https://minestom.net/docs/feature/events) to publish an event globally that can be listened to.
- Implements a simple Listener for the event, that notifies all online players and console that shutdown is requested
  - Periodically checks if **server** has no players, and if so, tells the sidecar that **server** is allowed to shut down
  - A minute or so after shutdown is requested, kicks all online players off from the **Server**
- Periodically collects **server** Metrics and sends them to the REST api provided at `SERVER_ADDRESS`

## Service
The **service** is used to report metrics, and based on them to scale up or down. The metric reporting logic itself is done as part of the Server module.
The basic algorithm in `service/internals/metric/metric_calculations.go` tries to have 20% of server slots free (presuming every server can have 2 players, which is not really a limit) as well as a buffer of 10 slots.
This is just to implement a simple Service as an example so that a user can implement better ones based on it.

## Deployments
The deployment file contains some simple Kubernetes manifests for deploying the above system. It contains a `GameType`, a service for accessing the game pods created by the `GameType`, a `GameAutoscaler`, a deployment of the scaling service, and a ClusterIP to access the service