package com.engels.tictactoe2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonPvP: Button = findViewById(R.id.button_pve);
        buttonPvP.setOnClickListener {
            val intent = Intent(this, PvCActivity::class.java)
            intent.putExtra("player", true)
            startActivity(intent)
        }
        val buttonPvE: Button = findViewById(R.id.button_pvp);
        buttonPvE.setOnClickListener {
            val intent = Intent(this, PvCActivity::class.java)
            intent.putExtra("player", false)
            startActivity(intent)
        }
    }
}