package com.darkness.sparkwomen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_terms_and_condition.*

class Terms_and_Condition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)

        accept_btn.setOnClickListener {
            val intent = Intent(this@Terms_and_Condition,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        cancel_btn.setOnClickListener {
            Toast.makeText(this,"Please Accept Conditions",Toast.LENGTH_LONG).show()

        }
    }




}