package com.example.kotlinpasswordmanager

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileWriter

class PasswordManagerActivity : Activity() {
    // initialise
    var site: EditText? = null
    var username: EditText? = null
    var password: EditText? = null
    var Additional: EditText? = null
    var save: Button? = null
    var reset: Button? = null
    var savedata = ""

    // for inflating the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // on selection of the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.view_passwords -> {
                val intent = Intent(this, viewPass::class.java)
                startActivity(intent)
                true
            }
            R.id.mp -> {
                val intent2 = Intent(this, masterPass::class.java)
                startActivity(intent2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initialise()
        val actionBar = actionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))

        //to set the site Edit Text to get the focus
        site!!.requestFocus()
        site!!.isFocusableInTouchMode = true

        // save the data to the textfile
        save!!.setOnClickListener {
            // creats hidden directory if not existing
            val dir = File(
                Environment.getExternalStorageDirectory().absolutePath + "/.sk/"
            )
            if (!dir.exists()) {
                dir.mkdir()
            }

            // saving data part
            val sFileName = Environment.getExternalStorageDirectory()
                .absolutePath + "/.sk/" + "logp.csv"
            try {
                val writer = FileWriter(sFileName, true)
                var sSite: String
                var sUser: String
                var sPass: String
                var sAdd: String
                sSite = site!!.text.toString()
                sUser = username!!.text.toString()
                sPass = password!!.text.toString()
                sAdd = Additional!!.text.toString()
                if (sSite == "" && sUser == ""
                    && sPass == "" && sAdd == ""
                ) {
                    Toast.makeText(
                        baseContext, "Please Enter Atleast one Field",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (sSite == "") sSite = "N/A"
                    if (sUser == "") sUser = "N/A"
                    if (sPass == "") sPass = "N/A"
                    if (sAdd == "") sAdd = "N/A"

                    // encrypting the passwords before saving
                    val mcrypt = SimpleCrypto()
                    sPass = SimpleCrypto.Companion.bytesToHex(mcrypt.encrypt(sPass))!!
                    //sPass = SimpleCrypto.encrypt("fugly", sPass);
                    writer.append(sSite)
                    writer.append(',')
                    writer.append(sUser)
                    writer.append(',')
                    writer.append(sPass)
                    writer.append(',')
                    writer.append(sAdd)
                    writer.append('\n')

                    // generate whatever data you want
                    writer.flush()
                    writer.close()
                    Toast.makeText(
                        baseContext, "Saved :)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    baseContext, e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Reset
        reset!!.setOnClickListener {
            site!!.setText("")
            username!!.setText("")
            password!!.setText("")
            Additional!!.setText("")
        }
    }

    fun initialise() {
        site = findViewById<View>(R.id.EditTextSite) as EditText
        username = findViewById<View>(R.id.EditTextUsername) as EditText
        password = findViewById<View>(R.id.editTextPassword) as EditText
        Additional = findViewById<View>(R.id.editTextAdditional) as EditText
        save = findViewById<View>(R.id.buttonSave) as Button
        reset = findViewById<View>(R.id.ButtonReset) as Button
    }
}