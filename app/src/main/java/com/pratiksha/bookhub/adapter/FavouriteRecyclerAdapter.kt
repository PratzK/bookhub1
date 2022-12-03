package com.pratiksha.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
import com.pratiksha.bookhub.R
import com.pratiksha.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val bookList: List<BookEntity>) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViwHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViwHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favorite_single_row, parent, false)

        return FavouriteViwHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViwHolder, position: Int) {
        val book = bookList [position]

        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtBookImage)
    }

    class FavouriteViwHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtBookName: TextView = view.findViewById(R.id.txtFavBookName)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val txtBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val linLayFavPage: LinearLayout = view.findViewById(R.id.linLayFavPage)

    }

}