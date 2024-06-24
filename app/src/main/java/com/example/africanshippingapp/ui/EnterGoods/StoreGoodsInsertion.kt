package com.example.africanshippingapp.ui.EnterGoods

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.africanshippingapp.R
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.africanshippingapp.GoodsModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StoreGoodsInsertion : AppCompatActivity() {

    private lateinit var etGoodsName: EditText
    private lateinit var etGoodsNo: EditText
    private lateinit var btnSaveData: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var dbRef: DatabaseReference
    private lateinit var radioGroup2: RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store_goods_insertion)

        etGoodsName = findViewById(R.id.etGoodsName)
        etGoodsNo = findViewById(R.id.etGoodsNo)
        btnSaveData = findViewById(R.id.btnSave)
        radioGroup = findViewById(R.id.radioGroup)
        radioGroup2= findViewById(R.id.radioGroup2)


        dbRef = FirebaseDatabase.getInstance().getReference("june_store")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val GoodName = etGoodsName.text.toString()
        val GoodsNo = etGoodsNo.text.toString()
        var StoreNo = ""
        var deliveryStatus =""

        val selectedId = radioGroup.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedId)
        StoreNo = radioButton.text.toString()

        val selectedId2 = radioGroup2.checkedRadioButtonId
        val radioButton2 = findViewById<RadioButton>(selectedId2)
        deliveryStatus = radioButton2.text.toString()

        if (GoodsNo.isEmpty()) {
            etGoodsNo.error = "Please enter Goods Number"
        }

        val empId = dbRef.push().key!!

        val employee = GoodsModel(empId, GoodName, GoodsNo, StoreNo, deliveryStatus)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etGoodsName.text.clear()
                etGoodsNo.text.clear()
                radioGroup.clearCheck()
                radioGroup2.clearCheck()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}