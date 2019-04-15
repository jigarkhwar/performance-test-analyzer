package ru.tinkoff.pta

final case class AppConfig(schema: SchemaCfg)

final case class SchemaCfg(host: String, port: Int)
