package com.sk.shaadiassignment.data.dao

import androidx.room.*
import com.sk.shaadiassignment.model.Result

/**
 * It is used to query Users stored in matcherUsers in database
 *
 */
@Dao
interface MatcherUserDao {

    /**
     * insert user list in database
     * @param list
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMatchUsersList(list: List<Result>)


    /**
     * Update accepted or decline user
     * @param user
     */
    @Update
    suspend fun updateUser(user: Result)

    /**
     * Query to get match users with limit and offset
     * @return
     */
    @Query("SELECT * FROM matcherUsers LIMIT :limit OFFSET :offset")
    suspend fun getMatchUsers(limit: Int, offset: Int): List<Result>

    /**
     * Query to return user's declined and accepted status
     *
     * @param uuid
     * @return
     */
    @Query("select declineRejectedStatus from matcherUsers where user_id = :uuid")
    suspend fun getDeclineRejectedStatus(uuid: String): Int
}