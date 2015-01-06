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

package com.pasviegas.firekka.actors.firebase

import akka.actor.ActorRef
import com.firebase.client.{ ChildEventListener, DataSnapshot, FirebaseError }
import com.pasviegas.firekka.actors.firebase.FirebaseEvents.{ Removed, Added, Changed }

object FirebaseEventListener {
  def apply(actor: ActorRef): ChildEventListener = new ChildEventListener {
    override def onChildRemoved(dataSnapshot: DataSnapshot): Unit =
      actor ! Removed(dataSnapshot)

    override def onChildAdded(dataSnapshot: DataSnapshot, s: String): Unit =
      actor ! Added(dataSnapshot)

    override def onChildChanged(dataSnapshot: DataSnapshot, s: String): Unit =
      actor ! Changed(dataSnapshot)

    override def onChildMoved(dataSnapshot: DataSnapshot, s: String): Unit = {}

    override def onCancelled(firebaseError: FirebaseError): Unit = {}
  }
}

