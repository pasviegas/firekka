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

import com.firebase.client.{ DataSnapshot, Firebase }

import scala.language.implicitConversions
import scala.util.Try

package object state {

  class StateRootNotFound() extends Exception

  trait DataSnapshotDecoder[T] {
    def decode(ds: DataSnapshot): Try[T]
  }

  class FirebaseOps(firebase: Firebase) {
    def setState(cc: Product): Unit =
      firebase.setValue(State(cc))

    def pushChild(cc: Product): Unit =
      firebase.push().setValue(State(cc))
  }

  implicit def firebaseOps(firebase: Firebase): FirebaseOps = {
    new FirebaseOps(firebase)
  }
}
