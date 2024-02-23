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



class PvCActivity: AppCompatActivity(), View.OnClickListener {
    companion object {
        var board: Array<Array<Tile?>> = Array(3) { Array(3){
            null
        } }
        var currentPlayer=true
        var IDList:LinkedHashMap<Int,Pair<Int,Int>> = LinkedHashMap()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvc)
        val buttonBack: Button = findViewById(R.id.button_back);
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        board[0][0]=Tile(findViewById<Button>(R.id.a1), null, false,)
        board[0][1]=Tile(findViewById<Button>(R.id.a2), null, false,)
        board[0][2]=Tile(findViewById<Button>(R.id.a3), null, false,)
        board[1][0]=Tile(findViewById<Button>(R.id.b1), null, false,)
        board[1][1]=Tile(findViewById<Button>(R.id.b2), null, false,)
        board[1][2]=Tile(findViewById<Button>(R.id.b3), null, false,)
        board[2][0]=Tile(findViewById<Button>(R.id.c1), null, false,)
        board[2][1]=Tile(findViewById<Button>(R.id.c2), null, false,)
        board[2][2]=Tile(findViewById<Button>(R.id.c3), null, false,)

        for (y in board.indices) {
            for (x in board[y].indices) {
                board[y][x]!!.button.setOnClickListener(this)
                IDList[board[y][x]!!.button.id]= Pair(x,y)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {

        val text = findViewById<TextView>(R.id.step)
        if ((v as Button).text.isNotEmpty()) return
        val coords = IDList[v.id]
        board[coords!!.second][coords.first]!!.captured = currentPlayer
        v.text = if (currentPlayer) "X" else "O"


        val win = checkWinConditions(coords.first, coords.second)
        Log.d("TAG", win.toString())
        //text.text= if(currentPlayer) "Cross" else "Nought" + if(win!=null)" wins!" else ""

        currentPlayer = !currentPlayer
        text.text = "Next:${if (currentPlayer) "Cross" else "Nought"}"

    }
    private fun checkWinConditions(x:Int, y:Int): Boolean? {
        //check if diag is possible in that point
        Log.d("X", x.toString())
        Log.d("Y", y.toString())
        printMatrix()
        if(board[1][1]!!.captured == currentPlayer) {
            if(x==0 && y==0 && (board[2][2]!!.captured== currentPlayer)) {
                board[0][0]!!.line=true
                board[1][1]!!.line=true
                board[2][2]!!.line=true
                paintTiles()
                return currentPlayer
            }
            if(x==2 && y==2 && (board[0][0]!!.captured== currentPlayer)){
                board[0][0]!!.line=true
                board[1][1]!!.line=true
                board[2][2]!!.line=true
                paintTiles()
                return currentPlayer
            }
            if(x==0 && y==2 && (board[0][2]!!.captured== currentPlayer)){
                board[2][0]!!.line=true
                board[1][1]!!.line=true
                board[0][2]!!.line=true
                paintTiles()
                return currentPlayer
            }
            if(x==2 && y==0 && (board[2][0]!!.captured== currentPlayer)){
                board[2][0]!!.line=true
                board[1][1]!!.line=true
                board[0][2]!!.line=true
                paintTiles()
                return currentPlayer
            }
        }
//TODO:might be xy-yx bug, check that


        if(board[0][x]!!.captured== currentPlayer && board[1][x]!!.captured== currentPlayer && board[2][x]!!.captured== currentPlayer) {
            board[0][x]!!.line = true
            board[1][x]!!.line = true
            board[2][x]!!.line = true
            paintTiles()
            return currentPlayer
        }
        if(board[y][0]!!.captured== currentPlayer && board[y][1]!!.captured== currentPlayer && board[y][2]!!.captured== currentPlayer){
            board[y][0]!!.line=true
            board[y][1]!!.line=true
            board[y][2]!!.line=true
            paintTiles()
            return currentPlayer
        }


        return null
    }

    private fun paintTiles(){
        Log.d("WIN", "WIN")
        //paint won tiles, later replace with more complex logic
        for (i in board.indices) {
            var arrayTest = arrayOf<Boolean>(false,false,false)
            for (j in board[i].indices) {
                arrayTest[j]= board[i][j]!!.line
                board[i][j]!!.button.setOnClickListener(null)
                if(board[i][j]!!.line)
                    board[i][j]!!.button.backgroundTintList = ContextCompat.getColorStateList(
                        this, R.color.green)
            }
            println(arrayTest.contentToString())
        }
    }

    private fun printMatrix(){
        //test purposes
        for (i in board.indices) {
            var arrayTest = arrayOf<Boolean?>(null,null,null)
            for (j in board[i].indices) {
                arrayTest[j]= board[i][j]!!.captured
            }
            println(arrayTest.contentToString())
        }
    }
}