package com.example.africanshippingapp.ui.ViewGoods


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.africanshippingapp.GoodsModel
import com.example.africanshippingapp.R
import com.google.firebase.database.FirebaseDatabase

class StoreGoodsDetails : AppCompatActivity() {
    private lateinit var tvGoodsId: TextView
    private lateinit var tvGoodsName: TextView
    private lateinit var tvGoodsNumber: TextView
    private lateinit var tvStoreName: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var radioGroup2: RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store_goods_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("goodsId").toString(),
                intent.getStringExtra("goodsName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("goodsId").toString()
            )
        }

    }

    private fun initView() {
        //initializing
        tvGoodsId = findViewById(R.id.tvGoodsId)
        tvGoodsName = findViewById(R.id.tvGoodsName)
        tvGoodsNumber = findViewById(R.id.tvGoodsNumber)
        tvStoreName = findViewById(R.id.tvStoreName)
        radioGroup2 = findViewById(R.id.radioGroup2)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvGoodsId.text = intent.getStringExtra("goodsId")
        tvGoodsName.text = intent.getStringExtra("goodsName")
        tvGoodsNumber.text = intent.getStringExtra("goodsNumber")
        tvStoreName.text = intent.getStringExtra("storeName")

        val deliveryStatus = intent.getStringExtra("deliveryStatus")
        when (deliveryStatus) {
            "Collected" -> radioGroup2.check(R.id.radio3)
            "Not Collected" -> radioGroup2.check(R.id.radio4)

        }
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("june_store").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Data deleted", Toast.LENGTH_LONG).show()
            //after deleting goes back to recycler view/fetching activity
            val intent = Intent(this, ViewStoreGoods::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun openUpdateDialog(
        id: String,
        goodsName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)
//updating
        val etGoodsName = mDialogView.findViewById<EditText>(R.id.upGoodsName)
        val etGoodsNumber = mDialogView.findViewById<EditText>(R.id.upGoodsNumber)
        val etStoreName = mDialogView.findViewById<EditText>(R.id.upStore)
        val radioGroup2 = mDialogView.findViewById<RadioGroup>(R.id.radioGroup2)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)
//setting to new values
        etGoodsName.setText(intent.getStringExtra("goodsName").toString())
        etGoodsNumber.setText(intent.getStringExtra("goodsNumber").toString())
        etStoreName.setText(intent.getStringExtra("storeName").toString())

        val deliveryStatus = intent.getStringExtra("deliveryStatus")
        when (deliveryStatus) {
            "Collected" -> radioGroup2.check(R.id.radio3)
            "Not Collected" -> radioGroup2.check(R.id.radio4)

        }

        var deliveryStatusSelected = ""

        mDialog.setTitle("Updating $goodsName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()
//btn submitting updates
        btnUpdateData.setOnClickListener {
            val selectedId = radioGroup2.checkedRadioButtonId
            val radioButton = mDialogView.findViewById<RadioButton>(selectedId)
            deliveryStatusSelected = radioButton.text.toString()

            updateEmpData(
                id,
                etGoodsName.text.toString(),
                etGoodsNumber.text.toString(),
                etStoreName.text.toString(),
                deliveryStatusSelected
            )

            Toast.makeText(applicationContext, "Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvGoodsName.text = etGoodsName.text.toString()
            tvGoodsNumber.text = etGoodsNumber.text.toString()
            tvStoreName.text = etStoreName.text.toString()
            val deliveryStatus = intent.getStringExtra("deliveryStatus")
            when (deliveryStatus) {
                "Collected" -> radioGroup2.check(R.id.radio3)
                "Not Collected" -> radioGroup2.check(R.id.radio4)

            }
            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        goodsId: String,
        goodsName: String,
        goodsNumber: String,
        storeName: String,
        deliveryStatus: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("june_store").child(goodsId)
        val goodsInfo = GoodsModel(goodsId, goodsName,goodsNumber, storeName, deliveryStatus)
        dbRef.setValue(goodsInfo)
    }
}