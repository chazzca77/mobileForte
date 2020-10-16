package com.chazzca.pruebamovilforte.modelos

data class ModeloCliente(val clienteId: Int,
                                 val nombreCompleto: String,
                                 val rfc: String,
                                val fechaNacimiento: String,
                                 val correoElectronico: String,
                                 val telefonoMovil: String,
                                 val domicilio: String,
                                 val limiteCredito: Double,
                                 val estatusClienteId: Int)