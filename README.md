firekka
=======

ultimate firebase -> akka integration :O

```scala
object Main extends App {

  //this actor logs events (Added, Removed, Changed) and the corresponding values
  //change to what ever FirebaseActor you implement
  val logger = FirebaseActorCreator(FirebaseLogActor.props)

  val config = ConfigFactory.load()
    .withFallback(Firekka.config("https://my-firebase.firebaseio.com/"))

  Firekka.system("my-system", config)
    .attachRoot("users", logger)
    .attachRoot("posts", logger)

}
````
Enjoy! :)
