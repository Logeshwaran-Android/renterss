package com.rent.renters.app.ui.inbox


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.MessageResponse
import com.rent.renters.app.data.model.MessagesItem
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.base.PaginationScrollListener
import com.rent.renters.app.ui.userHomePage.SearchSpaceActivity
import kotlinx.android.synthetic.main.fragment_travelling_inbox.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 */
class TravellingInboxFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var mMessageList : ArrayList<MessagesItem> = ArrayList()

    private var mInboxDetailsAdapter: InboxDetailsAdapter? = null
    private var mPage: Int = 1
    private var mTotalItemCount: Int = 1

    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travelling_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }



    private fun initView() {

        imgBtnBack.visibility = View.GONE
        tvTitle.text = getString(R.string.inbox)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTravellingInbox!!.layoutManager = layoutManager
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            mPage = 1
            callGetInbox()
        }

        rvTravellingInbox?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                if(mPage == mTotalItemCount){
                    isLastPage = true
                }
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true

                mPage += 1
                callGetInbox()

            }
        })
    }

    private fun initViewModel() {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.initMethod(context as Activity)
        callGetInbox()


    }

    private fun callGetInbox() {
        chatViewModel.callGetGuestInbox(mPage).observe(viewLifecycleOwner, Observer<MessageResponse> {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            isLoading = false
            try {
                it.data?.pagination_count?.let {
                    mTotalItemCount = it.toInt()
                }
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            if(mPage == 1)
                mMessageList.clear()
            if(it.status == "1") {
                it?.data?.messages?.let {
                    mMessageList.addAll(it)
                    setTravellingInboxAdapter()
                }
                if(mMessageList.size == 0){
                    swipeRefreshLayout.visibility = View.GONE
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.setOnClickListener{
                        val spaceSizeIntent = Intent(context, SearchSpaceActivity::class.java)
                        startActivity(spaceSizeIntent)
                    }
                }



            }
        })

    }

    private fun setTravellingInboxAdapter() {
        if (mInboxDetailsAdapter == null) {
            mInboxDetailsAdapter = InboxDetailsAdapter(context, mMessageList,false)
            rvTravellingInbox!!.adapter = mInboxDetailsAdapter
        } else {
            mInboxDetailsAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      //  super.onActivityResult(requestCode, resultCode, data)
       // if(resultCode == Activity.RESULT_OK){
            if(requestCode == Iconstants.MESSAGE_REQUEST_CODE){
                mMessageList.clear()
                mPage = 1
                mTotalItemCount = 1
                callGetInbox()
            }

       // }
    }

}
