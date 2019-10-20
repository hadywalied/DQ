package com.admins.dq.drawerui.home.adapter

import android.text.TextUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView


abstract class DynamicSearchAdapter<T : DynamicSearchAdapter.Searchable>(
    private var searchableList: ArrayList<T>
) :
    RecyclerView.Adapter<PatientsRecyclerAdapter.MyVH>(), Filterable {
    // Single not-to-be-modified copy of original data in the list.
    private val originalList = searchableList
    // a method-body to invoke when search returns nothing. It can be null.
    private var onNothingFound: (() -> Unit)? = null

    /**
     * Searches a specific item in the list and updates adapter.
     * if the search returns empty then onNothingFound callback is invoked if provided which can be used to update UI
     * @param s the search query or text. It can be null.
     * @param onNothingFound a method-body to invoke when search returns nothing. It can be null.
     */
    fun search(s: String?, onNothingFound: (() -> Unit)?) {
        this.onNothingFound = onNothingFound
        filter.filter(s)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (TextUtils.isEmpty(constraint) || TextUtils.equals(constraint, " ")) {
                    searchableList.addAll(originalList)
                } else {
                    val searchResults =
                        originalList.filter { it.getSearchCriteria().contains(constraint!!, true) }
                    if (searchResults.isNotEmpty()) {
                        searchableList.clear()
                        searchableList.addAll(searchResults)
                    } else searchableList.clear()
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no need to use "results" filtered list provided by this method.
                if (searchableList.isEmpty()) {
                    onNothingFound?.invoke()
                }

                notifyDataSetChanged()

            }
        }
    }

    interface Searchable {
        /** This method will allow to specify a search string to compare against
        your search this can be anything depending on your use case.
         */
        fun getSearchCriteria(): String
    }


}