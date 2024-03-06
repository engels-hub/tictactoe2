package com.engels.tictactoe2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val prefName = "TicTacToePlayerName"
        val playerNamePreferences = "playerName"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonPvP: Button = findViewById(R.id.button_pve);
        var playerName="Player"
        val sharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val retrievedName = sharedPreferences.getString(playerNamePreferences, null)
        val editText = findViewById<EditText>(R.id.player_name_input)
        editText.setText(retrievedName)
        if(intent.getStringExtra("name")!=null){
            playerName= intent.getStringExtra("name")!!
            editText.setText(playerName)
        }



        //TODO:implement an EditText change listener in Kotlin - Gemini
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // This function is called after the text has changed
                // You can access the new text using `s.toString()`
                playerName = s.toString()
                // Perform actions based on the new text, like validation or updates

                val editor = sharedPreferences.edit()
                editor.putString(playerNamePreferences, playerName)
                editor.apply()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This function is called before the text is changed
                // You can use this for actions before changes, like saving previous state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This function is called every time the text changes
                // You can use this for real-time processing, like filtering suggestions
            }
        })
        buttonPvP.setOnClickListener {
            val intent = Intent(this, PvCActivity::class.java)
            intent.putExtra("player", true)
            intent.putExtra("name",playerName)
            startActivity(intent)
        }
        val buttonPvE: Button = findViewById(R.id.button_pvp);
        buttonPvE.setOnClickListener {
            val intent = Intent(this, PvCActivity::class.java)
            intent.putExtra("player", false)
            intent.putExtra("name",playerName)
            startActivity(intent)
        }



    }
}