package com.example.kotlinpasswordmanager

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.kotlinpasswordmanager.viewPass
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class viewPass : Activity() {
    // for inflating the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val actionBar = actionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
        val inflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)
        return true
    }

    // on selection of the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.add_pass -> {
                val intent = Intent(this, PasswordManagerActivity::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpass)


        // Find the directory for the SD Card using the API
        // *Don't* hardcode "/sdcard"
        val sdcard = Environment.getExternalStorageDirectory()

        // Get the text file
        val file = File(sdcard, ".sk/logp.csv")

        // Read text from file
        val text = StringBuilder()
        try {
            val br = BufferedReader(FileReader(file))
            var line: String
            while (br.readLine().also { line = it } != null) {
                val temp = line.split(",".toRegex()).toTypedArray()
                val table =
                    findViewById<View>(R.id.maintable) as TableLayout

                // Inflate your row "template" and fill out the fields.
                val row = LayoutInflater.from(this@viewPass)
                    .inflate(R.layout.attrib_row, null) as TableRow
                (row.findViewById<View>(R.id.attrib_site) as TextView).text = temp[0]
                (row.findViewById<View>(R.id.attrib_username) as TextView).text = temp[1]

                // decrypted clear text
                val mcrypt = SimpleCrypto()
                var cleartext = ""
                try {
                    cleartext = String(mcrypt.decrypt(temp[2])!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                (row.findViewById<View>(R.id.attrib_pass) as TextView).text = cleartext
                (row.findViewById<View>(R.id.attrib_additional) as TextView).text = temp[3]
                table.addView(row)

                // add a null row to the end of each data
                val rownull = LayoutInflater.from(this@viewPass)
                    .inflate(R.layout.empty_row, null) as TableRow
                table.addView(rownull)

                //dont know if this is needed
                table.requestLayout()
            }
            br.close()
        } catch (e: IOException) {
            // You'll need to add proper error handling here
            e.printStackTrace()
        }
    }
}