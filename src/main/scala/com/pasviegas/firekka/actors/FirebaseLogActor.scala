// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.pasviegas.firekka.actors

import akka.actor.Props
import com.firebase.client.{ DataSnapshot, Firebase }
import com.pasviegas.firekka.actors.firebase.FirebaseEvents.{ Added, Changed, Removed }

class FirebaseLogActor(firebase: Firebase) extends FirebaseActor(firebase) {

  var content = Map[String, String]()

  override def receive: Receive = {
    case Added(ds)   => cacheAndPrint("added", ds)
    case Removed(ds) => cacheAndPrint("removed", ds)
    case Changed(ds) => cacheAndPrint("changed", ds)
    case other       => unhandled(other)
  }

  override def postStop(): Unit = {
    log.info(s"is dying")
    super.postStop()
  }

  private[this] def cacheAndPrint(event: String, ds: DataSnapshot) {
    content += (ds.getKey -> ds.getValue(true).toString)
    logEvent(event, ds)
  }

}

object FirebaseLogActor {
  def props(firebase: Firebase): Props = Props(new FirebaseLogActor(firebase))
}
