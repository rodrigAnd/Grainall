package com.fiap.grainall.feature.alimento

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fiap.grainall.R

class EditingScreen : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextPhoto: EditText
    private lateinit var buttonEdit: Button
    private lateinit var buttonSend: Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização dos componentes da UI
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextAge = findViewById(R.id.editTextAge)
        editTextPhoto = findViewById(R.id.editTextPhoto)
        buttonEdit = findViewById(R.id.buttonEdit)
        buttonSend = findViewById(R.id.buttonSend)
        imageView = findViewById(R.id.imageView)

        // Definindo ação do botão Editar
        buttonEdit.setOnClickListener {
            // Lógica para editar os dados
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val age = editTextAge.text.toString()
            val photoUrl = editTextPhoto.text.toString()

            // Aqui você pode implementar a lógica para manipular os dados editados
            // Por exemplo, você pode exibir uma mensagem de confirmação
            Toast.makeText(this, "Dados editados: Nome=$name, Email=$email, Idade=$age, Foto=$photoUrl", Toast.LENGTH_SHORT).show()
        }

        // Definindo ação do botão Enviar
        buttonSend.setOnClickListener {
            // Lógica para enviar os dados
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val age = editTextAge.text.toString()
            val photoUrl = editTextPhoto.text.toString()

            // Aqui você pode implementar a lógica para enviar os dados para algum lugar
            // Por exemplo, você pode enviar os dados para um servidor
            Toast.makeText(this, "Dados enviados: Nome=$name, Email=$email, Idade=$age, Foto=$photoUrl", Toast.LENGTH_SHORT).show()
        }
    }
}
