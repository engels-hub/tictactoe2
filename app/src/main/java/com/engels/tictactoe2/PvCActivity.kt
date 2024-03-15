package com.engels.tictactoe2

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.random.Random


class PvCActivity: AppCompatActivity(), View.OnClickListener {
    companion object {
        var board: Array<Array<Tile?>> = Array(3) { Array(3){
            null
        } }
        //bool for switching players
        var currentPlayer=true
        //list of buttons to loop through
        var IDList:LinkedHashMap<Int,Pair<Int,Int>> = LinkedHashMap()

        //rng for XO texturing
        val randomValues = List(9) { Random.nextInt(0, 5) }
        var rngIterator=0
        //bool for switching between pvp and pvc
        var notPlayer=true;
        //placeholder for first player name
        var firstPlayerName="Player1"
        //bool for allowing second player input
        var playerInputName=true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvc)

        //getting args from MainActivity
        notPlayer = intent.getBooleanExtra("player", true)
        firstPlayerName=intent.getStringExtra("name")!!

        //changing first player name
        val playerName=findViewById<TextView>(R.id.player1Name)
        playerName.text = firstPlayerName

        //first player moves
        rngIterator=0
        currentPlayer=true
        animatePadding()

        //"home" button
        val buttonBack: Button = findViewById(R.id.button_back);
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", firstPlayerName)
            startActivity(intent)
        }

        assignButtons()

        //switch to pvp mode
        if (!notPlayer) {
            assignSecondPlayerName()
        }

    }

    private fun assignSecondPlayerName() {
            //hide player placeholder name, show input field
            val editText = findViewById<EditText>(R.id.player2)
            val player2name = findViewById<TextView>(R.id.pcName)
            player2name.visibility = View.GONE
            editText.visibility = View.VISIBLE


            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    player2name.text = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
    }
    private fun setSecondPlayerName() {
        if (!notPlayer) {
            Log.d("A", "Mogus")
            val player2input = findViewById<EditText>(R.id.player2)
            val player2name = findViewById<TextView>(R.id.pcName)
            if(player2input.text.toString().isEmpty()) player2name.text=
                getString(R.string.player_2_default)
            //disable further input
            playerInputName = false
            //hide input, show changed name
            player2input.visibility = View.GONE
            player2name.visibility = View.VISIBLE
        }
    }
    private fun assignButtons(){
        //assign UI buttons to board
        board[0][0]=Tile(findViewById<Button>(R.id.a1), null, false)
        board[0][1]=Tile(findViewById<Button>(R.id.a2), null, false)
        board[0][2]=Tile(findViewById<Button>(R.id.a3), null, false)
        board[1][0]=Tile(findViewById<Button>(R.id.b1), null, false)
        board[1][1]=Tile(findViewById<Button>(R.id.b2), null, false)
        board[1][2]=Tile(findViewById<Button>(R.id.b3), null, false)
        board[2][0]=Tile(findViewById<Button>(R.id.c1), null, false)
        board[2][1]=Tile(findViewById<Button>(R.id.c2), null, false)
        board[2][2]=Tile(findViewById<Button>(R.id.c3), null, false)

        //flatten board 2d array
        for (y in board.indices) {
            for (x in board[y].indices) {
                board[y][x]!!.button.setOnClickListener(this)
                IDList[board[y][x]!!.button.id]= Pair(x,y)
            }
        }
    }

    override fun onClick(v: View?) {

        //when game starts, freeze second player name
        setSecondPlayerName()

        val text = findViewById<TextView>(R.id.step)

        //if tile is already occupied
        if ((v as Button).text.isNotEmpty()) return

        //change tile in board matrix
        val coords = IDList[v.id]
        board[coords!!.second][coords.first]!!.captured = currentPlayer


        //draw X or O in tile
        v.backgroundTintList = null
        if (currentPlayer) {
            when(randomValues[rngIterator]){
                0->v.setBackgroundResource(R.drawable.x1)
                1->v.setBackgroundResource(R.drawable.x2)
                2->v.setBackgroundResource(R.drawable.x3)
                3->v.setBackgroundResource(R.drawable.x4)
                4->v.setBackgroundResource(R.drawable.x5)
                else->v.setBackgroundResource(R.drawable.x1)
            }

        }else{
            when(randomValues[rngIterator]){
                0->v.setBackgroundResource(R.drawable.o1)
                1->v.setBackgroundResource(R.drawable.o2)
                2->v.setBackgroundResource(R.drawable.o3)
                3->v.setBackgroundResource(R.drawable.o4)
                4->v.setBackgroundResource(R.drawable.o5)
                else->v.setBackgroundResource(R.drawable.o1)
            }
        }
        rngIterator++

        //check if current player won
        val win = checkWinConditions(coords.first, coords.second)

        //win can be true, false (for player win conditions) and null for no win detected
        if(win!=null){
            displayWinner(text)
            return
        }
        //determine tie situation
        if(rngIterator==9){
            text.text= getString(R.string.tie)
            removeButtonListeners()
            return
        }
        //prepare for next move
        currentPlayer = !currentPlayer
        animatePadding()

        //let ai move in pvc mode
        if(!currentPlayer && notPlayer){
            aiMove()
            animatePadding()
        }


    }

    private fun removeButtonListeners() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j]!!.button.setOnClickListener(null)
            }
        }
    }

    private fun displayWinner(text: TextView) {
        val name: String
        if (currentPlayer) {
            text.text = getString(R.string.cross_won)
        } else {
            text.text = getString(R.string.nought_won)
        }
        return
    }



    private fun checkWinConditions(x:Int, y:Int): Boolean? {

        val testArray=arrayOf(
            arrayOf(board[0][0]!!,board[0][1]!!,board[0][2]!!),
            arrayOf(board[1][0]!!,board[1][1]!!,board[1][2]!!),
            arrayOf(board[2][0]!!,board[2][1]!!,board[2][2]!!),

            arrayOf(board[0][0]!!,board[1][0]!!,board[2][0]!!),
            arrayOf(board[0][1]!!,board[1][1]!!,board[2][1]!!),
            arrayOf(board[0][2]!!,board[1][2]!!,board[2][2]!!),

            arrayOf(board[0][0]!!,board[1][1]!!,board[2][2]!!),
            arrayOf(board[2][0]!!,board[1][1]!!,board[0][2]!!),


        )


        forLoop@for( i in testArray.indices){
            Log.d("CHECK",i.toString())
            for(j in testArray[i].indices){
                if(testArray[i][j].captured==null) continue@forLoop
            }
            if(testArray[i][0].captured==testArray[i][1].captured && testArray[i][1].captured==testArray[i][2].captured){
                testArray[i][0].line = true
                testArray[i][1].line = true
                testArray[i][2].line = true
                paintTiles()
                return testArray[i][2].captured
            }
        }

        return null
    }

    private fun paintTiles(){
        printMatrix()
        //tint won tiles, later maybe replace with more complex logic
        removeButtonListeners()
        for (i in board.indices) {
            for (j in board[i].indices) {
                if(board[i][j]!!.line)
                    board[i][j]!!.button.backgroundTintList = ContextCompat.getColorStateList(
                        this, R.color.green)
            }
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


    private fun animatePadding() {
        val animator = ValueAnimator.ofInt(0, 100)


        animator.duration = 500
        val v1:LinearLayout
        val v2:LinearLayout
        if(currentPlayer){
             v1=findViewById<LinearLayout>(R.id.player_card)
             v2=findViewById<LinearLayout>(R.id.comp_card)
        }else{
             v1=findViewById<LinearLayout>(R.id.comp_card)
             v2=findViewById<LinearLayout>(R.id.player_card)
        }
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int

            v1.setPadding(0,animatedValue,0,0)
            v2.setPadding(0,100-animatedValue,0,0)
        }


        animator.start()
    }

    private fun aiMove(){
        Thread.sleep(500)
        animatePadding()
        //find empty tiles
        val emptyTiles = mutableListOf<Pair<Int,Int>>()
        for (i in board.indices){
            for (j in board[i].indices) {
                if(board[i][j]!!.captured==null){
                    emptyTiles.add(Pair(i,j))
                }
            }
        }

        emptyTiles.forEach {

            val x=it.second
            val y=it.first
            //check horizontal

            if(x==0 && !(board[y][1]!!.captured == null || board[y][2]!!.captured == null)){
                if (board[y][1]!!.captured == board[y][2]!!.captured) {
                    Log.d("AI", "X")
                    aiButtonPress(y, x)
                    return
                }
            }
            if(x==1 && !(board[y][0]!!.captured == null || board[y][2]!!.captured == null)){

                if (board[y][0]!!.captured == board[y][2]!!.captured) {
                    aiButtonPress(y, x)
                    Log.d("AI", "X")
                    return
                }
            }
            if(x==2 && !(board[y][0]!!.captured == null || board[y][1]!!.captured == null)){
                if (board[y][0]!!.captured == board[y][1]!!.captured) {
                    aiButtonPress(y, x)
                    Log.d("AI", "X")
                    return
                }
            }


            //check vertical

            if(y==0 && !(board[1][x]!!.captured==null||board[2][x]!!.captured==null)){
                if(board[1][x]!!.captured==board[2][x]!!.captured) {
                    aiButtonPress(y,x)
                    Log.d("AI", "Y")
                    return
                }
            }
            if(y==1 && !(board[0][x]!!.captured==null||board[2][x]!!.captured==null)){
                if(board[0][x]!!.captured==board[2][x]!!.captured) {
                    aiButtonPress(y,x)
                    Log.d("AI", "Y")
                    return
                }
            }
            if(y==2 && !(board[0][x]!!.captured==null||board[1][x]!!.captured==null)){
                if(board[0][x]!!.captured==board[1][x]!!.captured) {
                    aiButtonPress(y,x)
                    Log.d("AI", "Y")
                    return
                }
            }

            //00
            if(x==0 && y==0)
                if(board[1][1]!!.captured!=null||board[2][2]!!.captured!=null)
                    if(board[1][1]!!.captured==board[2][2]!!.captured){
                        aiButtonPress(0,0)
                        Log.d("AI", "D")
                        return
                    }
            //02
            if(x==0 && y==2)
                if(board[1][1]!!.captured!=null||board[0][2]!!.captured!=null)
                    if(board[1][1]!!.captured==board[0][2]!!.captured){
                        aiButtonPress(2,0)
                        Log.d("AI", "D")
                        return
                    }
            //11
            if(x==1 && y==1){
                if(board[0][0]!!.captured!=null||board[2][2]!!.captured!=null)
                    if(board[0][0]!!.captured==board[2][2]!!.captured){
                        aiButtonPress(1,1)
                        Log.d("AI", "D")
                        return
                    }
                if(board[0][2]!!.captured!=null||board[2][0]!!.captured!=null)
                    if(board[0][2]!!.captured==board[2][0]!!.captured){
                        aiButtonPress(1,1)
                        Log.d("AI", "D")
                        return
                    }
            }
            //20
            if(x==2 && y==0)
                if(board[1][1]!!.captured!=null||board[2][0]!!.captured!=null)
                    if(board[1][1]!!.captured==board[2][0]!!.captured){
                        aiButtonPress(0,2)
                        Log.d("AI", "D")
                        return
                    }
            //22
            if(x==2 && y==2)
                if(board[1][1]!!.captured!=null||board[0][0]!!.captured!=null)
                    if(board[1][1]!!.captured==board[0][0]!!.captured){
                        aiButtonPress(2,2)
                        Log.d("AI", "D")
                        return
                    }
        }
        //random move
        val randomItem=emptyTiles.random()
        Log.d("AI", "RAND")
        aiButtonPress(randomItem.first,randomItem.second)
        return
    }

    private fun aiButtonPress(i: Int, j:Int){
        val v = board[i][j]!!.button;
        v.performClick()
        return
    }

}



