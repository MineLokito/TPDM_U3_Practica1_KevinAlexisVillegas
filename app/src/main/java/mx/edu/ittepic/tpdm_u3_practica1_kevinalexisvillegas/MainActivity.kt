package mx.edu.ittepic.tpdm_u3_practica1_kevinalexisvillegas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var spiner:Spinner?=null
    var insertar: Button?=null
    var pagado: RadioButton?=null
    var descrip: EditText?=null
    var monto: EditText?=null
    var fecha:EditText?=null
    var baseRemota = FirebaseFirestore.getInstance()
    var registros= ArrayList<String>()
    var keys =ArrayList<String>()
    var id = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        insertar=findViewById(R.id.boton)
        pagado=findViewById(R.id.Pagado)
        descrip=findViewById(R.id.Descripcion)
        monto=findViewById(R.id.Monto)
        fecha=findViewById(R.id.Fecha)

        insertar?.setOnClickListener {
            //Metodo de como insertar datos
            var datosInsertar= hashMapOf(
                "Pagado" to pagado?.isChecked.toString().toBoolean(),
                "Descripcion" to  descrip?.text.toString(),
                "Monto" to monto?.text.toString().toDouble(),
                "Fecha" to fecha?.text.toString()


            )

            baseRemota.collection("ReciboPagos")
                .add(datosInsertar)
                .addOnSuccessListener {

                    //Si se ejecuta este es que si se pudo
                    Toast.makeText(this, "Si se inserto con Exito", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{
                    //si se ejecuta este no se pudo
                    it.message
                    Toast.makeText(this, "Error al Insertar", Toast.LENGTH_LONG).show()

                }
            limpiarcampos()
        }

        baseRemota.collection("ReciboPagos")
            .addSnapshotListener { querySnapshot, e ->
                if (e!=null){
                    Toast.makeText(this,"No hay acceso a los datos",Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                registros.clear()
                keys.clear()
                for (documents in querySnapshot!!){
                    var cadena= "${"Descripcion: "+ documents.getString("Descripcion")}\n${"Fecha : "+documents.getString("Fecha")}\n${"Monto : "+documents.getDouble("Monto")}"+
                            "\n${"Pagado: "+documents.getBoolean("Pagado")}"
                    registros.add(cadena)
                    keys.add(documents.id)
                }
                if(registros.size==0){
                    registros.add("no hay datos Capturados")
                }
                var adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,registros)
                lista.adapter=adapter
            }

        lista.setOnItemClickListener { parent, view, position, id ->
            AlertDialog.Builder(this).setTitle("Atencion").setMessage("Que desea hacer con\n ${registros.get(position)}?")
                .setPositiveButton("Eliminar"){dialogInterface, wich ->
                    baseRemota.collection("ReciboPagos").document(keys.get(position)).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this,"se pudo eliminar",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"No se pudo Eliminar",Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Actualizar"){dialogInterface, wich ->

                    var nuevaVentana =
                        Intent(this, Main2Activity::class.java)
                    nuevaVentana.putExtra("id",keys.get(position) )
                    startActivity(nuevaVentana)
                }
                .setNeutralButton("Cancelar"){dialogInterface, wich ->  }
                .show()
        }

    }

    fun limpiarcampos() {
        descrip?.setText("")
        monto?.setText("")
        fecha?.setText("")
    }
    }




