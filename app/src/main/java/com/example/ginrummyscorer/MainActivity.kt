package com.example.ginrummyscorer

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private lateinit var scoreTable: TableLayout
    private lateinit var scoreInput: EditText
    private lateinit var resultInput: Spinner
    private lateinit var player1Button: Button
    private lateinit var player2Button: Button
    private lateinit var submitButton: Button
    private lateinit var totalsRow: TableRow
    private lateinit var separator: View

    private var selectedWinner: Player? = null
    private var player1: Player = Player("Brodie")
    private var player2: Player = Player("Paige")
    private var player1Total = 0
    private var player2Total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTable = findViewById(R.id.scoreTable)
        scoreInput = findViewById(R.id.scoreInput)
        resultInput = findViewById(R.id.resultInput)
        player1Button = findViewById(R.id.player1Button)
        player2Button = findViewById(R.id.player2Button)
        submitButton = findViewById(R.id.submitButton)

        val headerRow = scoreTable.getChildAt(0) as TableRow
        val player1Header = headerRow.getChildAt(0) as TextView
        val player2Header = headerRow.getChildAt(1) as TextView
        player1Header.text = player1.name
        player2Header.text = player2.name
        player1Button.text = player1.name
        player2Button.text = player2.name

        // Setup result spinner
        val resultOptions = arrayOf("Knock", "Gin", "Big Gin", "Undercut")
        val resultAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resultOptions)
        resultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resultInput.adapter = resultAdapter

        // Winner selection logic
        fun updateButtonStates(winner: Player) {
            selectedWinner = winner
            if (winner == player1) {
                player1Button.setBackgroundColor(Color.GREEN)
                player2Button.setBackgroundColor(Color.GRAY)
            } else {
                player2Button.setBackgroundColor(Color.GREEN)
                player1Button.setBackgroundColor(Color.GRAY)
            }
        }

        player1Button.setOnClickListener { updateButtonStates(player1) }
        player2Button.setOnClickListener { updateButtonStates(player2) }

        submitButton.setOnClickListener {
            addRow()
        }

        // Separator (horizontal line)
        separator = View(this).apply {
            setBackgroundColor(Color.BLACK)
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                4 // Line thickness
            )
        }

        // Totals row setup
        totalsRow = TableRow(this)
        updateTotalsRow()

        // Add separator and totals initially
        scoreTable.addView(separator)
        scoreTable.addView(totalsRow)
    }

    private fun addRow() {
        val scoreText = scoreInput.text.toString()
        val result = resultInput.selectedItem.toString()
        val winner = selectedWinner

        //if score is empty
        if (scoreText.isEmpty()) {
            Toast.makeText(this, "Please enter a score", Toast.LENGTH_SHORT).show()
            return
        }

        //if winner is not selected
        if (winner == null) {
            Toast.makeText(this, "Please select a winner", Toast.LENGTH_SHORT).show()
            return
        }

        //initialises new row
        val newRow = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val score = scoreText.toInt()

        val (player1TextView, player2TextView) = if (winner == player1) {
            player1Total += score
            TextView(this).apply { text = score.toString(); setPadding(8, 8, 8, 8) } to
                    TextView(this).apply { text = "0"; setPadding(8, 8, 8, 8) }
        } else {
            player2Total += score
            TextView(this).apply { text = "0"; setPadding(8, 8, 8, 8) } to
                    TextView(this).apply { text = score.toString(); setPadding(8, 8, 8, 8) }
        }

        val resultTextView = TextView(this).apply {
            text = "${winner.name}: $result"
            setPadding(8, 8, 8, 8)
        }

        newRow.addView(player1TextView)
        newRow.addView(player2TextView)
        newRow.addView(resultTextView)

        // Re-add separator and totals at the bottom
        scoreTable.removeView(separator)
        scoreTable.removeView(totalsRow)
        scoreTable.addView(newRow)
        scoreTable.addView(separator)
        scoreTable.addView(totalsRow)

        updateTotalsRow()

        // Reset inputs
        scoreInput.text.clear()
        resultInput.setSelection(0)
    }

    private fun updateTotalsRow() {
        totalsRow.removeAllViews()

        val player1TotalText = TextView(this).apply {
            text = player1Total.toString()
            setPadding(8, 8, 8, 8)
            setTextColor(Color.BLACK)
        }

        val player2TotalText = TextView(this).apply {
            text = player2Total.toString()
            setPadding(8, 8, 8, 8)
            setTextColor(Color.BLACK)
        }

        val labelText = TextView(this).apply {
            text = "Totals"
            setPadding(8, 8, 8, 8)
            setTextColor(Color.BLACK)
        }

        totalsRow.addView(player1TotalText)
        totalsRow.addView(player2TotalText)
        totalsRow.addView(labelText)
    }
}
