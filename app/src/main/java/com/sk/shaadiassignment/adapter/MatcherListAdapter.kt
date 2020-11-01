package com.sk.shaadiassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.sk.shaadiassignment.R
import com.sk.shaadiassignment.data.database.AppDatabase
import com.sk.shaadiassignment.model.Result
import com.sk.shaadiassignment.repositories.MatchUserRepositories
import kotlinx.android.synthetic.main.list_item_match.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatcherListAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<MatcherListAdapter.ViewHolder>() {

    private var matcherList = emptyList<Result>()
    private val db = AppDatabase.getDatabase(context)
    private val matchUserDao = db.matchUserDao()
    private val repo = MatchUserRepositories(matchUserDao)

    fun setData(list: List<Result>) {
        matcherList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.userImage
        val profileDescription: TextView = view.profileDescription
        val userName: TextView = view.userName
        val userAddress: TextView = view.userAddress
        val decline: ExtendedFloatingActionButton = view.decline
        val accept: ExtendedFloatingActionButton = view.accept

        init {
            decline.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    if (matcherList[position].declineRejectedStatus == 1) {
                        Toast.makeText(context, "Member already declined", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    matcherList[position].declineRejectedStatus = 1
                    onClickListener.onClick(matcherList[position], position)
                }
            }

            accept.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    if (matcherList[position].declineRejectedStatus == 2) {
                        Toast.makeText(context, "Member already accepted", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    matcherList[position].declineRejectedStatus = 2
                    onClickListener.onClick(matcherList[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_match,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = matcherList[position]

        holder.userName.text = getUserName(data)
        holder.profileDescription.text = getUserAgeGender(data)
        holder.userAddress.text = getUserAddress(data)
        loadUserImage(holder.userImage, data)

        CoroutineScope(Dispatchers.IO).launch {
            val value: Int? = getDeclineAcceptStatus(data)

            withContext(Dispatchers.Main) {
                when (value) {
                    1 -> {
                        holder.decline.text = context.getString(R.string.declined)
                        holder.accept.text = context.getString(R.string.accept)

                        holder.decline.extend()
                        holder.accept.shrink()
                    }
                    2 -> {
                        holder.decline.text = context.getString(R.string.decline)
                        holder.accept.text = context.getString(R.string.accepted)

                        holder.decline.shrink()
                        holder.accept.extend()

                    }
                    else -> {
                        holder.decline.text = context.getString(R.string.decline)
                        holder.accept.text = context.getString(R.string.accept)

                        holder.decline.shrink()
                        holder.accept.shrink()
                    }
                }
            }
        }
    }

    private fun getUserAgeGender(data: Result): String {
        val ageGender = StringBuilder()
        ageGender.append(data.gender).append(", ").append(data.dob?.age)
        return ageGender.toString()
    }

    private suspend fun getDeclineAcceptStatus(data: Result): Int {
        return repo.getDeclineRejectedStatus(data.uuid)
    }

    private fun loadUserImage(userImage: ImageView?, data: Result) {
        userImage?.let {
            Glide.with(context).load(data.picture?.large)
                .thumbnail(Glide.with(context).load(data.picture?.thumbnail))
                .into(it)
        }
    }

    private fun getUserAddress(data: Result): String {
        val description = StringBuilder()
        description.append(data.location?.city).append(", ")
            .append(data.location?.state).append(", ").append(data.location?.country)
        return description.toString()
    }

    private fun getUserName(data: Result): String {
        val name = StringBuilder()
        name.append(data.name?.title).append(". ").append(data.name?.first).append(" ")
            .append(data.name?.last)
        return name.toString()
    }

    override fun getItemCount(): Int = matcherList.size

    interface OnClickListener {
        fun onClick(user: Result, position: Int)
    }

}