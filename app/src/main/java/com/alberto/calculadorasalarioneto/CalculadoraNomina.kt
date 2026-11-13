package com.alberto.calculadorasalarioneto

//Datos que introduce el usuario en la primera vista

class DatosNomina(
    val salarioBrutoAnual: Double,
    val numeroPagas: Int,
    val edad: Int,
    val grupoProfesional: String,
    val gradoDiscapacidad: Int,
    val estadoCivil: String,
    val numeroHijos: Int
)


//Resultaddos que mostraremos en las segunda vista
class ResultadoNomina(
    val salarioBrutoAnual: Double,
    val salarioBrutoMensual: Double,
    val porcentajeIrpf: Double,
    val retencionIrpf: Double,
    val seguridadSocial: Double,
    val deducciones: Double,
    val salarioNetoAnual: Double,
    val salarioNetoMensual: Double
)


//Calculo simulado

fun calcularNomina(datos: DatosNomina): ResultadoNomina {
    val bruto = datos.salarioBrutoAnual

    //Porcentaje base de IRPF segun salario

    val porcentajeBase = when {
        bruto < 15000 -> 2.0
        bruto < 20000 -> 8.0
        bruto < 35000 -> 15.0
        bruto < 60000 -> 22.0
        else -> 30.0

    }

    var puntosDeduccion = datos.numeroHijos * 1.0


    puntosDeduccion += when {
        datos.gradoDiscapacidad >= 65 -> 3.0
        datos.gradoDiscapacidad >= 33 -> 2.0
        else -> 0.0
    }

    if (datos.estadoCivil == "Casado/a") puntosDeduccion += 1.0
    if (datos.edad >= 65) puntosDeduccion += 1.0

    //Porcentaje final de IRPF

    var porcentajeIrpf = porcentajeBase - puntosDeduccion
    if (porcentajeIrpf < 2.0) porcentajeIrpf = 2.0


    //Importes en euros

    val retencionIrpf = bruto * porcentajeIrpf / 100
    val deducciones = bruto * (porcentajeBase - porcentajeIrpf) / 100
    val seguridadSocial = bruto * 6.35 / 100
    val netoAnual = bruto - retencionIrpf - seguridadSocial

    return ResultadoNomina(
        salarioBrutoAnual = bruto,
        salarioBrutoMensual = bruto / datos.numeroPagas,
        porcentajeIrpf = porcentajeIrpf,
        retencionIrpf = retencionIrpf,
        seguridadSocial = seguridadSocial,
        deducciones = deducciones,
        salarioNetoAnual = netoAnual,
        salarioNetoMensual = netoAnual / datos.numeroPagas
    )
}