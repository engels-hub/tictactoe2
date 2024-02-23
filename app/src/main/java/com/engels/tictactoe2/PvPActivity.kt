package com.engels.tictactoe2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class PvPActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var board: Array<Array<Tile?>> = Array(3) { Array(3){
            null
        } }
        var currentPlayer=true
        var IDList:LinkedHashMap<Int,Pair<Int,Int>> = LinkedHashMap()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvp)
        val buttonBack: Button = findViewById(R.id.button_back);
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        PvCActivity.board[0][0]=Tile(findViewById<Button>(R.id.a1), null, false,)
        PvCActivity.board[0][1]=Tile(findViewById<Button>(R.id.a2), null, false,)
        PvCActivity.board[0][2]=Tile(findViewById<Button>(R.id.a3), null, false,)
        PvCActivity.board[1][0]=Tile(findViewById<Button>(R.id.b1), null, false,)
        PvCActivity.board[1][1]=Tile(findViewById<Button>(R.id.b2), null, false,)
        PvCActivity.board[1][2]=Tile(findViewById<Button>(R.id.b3), null, false,)
        PvCActivity.board[2][0]=Tile(findViewById<Button>(R.id.c1), null, false,)
        PvCActivity.board[2][1]=Tile(findViewById<Button>(R.id.c2), null, false,)
        PvCActivity.board[2][2]=Tile(findViewById<Button>(R.id.c3), null, false,)

        for (y in PvCActivity.board.indices) {
            for (x in PvCActivity.board[y].indices) {
                PvCActivity.board[y][x]!!.button.setOnClickListener(this)
                PvCActivity.IDList[PvCActivity.board[y][x]!!.button.id]= Pair(x,y)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {

        val text = findViewById<TextView>(R.id.step)
        if ((v as Button).text.isNotEmpty()) return
        val coords = PvCActivity.IDList[v.id]
        PvCActivity.board[coords!!.second][coords.first]!!.captured = PvCActivity.currentPlayer
        v.text = if (PvCActivity.currentPlayer) "X" else "O"


        val win = checkWinConditions(coords.first, coords.second)
        Log.d("TAG", win.toString())
        //text.text= if(currentPlayer) "Cross" else "Nought" + if(win!=null)" wins!" else ""

        PvCActivity.currentPlayer = !PvCActivity.currentPlayer
        text.text = "Next:${if (PvCActivity.currentPlayer) "Cross" else "Nought"}"

    }
    private fun checkWinConditions(x:Int, y:Int): Boolean? {
        //check if diag is possible in that point
        Log.d("X", x.toString())
        Log.d("Y", y.toString())
        printMatrix()
        if(PvCActivity.board[1][1]!!.captured == PvCActivity.currentPlayer) {
            if(x==0 && y==0 && (PvCActivity.board[2][2]!!.captured== PvCActivity.currentPlayer)) {
                PvCActivity.board[0][0]!!.line=true
                PvCActivity.board[1][1]!!.line=true
                PvCActivity.board[2][2]!!.line=true
                paintTiles()
                return PvCActivity.currentPlayer
            }
            if(x==2 && y==2 && (PvCActivity.board[0][0]!!.captured== PvCActivity.currentPlayer)){
                PvCActivity.board[0][0]!!.line=true
                PvCActivity.board[1][1]!!.line=true
                PvCActivity.board[2][2]!!.line=true
                paintTiles()
                return PvCActivity.currentPlayer
            }
            if(x==0 && y==2 && (PvCActivity.board[0][2]!!.captured== PvCActivity.currentPlayer)){
                PvCActivity.board[2][0]!!.line=true
                PvCActivity.board[1][1]!!.line=true
                PvCActivity.board[0][2]!!.line=true
                paintTiles()
                return PvCActivity.currentPlayer
            }
            if(x==2 && y==0 && (PvCActivity.board[2][0]!!.captured== PvCActivity.currentPlayer)){
                PvCActivity.board[2][0]!!.line=true
                PvCActivity.board[1][1]!!.line=true
                PvCActivity.board[0][2]!!.line=true
                paintTiles()
                return PvCActivity.currentPlayer
            }
        }
//TODO:might be xy-yx bug, check that


        if(PvCActivity.board[0][x]!!.captured== PvCActivity.currentPlayer && PvCActivity.board[1][x]!!.captured== PvCActivity.currentPlayer && PvCActivity.board[2][x]!!.captured== PvCActivity.currentPlayer) {
            PvCActivity.board[0][x]!!.line = true
            PvCActivity.board[1][x]!!.line = true
            PvCActivity.board[2][x]!!.line = true
            paintTiles()
            return PvCActivity.currentPlayer
        }
        if(PvCActivity.board[y][0]!!.captured== PvCActivity.currentPlayer && PvCActivity.board[y][1]!!.captured== PvCActivity.currentPlayer && PvCActivity.board[y][2]!!.captured== PvCActivity.currentPlayer){
            PvCActivity.board[y][0]!!.line=true
            PvCActivity.board[y][1]!!.line=true
            PvCActivity.board[y][2]!!.line=true
            paintTiles()
            return PvCActivity.currentPlayer
        }


        return null
    }

    private fun paintTiles(){
        Log.d("WIN", "WIN")
        //paint won tiles, later replace with more complex logic
        for (i in PvCActivity.board.indices) {
            var arrayTest = arrayOf<Boolean>(false,false,false)
            for (j in PvCActivity.board[i].indices) {
                arrayTest[j]= PvCActivity.board[i][j]!!.line
                PvCActivity.board[i][j]!!.button.setOnClickListener(null)
                if(PvCActivity.board[i][j]!!.line)
                    PvCActivity.board[i][j]!!.button.backgroundTintList = ContextCompat.getColorStateList(
                        this, R.color.green)
            }
            println(arrayTest.contentToString())
        }
    }

    private fun printMatrix(){
        //test purposes
        for (i in PvCActivity.board.indices) {
            var arrayTest = arrayOf<Boolean?>(null,null,null)
            for (j in PvCActivity.board[i].indices) {
                arrayTest[j]= PvCActivity.board[i][j]!!.captured
            }
            println(arrayTest.contentToString())
        }
    }
}