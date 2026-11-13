package com.alberto.calculadorasalarioneto


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alberto.calculadorasalarioneto.ui.theme.CalculadoraSalarioNetoTheme
import android.content.Intent
import androidx.compose.runtime.saveable.rememberSaveable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
            )

        setContent {
            CalculadoraSalarioNetoTheme{
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioScreen(
                        modifier = Modifier.padding(innerPadding),
                        onCalcular = { datos ->
                            val intent = Intent(this@MainActivity, ResultadoActivity::class.java)
                            intent.putExtra("salarioBrutoAnual", datos.salarioBrutoAnual)
                            intent.putExtra("numeroPagas", datos.numeroPagas)
                            intent.putExtra("edad", datos.edad)
                            intent.putExtra("grupoProfesional", datos.grupoProfesional)
                            intent.putExtra("gradoDiscapacidad", datos.gradoDiscapacidad)
                            intent.putExtra("estadoCivil", datos.estadoCivil)
                            intent.putExtra("numeroHijos", datos.numeroHijos)
                            startActivity(intent)
                         }
                        )
                    }
                }
            }
        }
}

@Composable
fun FormularioScreen(modifier: Modifier = Modifier, onCalcular: (DatosNomina) -> Unit) {
    //Esttado de cada campo del formulario

    var salarioTexto by rememberSaveable { mutableStateOf("") }
    var numeroPagas by rememberSaveable { mutableStateOf("14") }
    var edad by rememberSaveable { mutableFloatStateOf(30f) }
    var grupoProfesional by rememberSaveable { mutableStateOf("Técnico") }
    var discapacidad by rememberSaveable { mutableStateOf("Ninguna") }
    var estadoCivil by rememberSaveable { mutableStateOf("Soltero/a") }
    var numeroHijos by rememberSaveable { mutableIntStateOf(0) }
    var mensajeError by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Text("Calculadora de salario neto", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = salarioTexto,
            onValueChange = { salarioTexto = it },
            label = { Text("Salario bruto anual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Numero de pagas")
        SelectorOpciones(listOf("12", "14"), numeroPagas) {numeroPagas = it }

        Text("Edad: ${edad.toInt()} años")
        Slider(value = edad, onValueChange = { edad = it }, valueRange = 16f..70f)

        Text("Grupo profesional")
        SelectorOpciones(listOf("Directivo", "Técnico", "Operario"), grupoProfesional) { grupoProfesional = it }

        Text("Grado de discapacidad")
        SelectorOpciones(listOf("Ninguna", "33 %", "65 %"), discapacidad) { discapacidad = it }

        Text("Estado civil")
        SelectorOpciones(listOf("Soltero/a", "Casado/a"), estadoCivil) { estadoCivil = it }

        Text("Número de hijos")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (numeroHijos > 0) numeroHijos-- }) { Text("-") }
            Text("$numeroHijos", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 24.dp))
            Button(onClick = { numeroHijos++ }) { Text("+") }

        }

        if (mensajeError != "") {
            Text(mensajeError, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val salario = salarioTexto.replace(",", ".").toDoubleOrNull()
                if (salario == null || salario <= 0) {
                    mensajeError = "Introduce un salario bruto valido"
                } else {
                    mensajeError = ""
                    val gradoDiscapacidad = when (discapacidad) {
                        "33 %" -> 33
                        "65 %" -> 65
                        else -> 0
                    }
                    onCalcular(
                        DatosNomina(
                            salarioBrutoAnual = salario,
                            numeroPagas = numeroPagas.toInt(),
                            edad = edad.toInt(),
                            grupoProfesional = grupoProfesional,
                            gradoDiscapacidad = gradoDiscapacidad,
                            estadoCivil = estadoCivil,
                            numeroHijos = numeroHijos

                        )
                    )


                }
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}

//Fila de botones donde solo uno esta seleccionado

@Composable
fun SelectorOpciones(opciones: List<String>, seleccionada: String, onSeleccion: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        for (opcion in opciones) {
            if (opcion == seleccionada) {
                Button(onClick = { onSeleccion(opcion) }, modifier = Modifier.weight(1f)) { Text(opcion) }
            } else {
                OutlinedButton(onClick = { onSeleccion(opcion) }, modifier = Modifier.weight(1f)) { Text(opcion) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioPreview() {
    CalculadoraSalarioNetoTheme {
        FormularioScreen(onCalcular = {})
    }
}