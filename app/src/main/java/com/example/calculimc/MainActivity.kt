package com.example.calculimc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)
        val buttonCalculer = findViewById<Button>(R.id.buttonCalculer)
        val buttonEffacer = findViewById<Button>(R.id.buttonEffacer)

        buttonCalculer.setOnClickListener {
            calculerImc()
        }

        buttonEffacer.setOnClickListener {
            effacer()
        }
        savedInstanceState?.let {
            editTextPoids.setText(it.getString("poids", ""))
            editTextTaille.setText(it.getString("taille", ""))
            textViewImc.text = it.getString("resultatImc", "")
            textViewCategorie.text = it.getString("resultatCategorie", "")
        }

    }

    private fun calculerImc() {
        val texteP = editTextPoids.text.toString()
        val texteT = editTextTaille.text.toString()

        // 1. Vérifier que les deux champs sont renseignés
        if (texteP.isBlank() || texteT.isBlank()) {
            Toast.makeText(this, R.string.erreur_champs_vides, Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Convertir en nombres décimaux
        val poids = texteP.toDoubleOrNull()
        val taille = texteT.toDoubleOrNull()

        // 3. Vérifier que la conversion a réussi ET que les valeurs sont strictement positives
        if (poids == null || taille == null || poids <= 0 || taille <= 0) {
            Toast.makeText(this, R.string.erreur_valeurs_invalides, Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Calculer l'IMC
        val imcBrut = poids / (taille * taille)
        val imc = (imcBrut * 100).roundToInt() / 100.0 // arrondi à 2 chiffres

        // 5. Déterminer la catégorie et la couleur
        val (categorie, couleur) = when {
            imc < 18.5 -> getString(R.string.categorie_insuffisance) to "#FF9800"       // orange
            imc < 25.0 -> getString(R.string.categorie_normale) to "#2E7D32"            // vert
            imc < 30.0 -> getString(R.string.categorie_surpoids) to "#FF9800"           // orange
            imc < 35.0 -> getString(R.string.categorie_obesite_moderee) to "#C62828"    // rouge
            imc < 40.0 -> getString(R.string.categorie_obesite_severe) to "#C62828"     // rouge
            else -> getString(R.string.categorie_obesite_morbide) to "#8B0000"          // rouge foncé
        }

        // 6. Afficher le résultat
        textViewImc.text = String.format("%.2f", imc)
        textViewCategorie.text = categorie
        textViewImc.setTextColor(android.graphics.Color.parseColor(couleur))
        textViewCategorie.setTextColor(android.graphics.Color.parseColor(couleur))
    }

    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("poids", editTextPoids.text.toString())
        outState.putString("taille", editTextTaille.text.toString())
        outState.putString("resultatImc", textViewImc.text.toString())
        outState.putString("resultatCategorie", textViewCategorie.text.toString())
    }
}