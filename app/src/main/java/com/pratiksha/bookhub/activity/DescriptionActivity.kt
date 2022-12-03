package com.pratiksha.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pratiksha.bookhub.R
import com.pratiksha.bookhub.database.BookDatabase
import com.pratiksha.bookhub.database.BookEntity
import com.pratiksha.bookhub.databinding.ActivityDescriptionBinding
import com.pratiksha.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    var bookID: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        setSupportActionBar(binding.descriptionPageToolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookID = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (bookID == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookID)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(
                Method.POST,
                url,
                jsonParams,
                Response.Listener {
                    try {
                        Log.d("JsonResp", "$it")
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            binding.descProgressLayout.visibility = View.INVISIBLE

                            val bookImageUrl = bookJsonObject.getString("image")

                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(binding.imgDescBookImage)
                            binding.txtDesBookName.text = bookJsonObject.getString("name")
                            binding.txtDesBookAuthor.text = bookJsonObject.getString("author")
                            binding.txtDesBookPrice.text = bookJsonObject.getString("price")
                            binding.txtDesBookRating.text = bookJsonObject.getString("rating")
                            binding.txtBookDesc.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookID?.toInt() as Int,
                                binding.txtDesBookName.text.toString(),
                                binding.txtDesBookAuthor.text.toString(),
                                binding.txtDesBookPrice.text.toString(),
                                binding.txtDesBookRating.text.toString(),
                                binding.txtBookDesc.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                binding.btnAddToFav.text = "Remove from Favourites"
                                val favColour =
                                    ContextCompat.getColor(applicationContext, R.color.purple_700)
                                binding.btnAddToFav.setBackgroundColor(favColour)
                            } else {
                                binding.btnAddToFav.text = "Add to Favourites"
                                val noFavColour =
                                    ContextCompat.getColor(applicationContext, R.color.orange)
                                binding.btnAddToFav.setBackgroundColor(noFavColour)
                            }

                            binding.btnAddToFav.setOnClickListener {

                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book added to Favourites!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        binding.btnAddToFav.text = "Remove from Favourites"
                                        binding.btnAddToFav.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.purple_700)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occurred...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book removed from favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        binding.btnAddToFav.text = "Add to Favourites"
                                        binding.btnAddToFav.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.orange)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occurred...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some error occurred...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT)
                        .show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "d569c0b85b9f35"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }

    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is in favourite or not
        Mode 2 -> Add book to favourites
        Mode 3 -> Remove book from favourites
        * */
        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    //Check DB if the book is in favourite or not
                    val book: Boolean = db.bookDao().bookExists(bookEntity.book_id)
                    db.close()
                    return book
                }
                2 -> {
                    // Save the book into DB as a favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    // Remove the favourite from book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

}