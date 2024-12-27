package com.example.nhahang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DangKi : AppCompatActivity() {
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var editXacNhanMatKhau: EditText
    private lateinit var editName: EditText
    private lateinit var editGmail: EditText
    private lateinit var btnDangKi: Button
    private lateinit var btnQuayLai: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ki)
        setControlAndEvent()
    }

    fun setControlAndEvent() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "users"
        ).build()
        val userDao = db.userDao()
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        editXacNhanMatKhau = findViewById(R.id.editXacNhanMatKhau)
        editName = findViewById(R.id.editName)
        editGmail = findViewById(R.id.editGmail)
        btnDangKi = findViewById(R.id.btnDangKi)
        btnQuayLai = findViewById(R.id.btnQuayLaiLogin)

        btnQuayLai.setOnClickListener {
            finish()
        }
        btnDangKi.setOnClickListener {
            if (editUsername.length() == 0 || editPassword.length() == 0 || editXacNhanMatKhau.length() == 0 || editName.length() == 0 || editGmail.length() == 0) {
                Toast.makeText(this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show()
            } else if (editPassword.text.toString() != editXacNhanMatKhau.text.toString()) {
                Toast.makeText(this, "Mật khẩu không trùng nhau!", Toast.LENGTH_SHORT).show()
            } else if (checkGmail(editGmail.text.toString())) {
                val newUser = User(
                    username = editUsername.text.toString(),
                    password = editPassword.text.toString(),
                    quyen = "",
                    name = editName.text.toString(),
                    gmail = editGmail.text.toString()
                )
                // Chạy trong Coroutine để thêm vào database
                CoroutineScope(Dispatchers.IO).launch {
                    val checkGmail = userDao.findUserByEmail(editGmail.text.toString())
                    val checkUsername = userDao.kiemTraUsername(editUsername.text.toString())
                    withContext(Dispatchers.Main) {
                        if (checkUsername > 0) {
                            Toast.makeText(this@DangKi, "Username đã tồn tại!", Toast.LENGTH_SHORT)
                                .show()
                        } else if (checkGmail == null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                userDao.insertUser(newUser)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@DangKi,
                                        "Đăng ký thành công!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@DangKi, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@DangKi,
                                "Email đã được đăng kí!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Gmail không đúng định dạng!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkGmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }
}