package ru.tinkoff.pta.entities

trait Key[A] {
  def key(a: A): Long
  def updateBody(newA: A, oldId: Long): A
  def updateId(a: A, newId: Long): A
  def notFound(id: Long): Throwable
  def multiple(id: Long): Throwable
}

object Key {
  def key[A](a: A)(implicit key: Key[A]): Long                          = key.key(a)
  def updateWithoutId[A](newA: A, oldId: Long)(implicit key: Key[A]): A = key.updateBody(newA, oldId)
  def updateId[A](a: A, newId: Long)(implicit key: Key[A]): A           = key.updateId(a, newId)
  def notFound[A](id: Long)(implicit key: Key[A]): Throwable            = key.notFound(id)
  def multiple[A](id: Long)(implicit key: Key[A]): Throwable            = key.multiple(id)
}
