package com.pratiksha.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.pratiksha.bookhub.R
import com.pratiksha.bookhub.adapter.FavouriteRecyclerAdapter
import com.pratiksha.bookhub.database.BookDatabase
import com.pratiksha.bookhub.database.BookEntity
import com.pratiksha.bookhub.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    lateinit var binding: FragmentFavouritesBinding
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if (activity != null){
            binding.favProgressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            binding.recyclerFavourite.adapter = recyclerAdapter
            binding.recyclerFavourite.layoutManager = layoutManager
        }

        return binding.root
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            return db.bookDao().getAllBooks()

        }

    }
}