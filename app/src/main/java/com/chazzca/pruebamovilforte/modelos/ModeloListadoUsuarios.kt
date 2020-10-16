package com.chazzca.pruebamovilforte.modelos

data class ModeloListadoUsuarios(val clienteId: Int,
                          val nombreCompleto: String,
                          val correoElectronico: String,
                          val edad: Int,
                          val limiteCredito: Double,
                          val estatusClienteId: Int,
                          val estatusCliente: String)