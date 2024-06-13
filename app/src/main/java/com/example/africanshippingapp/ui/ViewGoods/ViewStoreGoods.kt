package com.example.africanshippingapp.ui.ViewGoods

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.africanshippingapp.GoodsModel
import com.example.africanshippingapp.R
import com.example.africanshippingapp.ViewGoodsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewStoreGoods : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var goodsList: ArrayList<GoodsModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var searchBar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_store_goods)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            recreate()
        }
        searchBar = findViewById(R.id.searchBar)

        val searchButton = findViewById<Button>(R.id.btnSearch)
        searchButton.setOnClickListener {
            val searchQuery = searchBar.text.toString().trim()
            performSearch(searchQuery)
        }


        empRecyclerView = findViewById(R.id.rvGoods)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingDat)

        goodsList = arrayListOf<GoodsModel>()

        getEmployeesData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
    }
}
    private fun getEmployeesData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Store_Goods")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                goodsList.clear()
                //getting data
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(GoodsModel::class.java)
                        goodsList.add(empData!!)
                    }
                    val mAdapter = ViewGoodsAdapter(goodsList)
                    empRecyclerView.adapter = mAdapter

                    //when an item is clicked the all the employee data is displayed in a different page
                    mAdapter.setOnItemClickListener(object : ViewGoodsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@ViewStoreGoods, StoreGoodsDetails::class.java)

                            //put extras
                            intent.putExtra("goodsId", goodsList[position].GoodsId)
                            intent.putExtra("goodsName", goodsList[position].GoodsName)
                            intent.putExtra("goodsNumber", goodsList[position].GoodsNumber)
                            intent.putExtra("storeName", goodsList[position].StoreName)
                            intent.putExtra("deliveryStatus", goodsList[position].deliveryStatus)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun performSearch(query: String) {
        // This is a basic example searching by title. You can modify based on your data structure.
        val queryRef = dbRef.orderByChild("goodsNumber")
            .startAt(query)
            .endAt(query + "\uf8ff")  // Search by starting characters and include special character for wider range

        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle search results here
                if (dataSnapshot.exists()) {
                    val searchResults = arrayListOf<GoodsModel>()
                    for (empSnap in dataSnapshot.children) {
                        val empData = empSnap.getValue(GoodsModel::class.java)
                        searchResults.add(empData!!)
                    }
                    val mAdapter = ViewGoodsAdapter(searchResults)
                    empRecyclerView.adapter = mAdapter

                    //when an item is clicked the all the employee data is displayed in a different page
                    mAdapter.setOnItemClickListener(object : ViewGoodsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ViewStoreGoods, ViewStoreGoods::class.java)

                            //put extras
                            intent.putExtra("goodsId", searchResults[position].GoodsId)
                            intent.putExtra("goodsName", searchResults[position].GoodsName)
                            intent.putExtra("goodsNumber", searchResults[position].GoodsNumber)
                            intent.putExtra("storeName", searchResults[position].StoreName)
                            intent.putExtra("deliveryStatus", searchResults[position].deliveryStatus)
                            startActivity(intent)
                        }
                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                } else {
                    // No data found
                    tvLoadingData.text = "No results found for '$query'"
                    empRecyclerView.visibility = View.GONE
                    tvLoadingData.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}