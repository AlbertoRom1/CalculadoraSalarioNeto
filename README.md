Calculadora de salario neto

Actividad 1 - Programacion multimedia y dispositivos moviles

Alberto Romero Martin


Aplicaion android en Kotlin que calcula el salario neto a partir de los datos de la persona al usarla. Los calculos son simulados

Se compone de:
-MainActivity.kt - Vista 1: Formulario
-ResultadoActivity.kt - Vista 2, los reultados 
-CalculadoraNomina.kt / Clases de datos y funcion de calculo


El calculo de manera simulada toma el porcentaje base de IRP segun tramo de salario del 2% al 30%,
las deducciones en puntos por hijos y discapacidad por estar casado y por tener 65 años o mas,
seguridad social que compone el 6.35% del bruto.
Y el neto es igual al salario bruto - el IRPF - la Seguridad social.