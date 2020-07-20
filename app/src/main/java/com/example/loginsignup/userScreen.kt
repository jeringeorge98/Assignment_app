package com.example.loginsignup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.like.LikeButton
import com.like.OnLikeListener
import kotlin.random.Random

private lateinit var db: FirebaseFirestore

class userScreen : AppCompatActivity() {

    private var posts: ArrayList<Posts> = ArrayList()
    private lateinit var auth: FirebaseAuth

    init {
        var uid = FirebaseAuth.getInstance().currentUser?.email
        db= FirebaseFirestore.getInstance()
        db.collection("/posts")
            .get()
            .addOnSuccessListener { result ->
                val post=result
                for (document in result) {
                    val id = document.id
                    val comm = document.data["comments"]
                    val dta = document.data["data"]
                    val like = document.data["likes"]
                    val date=document.data["Date"] as com.google.firebase.Timestamp
                    val temp = Posts(id, comm, dta, like,date)

                    posts.add(temp)
                }
                var listview =findViewById<ListView>(R.id.user_list_view)
                listview.adapter=UserAdapter(this,posts)
            }
            .addOnFailureListener { exception ->
                Log.i("info", "Error getting documents.", exception)
            }

        Log.i("info", uid.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_screen)
        val toolbar =findViewById<Toolbar>(R.id.toolbar)
        auth = FirebaseAuth.getInstance()
        setSupportActionBar(toolbar)


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

                Toast.makeText(applicationContext,"Signed out succesfully",Toast.LENGTH_LONG).show()
                val homeintent= Intent(this,MainActivity::class.java)
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(homeintent)
                finishAffinity()
            }
        }
        return false
    }
}

private class UserAdapter(context: Context, private val dataList: ArrayList<Posts> = ArrayList()):BaseAdapter(){
    private val mContext:Context
    init{
        mContext=context

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var dataItem=dataList[position]
        val layoutinflater= LayoutInflater.from(mContext)

        val rowMain=layoutinflater.inflate(R.layout.user_rows,parent,false)
        rowMain.findViewById<TextView>(R.id.user_text_View).text="Post ${position+1}"
        rowMain.findViewById<TextView>(R.id.user_body_View2).text= dataItem.data.toString()
        val submitBtn = rowMain.findViewById<Button>(R.id.submitComment)
        val commentText = rowMain.findViewById<EditText>(R.id.comment).text


        val showComments = rowMain.findViewById<TextView>(R.id.showComments)
        showComments?.setOnClickListener(){
            var id = dataList[position].id.toString()
            var commentintent= Intent(mContext, Comment_screen::class.java)
            var dbColl = db.collection("posts").document(id)
            commentintent.putExtra("id",id)
            mContext.startActivity(commentintent)
        }

        val likeBtn = rowMain.findViewById<LikeButton>(R.id.like_button)
        likeBtn.setOnLikeListener(object:OnLikeListener{
            var id = dataList[position].id.toString()
            var dbColl = db.collection("posts").document(id)
            var userid = FirebaseAuth.getInstance().currentUser
            var uid = userid?.uid

            override fun liked(likebtn: LikeButton){
                dbColl.update("likes", FieldValue.arrayUnion(userid?.uid))
                    .addOnSuccessListener { Log.i("info","Liked by user ") }
                    .addOnFailureListener { Log.i("info","didnt like") }
            }
            override fun unLiked(likeButton: LikeButton?) {
                dbColl.update("likes", FieldValue.arrayRemove(userid?.uid))
                    .addOnSuccessListener { Log.i("info","Removed user ") }
                    .addOnFailureListener { Log.i("info","couldnt un like") }
            }
        })

        submitBtn?.setOnClickListener(){
            var id = dataList[position].id.toString()
            var dbColl = db.collection("posts").document(id)
            var commentintent= Intent(mContext, Comment_screen::class.java)
            var comment=Bundle()

            dbColl.update("comments", FieldValue.arrayUnion(commentText.toString()) )
                .addOnSuccessListener {
                    Log.i("info", "success comment"+dataItem.comments)
                           // going to comment screen
                           commentintent.putExtra("id",id)
                   mContext.startActivity(commentintent)
                 }
                .addOnFailureListener { Log.i("info", "failed comment") }
        }

        return rowMain
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        Log.i("no","doesnothing")
        return 1
    }

}


