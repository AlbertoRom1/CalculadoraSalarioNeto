package com.alberto.calculadorasalarioneto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alberto.calculadorasalarioneto.ui.theme.CalculadoraSalarioNetoTheme
import java.util.Locale

class ResultadoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        //Leemos los datos que nos ha enviado MainActivity
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
                    ResultadoScreen(
                        datos = datos,
                        resultado = resultado,
                        onVolver = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultadoScreen(
    datos: DatosNomina,
    resultado: ResultadoNomina,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Resultado de tu nómina",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        //Tarjeta destacada con el salario neto
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Salario neto por paga")
                Text(formatoEuros(resultado.salarioNetoMensual), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("${formatoEuros(resultado.salarioNetoAnual)} al año en ${datos.numeroPagas} pagas")
            }
        }

        //Desglose del salario
        TarjetaInfo(titulo = "Desglose anual") {
            FilaResultado("Salario bruto", formatoEuros(resultado.salarioBrutoAnual))
            FilaResultado("Bruto por paga", formatoEuros(resultado.salarioBrutoMensual))
            FilaResultado("Retención IRPF (${resultado.porcentajeIrpf.toInt()} %)", "- " + formatoEuros(resultado.retencionIrpf))
            FilaResultado("Seguridad Social (6,35 %)", "- " + formatoEuros(resultado.seguridadSocial))
            HorizontalDivider()
            FilaResultado("Salario neto", formatoEuros(resultado.salarioNetoAnual))
        }

        //Deducciones
        TarjetaInfo(titulo = "Deducciones en el IRPF") {
            FilaResultado("Ahorro por deducciones", formatoEuros(resultado.deducciones))
            Text(
                "Se reducen puntos de IRPF por hijos, discapacidad, estar casado/a y tener 65 años o más.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        //Resumen de los datos introducidos
        TarjetaInfo(titulo = "Tus datos") {
            FilaResultado("Edad", "${datos.edad} años")
            FilaResultado("Grupo profesional", datos.grupoProfesional)
            FilaResultado("Estado civil", datos.estadoCivil)
            FilaResultado("Número de hijos", "${datos.numeroHijos}")
            FilaResultado("Discapacidad", "${datos.gradoDiscapacidad} %")
        }

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a calcular")
        }
    }
}

//Tarjeta gris con un título y el contenido que le pasemos
@Composable
fun TarjetaInfo(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(titulo, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            contenido()
        }
    }
}

//Fila con la etiqueta a la izquierda y el valor a la derecha
@Composable
fun FilaResultado(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta)
        Text(valor, fontWeight = FontWeight.Bold)
    }
}

//Convierte 24495.0 en "24.495,00 €"
fun formatoEuros(valor: Double): String {
    return String.format(Locale.forLanguageTag("es-ES"), "%,.2f €", valor)
}

@Preview
@Composable
fun ResultadoPreview() {
    val datos = DatosNomina(30000.0, 14, 35, "Técnico", 0, "Casado/a", 2)
    CalculadoraSalarioNetoTheme {
        Surface {
            ResultadoScreen(datos, calcularNomina(datos), onVolver = {})
        }
    }
}