package com.engels.tictactoe2

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlin.random.Random


class PvCActivity: AppCompatActivity(), View.OnClickListener {
    companion object {
        var board: Array<Array<Tile?>> = Array(3) { Array(3){
            null
        } }
        var currentPlayer=true
        var IDList:LinkedHashMap<Int,Pair<Int,Int>> = LinkedHashMap()
        val randomValues = List(9) { Random.nextInt(0, 5) }
        var rngIterator=0
        var isPlayer=true;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvc)
        isPlayer = intent.getBooleanExtra("player", true)
        rngIterator=0
        currentPlayer=true
        animatePadding()
        val buttonBack: Button = findViewById(R.id.button_back);
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        board[0][0]=Tile(findViewById<Button>(R.id.a1), null, false)
        board[0][1]=Tile(findViewById<Button>(R.id.a2), null, false)
        board[0][2]=Tile(findViewById<Button>(R.id.a3), null, false)
        board[1][0]=Tile(findViewById<Button>(R.id.b1), null, false)
        board[1][1]=Tile(findViewById<Button>(R.id.b2), null, false)
        board[1][2]=Tile(findViewById<Button>(R.id.b3), null, false)
        board[2][0]=Tile(findViewById<Button>(R.id.c1), null, false)
        board[2][1]=Tile(findViewById<Button>(R.id.c2), null, false)
        board[2][2]=Tile(findViewById<Button>(R.id.c3), null, false)

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

        val win = checkWinConditions(coords.first, coords.second)
        Log.d("TAG", win.toString())
        //text.text= if(currentPlayer) "Cross" else "Nought" + if(win!=null)" wins!" else ""
        if(win!=null){
            text.text="${if (currentPlayer) "Cross" else "Nought"} won!!"
            return
        }
        if(rngIterator==9){
            text.text="Tie!"
            for (i in board.indices) {
                for (j in board[i].indices) {
                    board[i][j]!!.button.setOnClickListener(null)
                }
            }
            return
        }
        currentPlayer = !currentPlayer
        animatePadding()

        if(!currentPlayer && isPlayer){

            aiMove()
            animatePadding()
        }


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

//        //check if diag is possible in that point
//        Log.d("X", x.toString())
//        Log.d("Y", y.toString())
//        printMatrix()
//
//        if(!(board[1][1]!!.captured==null||board[0][0]!!.captured==null||board[2][2]!!.captured==null)){
//            Log.d("CHECK","D1")
//            if(board[1][1]!!.captured==board[0][0]!!.captured && board[0][0]!!.captured==board[2][2]!!.captured){
//                board[0][0]!!.line = true
//                board[1][1]!!.line = true
//                board[2][2]!!.line = true
//                paintTiles()
//                return board[1][1]!!.captured
//            }
//        }
//
//        if(!(board[1][1]!!.captured==null||board[2][0]!!.captured==null||board[0][2]!!.captured==null)){
//            Log.d("CHECK","D2")
//            if(board[1][1]!!.captured==board[2][0]!!.captured && board[0][0]!!.captured==board[2][0]!!.captured){
//                board[2][0]!!.line = true
//                board[1][1]!!.line = true
//                board[0][2]!!.line = true
//                paintTiles()
//                return board[1][1]!!.captured
//            }
//        }
//
//
//        if(board[0][x]!!.captured== currentPlayer && board[1][x]!!.captured== currentPlayer && board[2][x]!!.captured== currentPlayer) {
//            board[0][x]!!.line = true
//            board[1][x]!!.line = true
//            board[2][x]!!.line = true
//            paintTiles()
//            return currentPlayer
//        }
//        if(board[y][0]!!.captured== currentPlayer && board[y][1]!!.captured== currentPlayer && board[y][2]!!.captured== currentPlayer){
//            board[y][0]!!.line=true
//            board[y][1]!!.line=true
//            board[y][2]!!.line=true
//            paintTiles()
//            return currentPlayer
//        }


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
        //emptyTiles.shuffle()

        emptyTiles.forEach {

            val x=it.second
            val y=it.first
            //check horizontal
            Log.d("AISTEP", "${y} ${x}")

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


            //check y

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



