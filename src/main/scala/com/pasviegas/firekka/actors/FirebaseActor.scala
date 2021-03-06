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

import akka.actor.{ Actor, ActorLogging }
import com.firebase.client.{ DataSnapshot, Firebase }
import com.pasviegas.firekka.actors.firebase.FirebaseEventListener
import com.pasviegas.firekka.actors.support.FirebaseActorCreator

abstract class FirebaseActor(firebase: Firebase)
    extends Actor with ActorLogging {

  private[this] val eventListener = FirebaseEventListener(self)

  override def preStart(): Unit = firebase.addChildEventListener(eventListener)

  override def postStop(): Unit = firebase.removeEventListener(eventListener)

  protected def attachChildren[T <: FirebaseActor](ds: DataSnapshot, factory: FirebaseActorCreator[T]) =
    context.actorOf(FirebaseRootActor.props(firebase.child(ds.getKey), factory.create), ds.getKey)

  protected def logEvent(event: String, ds: DataSnapshot): Unit =
    logEvent(event)(ds.getValue)

  protected def logEvent[T](event: String)(value: T): Unit =
    log.info(s"${event.capitalize}($value)")
}

