package mx.edu.ittepic.tpdm_u3_practica1_kevinalexisvillegas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class Main2Activity : AppCompatActivity() {
    var spiner: Spinner?=null
    var actualizar: Button?=null
    var cancelar: Button?=null
    var Rpagado: RadioButton?=null
    var descrip: EditText?=null
    var monto: EditText?=null
    var fecha: EditText?=null
    var baseRemota = FirebaseFirestore.getInstance()
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        actualizar=findViewById(R.id.actualizar)
        cancelar=findViewById(R.id.regresar)
        Rpagado=findViewById(R.id.APagado)
        descrip=findViewById(R.id.descripcionActualizar)
        monto=findViewById(R.id.MontoActualizar)
        fecha=findViewById(R.id.fechaActualizar)

        id = intent.extras?.getString("id").toString()!!

        baseRemota.collection("ReciboPagos")
            .document(id)
            .get()
            .addOnSuccessListener {

                descrip?.setText(it.getString("Descripcion"))
                fecha?.setText(it.getString("Fecha"))

                monto?.setText(it.getDouble("Monto").toString())
            }
            .addOnFailureListener {
                descrip?.setText("NULL")
                fecha?.setText("NULL")
                monto?.setText("No se encontro dato ")

                descrip?.isEnabled = false
                fecha?.isEnabled = false
                monto?.isEnabled = false
                actualizar?.isEnabled = false
            }
        actualizar?.setOnClickListener {
            var datosActualizar = hashMapOf(
                "Pagado" to Rpagado?.isChecked.toString().toBoolean(),
                "Descripcion" to  descrip?.text.toString(),
                "Monto" to monto?.text.toString().toDouble(),
                "Fecha" to fecha?.text.toString()
            )

            baseRemota.collection("ReciboPagos").document(id)
                .set(datosActualizar)
                .addOnSuccessListener {
                    limpiarcampos()
                    Toast.makeText(this, "Se Actualizo", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error No Se Logro Actualizar", Toast.LENGTH_LONG).show()
                }
        }

        cancelar?.setOnClickListener { finish() }

    }

    fun limpiarcampos() {
        descrip?.setText("")
        monto?.setText("")
        fecha?.setText("")
    }
    }



