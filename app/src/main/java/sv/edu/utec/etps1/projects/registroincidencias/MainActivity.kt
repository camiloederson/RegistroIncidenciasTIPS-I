package sv.edu.utec.etps1.projects.registroincidencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import sv.edu.utec.etps1.projects.registroincidencias.ui.theme.RegistroIncidenciasTheme

// Modelo de datos para la incidencia
data class Incidencia(
    val titulo: String,
    val descripcion: String,
    val fechaHora: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroIncidenciasTheme {
                AppNavegacion()
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    // Lista compartida en memoria
    val listaIncidencias = remember { mutableStateListOf<Incidencia>() }
    // Estado para controlar la vista actual: true = Registro, false = Lista
    var verPantallaRegistro by remember { mutableStateOf(true) }

    if (verPantallaRegistro) {
        PantallaRegistro(
            onGuardarIncidencia = { nuevaIncidencia ->
                listaIncidencias.add(0, nuevaIncidencia) // Agrega al inicio
            },
            onIrALista = { verPantallaRegistro = false }
        )
    } else {
        PantallaListaIncidencias(
            incidencias = listaIncidencias,
            onIrARegistro = { verPantallaRegistro = true }
        )
    }
}

@Composable
fun PantallaRegistro(
    onGuardarIncidencia: (Incidencia) -> Unit,
    onIrALista: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Incidencias", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de agregar tarea / incidencia
        Button(
            onClick = {
                if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                    // Genera fecha y hora automática
                    val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
                    onGuardarIncidencia(Incidencia(titulo, descripcion, fechaActual))
                    mensaje = "Incidencia registrada con éxito"
                    titulo = ""
                    descripcion = ""
                } else {
                    mensaje = "Por favor complete todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar incidencia")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Link / Botón para ir a la lista de incidencias
        TextButton(onClick = onIrALista) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Ver lista de incidencias")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = mensaje, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PantallaListaIncidencias(
    incidencias: List<Incidencia>,
    onIrARegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Historial de Incidencias", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (incidencias.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("No hay incidencias registradas.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(incidencias) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = item.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = item.descripcion, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Fecha: ${item.fechaHora}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onIrARegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a registrar")
        }
    }
}