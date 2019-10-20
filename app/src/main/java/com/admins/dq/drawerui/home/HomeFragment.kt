package com.admins.dq.drawerui.home

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.drawerui.home.adapter.PatientsRecyclerAdapter
import com.admins.dq.model.oldModel.Patient
import com.admins.dq.utils.MyLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var dataAdapter: PatientsRecyclerAdapter

    val list = arrayListOf(
        Patient("Hady1"),
        Patient("Hady2"),
        Patient("Hady3"),
        Patient("Hady4")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        dataAdapter = PatientsRecyclerAdapter(
            this@HomeFragment.context!!,
            list
        )

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val linearLayoutManager = MyLinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_get_all.layoutManager = linearLayoutManager
        rv_get_all.adapter = dataAdapter

        pullToRefresh.setOnRefreshListener {
            rv_get_all.isEnabled = true
            Handler().postDelayed({
                //use the network call
                pullToRefresh.isRefreshing = false
            }, 500)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = inflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query))
                    search(query)
                else {
//                    list.clear()
//                    pullToRefresh.isRefreshing = true
//                    mViewModel.getAll()
//                    dataAdapter.notifyDataSetChanged()
                }

//                dataAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText))
                    search(newText)
                else {
//                    list.clear()
//                    dataAdapter.notifyDataSetChanged()
//                    pullToRefresh.isRefreshing = true
//                    mViewModel.getAll()
                }

//                dataAdapter.notifyDataSetChanged()
                return true
            }
        })
    }


    private fun search(s: String?) {
        /*   dataAdapter.search(s) {
               // update UI on nothing found
               Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show()
               pullToRefresh.isRefreshing = true
               mViewModel.getAll()
           }*/
    }
}