package com.example.urlconnectpractice

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

@Suppress("DEPRECATION")

class JSONParser(
    private var c: Context,
    private var jsonData: String,
    private var myGridView: GridView
) : AsyncTask<Void, Void, Boolean>() {

    private lateinit var pd: ProgressDialog
    private var users = ArrayList<User>()

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()

        Log.d("ASD", "PRE EXECUTE")
        pd = ProgressDialog(c)
        pd.setTitle("Parse JSON")
        pd.setMessage("Parsing...Please wait")
        pd.show()
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg voids: Void): Boolean? {
        Log.d("ASD", "doInBackground")
        return parse()
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)

        pd.dismiss()
        if (isParsed!!) {
            //BIND
            myGridView.adapter = MrAdapter(c, users)
        } else {
            Toast.makeText(
                c,
                "Unable To Parse that data. ARE YOU SURE IT IS VALID JSON DATA? JsonException was raised. Check Log Output.",
                Toast.LENGTH_LONG
            ).show()
            Toast.makeText(
                c,
                "THIS IS THE DATA WE WERE TRYING TO PARSE :  " + jsonData,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /*
    Parse JSON data
     */
    private fun parse(): Boolean {
        try {
            Log.d("ASD", "json pre-parse")
            Log.d("ASD", jsonData)
            val ja = JSONArray(jsonData)
            Log.d("ASD", "json array")
            var jo: JSONObject
            Log.d("ASD", "json object")

            users.clear()
            var user: User

            for (i in 0 until ja.length()) {
                jo = ja.getJSONObject(i)

                val name = jo.getString("name")
                val username = jo.getString("username")
                val email = jo.getString("email")

                user = User(username, name, email)
                users.add(user)
            }

            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

    class User(
        private var m_username: String,
        private var m_name: String,
        private var m_email: String
    ) {

        fun getUsername(): String {
            return m_username
        }

        fun getName(): String {
            return m_name
        }

        fun getEmail(): String {
            return m_email
        }
    }

    class MrAdapter(private var c: Context, private var users: ArrayList<User>) : BaseAdapter() {

        override fun getCount(): Int {
            return users.size
        }

        override fun getItem(pos: Int): Any {
            return users[pos]
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        /*
        Inflate row_model.xml and return it
         */
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false)
            }

            val nameTxt = convertView!!.findViewById<TextView>(R.id.nameTxt) as TextView
            val usernameTxt = convertView.findViewById<TextView>(R.id.usernameTxt) as TextView
            val emailTxt = convertView.findViewById<TextView>(R.id.emailTxt) as TextView

            val user = this.getItem(i) as User

            nameTxt.text = user.getName()
            emailTxt.text = user.getEmail()
            usernameTxt.text = user.getUsername()

            convertView.setOnClickListener {
                Toast.makeText(c, user.getName(), Toast.LENGTH_SHORT).show()
            }

            return convertView
        }
    }
}
