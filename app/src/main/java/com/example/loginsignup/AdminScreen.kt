package com.example.loginsignup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.widget.Toolbar
import kotlin.random.Random
import kotlin.reflect.typeOf

class AdminScreen : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    private var posts: ArrayList<Posts> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        db= FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_screen)

        val lstView = findViewById<ListView>(R.id.list_view)
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
//        val binding

        val Btn : View = findViewById(R.id.floatingBtn)
        Btn.setOnClickListener(){
            val testIntent = Intent(this, UploadPosts::class.java)
            startActivityForResult(testIntent, 1)
        }
        db.collection("/posts")
            .get()
            .addOnSuccessListener { result ->
                val post=result
                Log.i("info","result is "+ post)
                for (document in result) {
                    val id = document.id
                    val comm = document.data["comments"]
                    val dta = document.data["data"]
                    val like = document.data["likes"]
                    val date=document.data["Date"] as com.google.firebase.Timestamp
                    val temp = Posts(id, comm, dta, like,date)

                    posts.add(temp)
                }

                  lstView.adapter=MyCustomAdapter(this,posts)
            }
            .addOnFailureListener { exception ->
                Log.i("info", "Error getting documents.", exception)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==1) {
            if(resultCode== Activity.RESULT_OK){
                recreate()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemview= item.itemId
        when(itemview){
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()

                Toast.makeText(applicationContext,"Signed out succesfully clicked", Toast.LENGTH_LONG).show()
                val homeintent= Intent(this,MainActivity::class.java)
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(homeintent)
                finishAffinity()
            }
        }
        return false
    }
}
private class MyCustomAdapter(context:Context,private val dataList: ArrayList<Posts> = ArrayList()): BaseAdapter(){
      private val mContext:Context
       init{
           mContext=context
       }
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        var dataItem=dataList[position]
        val currentdate= dataItem.date.seconds*1000+ dataItem.date.nanoseconds/ 1000000
       val netdate= Date(currentdate)
        val sdf = SimpleDateFormat("MM/dd/yyyy")
//      Log.i("info","${sdf.format(currentdate)}")
        Log.i("info","${currentdate}")
        val layoutinflater= LayoutInflater.from(mContext)
        val rowMain=layoutinflater.inflate(R.layout.rows,viewGroup,false)
     rowMain.findViewById<TextView>(R.id.date_view).text="Posted on ${sdf.format(netdate).toString()}"
        rowMain.findViewById<TextView>(R.id.main_text).text="Post ${position+1}"
         rowMain.findViewById<TextView>(R.id.body_text).text= dataItem.data.toString()

        // show comments
        val showComments = rowMain.findViewById<TextView>(R.id.showComments_admin)
        showComments?.setOnClickListener(){
            var id = dataList[position].id.toString()
            var commentintent= Intent(mContext, Comment_screen::class.java)
//            var dbColl = db.collection("posts").document(id)
            commentintent.putExtra("id",id)
            mContext.startActivity(commentintent)
        }

        return rowMain
    }

    override fun getCount(): Int {
      return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }
}
data class Posts(val id: String, val comments: Any?, val data: Any?, val likes: Any?,val date:com.google.firebase.Timestamp){
    val c = comments
    val d = data
    val i = id
    val l = likes
    val da=date
}


