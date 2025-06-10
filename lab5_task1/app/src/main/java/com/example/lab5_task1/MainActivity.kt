package com.example.lab5_task1
import GridItem
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

@OptIn(ExperimentalStdlibApi::class)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        val itemCount = 40
        val items = mutableListOf<GridItem>()
        for (i in 1..itemCount) {
            val number = Random.nextInt(1, 100)
            val color = Color.rgb(
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
            items.add(GridItem(number, color))
        }
        val adapter = GridAdapter(items) { item ->
            AlertDialog.Builder(this)
                .setTitle("Number")
                .setMessage("#: ${item.number}. AHEX: #${item.color.toHexString(
                    HexFormat.UpperCase)}")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        recyclerView.adapter = adapter
    }
}