package com.darkness.sparkwomen

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashSet

class ContactActivity : AppCompatActivity() {
    private lateinit var contact: EditText
    private lateinit var addContact: Button
   private lateinit var recyclerView: RecyclerView
    var contacts: HashMap<String, String>? = null
    var send: ArrayList<String>? = null
    var adapter: ContactsAdapter? = null
    var onClickListener: MyOnClickListener? = null
   private lateinit var edit: ImageView
    var callerInfo: TextView? = null
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        edit = findViewById(R.id.editCallButton)
        edit.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this@ContactActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog)
            val close: Button
            val save: Button
            close = dialog.findViewById(R.id.dialogCancel)
            save = dialog.findViewById(R.id.dialogSave)
            val number = dialog.findViewById<EditText>(R.id.dialogEditText)
            save.setOnClickListener {
                val numberText = number.text.toString()
                if (numberText.length == 10) {
                    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("firstNumber", numberText)
                    editor.apply()
                    setCallingInformation()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this@ContactActivity, "Enter valid number!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            close.setOnClickListener { dialog.dismiss() }
            dialog.show()
        })
        callerInfo = findViewById(R.id.callText)
        setCallingInformation()
        contacts = HashMap()
        send = ArrayList()
        adapter = ContactsAdapter(this, send!!) { position -> deleteItemFromDatabase(position as Int) }
        recyclerView = findViewById(R.id.contacts)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        data
        contact = findViewById(R.id.contactGet)
        addContact = findViewById(R.id.addContact)
        addContact.setOnClickListener(View.OnClickListener {
            createContact(
                contact.getText().toString()
            )
        })
    }

    private fun createContact(contactString: String) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val oldNumbers = sharedPreferences.getStringSet("enumbers", LinkedHashSet())
        oldNumbers!!.add(contactString)
        editor.remove("enumbers")
        editor.putStringSet("enumbers", oldNumbers)
        editor.apply()
        contact!!.setText("")
        editor.apply()
        data
    }

    private fun setCallingInformation() {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val firstNumber = sharedPreferences.getString("firstNumber", "null")
        if (firstNumber!!.isEmpty() || firstNumber.equals("null", ignoreCase = true)) {
            callerInfo!!.text = "Please add number."
        } else {
            callerInfo!!.text = firstNumber
        }
    }

    private fun deleteItemFromDatabase(position: Int) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val oldNumbers = sharedPreferences.getStringSet("enumbers", LinkedHashSet())
        val editor = sharedPreferences.edit()
        editor.remove("enumbers")
        oldNumbers!!.remove(send!![position])
        editor.putStringSet("enumbers", oldNumbers)
        editor.apply()
        data
    }

    private val data: Unit
        private get() {
            send!!.clear()
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val oldNumbers = sharedPreferences.getStringSet("enumbers", LinkedHashSet())
            send!!.addAll(oldNumbers!!)
            adapter!!.notifyDataSetChanged()
        }
}