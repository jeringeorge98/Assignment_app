package com.example.loginsignup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.typeOf

private lateinit var db: FirebaseFirestore
var commentsList : ArrayList<String> = arrayListOf()

class Comment_screen : AppCompatActivity() {

    init{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_screen)

        db= FirebaseFirestore.getInstance()
        val id= intent.getStringExtra("id")
        val docRef = db.collection("/posts").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if(document!=null){
                    var comments= document.data?.get("comments")
                    Log.i("info", comments.toString())
                    Log.i("info", comments?.javaClass?.kotlin.toString())
                    commentsList = comments as ArrayList<String>
                    var listview =findViewById<ListView>(R.id.comment_list_view)
                    listview.adapter=CommentAdapter(this, commentsList)
                }
                else{
                    Log.i("info","No document exits")
                }
            }
            .addOnFailureListener{exception ->
                Log.i("info","expception is "+ exception)
            }

    }
}

private class CommentAdapter(context: Context, list: Any?):BaseAdapter(){

    var mainContext = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var layoutInflater = LayoutInflater.from(mainContext)
        var rowsMain = layoutInflater.inflate(R.layout.comment_rows, parent, false)

        var commentText = rowsMain.findViewById<TextView>(R.id.commentData)
        commentText.text = commentsList[position].toString()
        Log.i("info", commentsList.toString())
        return rowsMain
    }
    override fun getCount(): Int {
        return commentsList.size
    }
    override fun getItem(position: Int): Any {
        return commentsList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

}