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

package com.pasviegas.firekka

import akka.actor.ActorSystem
import com.firebase.client.Firebase
import com.pasviegas.firekka.FirebaseRootActor.props
import com.typesafe.config.{ Config, ConfigFactory }

sealed class Firekka(actorSystem: ActorSystem, firebase: Firebase) {

  def attachRoot[T <: FirebaseActor](factory: FirebaseActorCreator[T]): Firekka = {
    actorSystem.actorOf(props(firebase, factory.create))
    this
  }

  def attachRoot[T <: FirebaseActor](child: String, factory: FirebaseActorCreator[T]): Firekka = {
    actorSystem.actorOf(props(firebase.child(child), factory.create), child)
    this
  }
}

object Firekka {
  val firebaseUri: String = "akka.firekka.firebase.root"

  def config(url: String): Config = ConfigFactory.parseString(s"""${Firekka.firebaseUri}="$url"""")

  def system(name: String, config: Config): Firekka = {
    if (!config.hasPath(firebaseUri)) {
      throw new RuntimeException("where is the firebase root?")
    }

    new Firekka(ActorSystem(name, config), new Firebase(config.getString(Firekka.firebaseUri)))
  }
}
