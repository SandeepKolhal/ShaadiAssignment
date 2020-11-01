package com.sk.shaadiassignment.repositories

import com.sk.shaadiassignment.data.dao.MatcherUserDao
import com.sk.shaadiassignment.model.Result

class MatchUserRepositories(private val dao: MatcherUserDao) {

    /**
     * insert list of users in database
     *
     * @param list
     */
    suspend fun insertData(list: List<Result>) {
        dao.insertMatchUsersList(list)
    }

    /**
     * update accepted and decline status of the user
     *
     * @param user
     */
    suspend fun updateUser(user: Result) {
        dao.updateUser(user)
    }

    /**
     * get match users from database
     *
     * @param limit
     * @param offset
     * @return
     */
    suspend fun getUsers(limit: Int, offset: Int): List<Result> {
        return dao.getMatchUsers(limit, offset)
    }

    suspend fun getDeclineRejectedStatus(uuid: String): Int {
        return dao.getDeclineRejectedStatus(uuid)
    }
}