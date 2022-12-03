package com.pratiksha.bookhub.database

import androidx.room.*

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks() : List <BookEntity>

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    fun getBookByID(bookId: String): BookEntity?

    @Query("SELECT EXISTS(SELECT * FROM books WHERE book_id = :bookId)")
    fun bookExists(bookId: Int): Boolean
}