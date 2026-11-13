package com.alberto.calculadorasalarioneto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alberto.calculadorasalarioneto.ui.theme.CalculadoraSalarioNetoTheme

class ResultadoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        // Leemos los datos que nos ha enviado MainActivity
        val datos = DatosNomina(
            salarioBrutoAnual = intent.getDoubleExtra("salarioBrutoAnual", 0.0),
            numeroPagas = intent.getIntExtra("numeroPagas", 12),
            edad = intent.getIntExtra("edad", 0),
            grupoProfesional = intent.getStringExtra("grupoProfesional") ?: "",
            gradoDiscapacidad = intent.getIntExtra("gradoDiscapacidad", 0),
            estadoCivil = intent.getStringExtra("estadoCivil") ?: "",
            numeroHijos = intent.getIntExtra("numeroHijos", 0)
        )

        val resultado = calcularNomina(datos)

        setContent {
            CalculadoraSalarioNetoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                        Text("Datos recibidos (prueba)")
                        Text("Bruto anual: ${resultado.salarioBrutoAnual}")
                        Text("Hijos: ${datos.numeroHijos}  Grupo: ${datos.grupoProfesional}")
                        Text("Neto anual: ${resultado.salarioNetoAnual}")
                        Button(onClick = { finish() }) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }
}