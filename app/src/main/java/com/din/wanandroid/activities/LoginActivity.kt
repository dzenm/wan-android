package com.din.wanandroid.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.din.wanandroid.R
import com.din.wanandroid.api.Api
import com.din.wanandroid.model.UserModel
import com.din.wanandroid.util.getLoginState
import com.din.wanandroid.util.setLoginState
import com.din.wanandroid.util.writeUser
import com.dzenm.helper.dialog.PromptDialog
import com.dzenm.helper.log.Logger
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var loadDialog: PromptDialog
    private var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (getLoginState(this)) {       // 判断是否登录
            Logger.i(this::class.java.simpleName + "已登录过，自动跳转到首页")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        loadDialog = PromptDialog.newInstance(this)

        sign_in_button.setOnClickListener {
            attemptLogin()
        }

        // 注册登录切换
        choose_login.setOnClickListener {
            if (isLogin) {
                choose_login.setText(getString(R.string.text_register))
                sign_in_button.setText(R.string.action_sign_up)
                repassword_view.visibility = View.VISIBLE
                isLogin = false
            } else {
                choose_login.setText(getString(R.string.text_login))
                sign_in_button.setText(R.string.action_sign_in)
                repassword_view.visibility = View.GONE
                isLogin = true
            }
        }
    }

    /**
     * 客户端登录验证
     */
    private fun attemptLogin() {
        // Reset errors.
        username.error = null
        password.error = null
        repassword.error = null

        // Store values at the time of the login attempt.
        val userStr = username.text.toString()
        val passwordStr = password.text.toString()
        val repasswordStr = repassword.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(userStr) && !isFieldValid(userStr)) {
            username.error = getString(R.string.error_not_null_username)
            focusView = username
            cancel = true
        }

        if (!TextUtils.isEmpty(passwordStr) && !isFieldValid(passwordStr)) {
            password.error = getString(R.string.error_length_in_password)
            focusView = password
            cancel = true
        }


        if (!isLogin) {
            if (TextUtils.isEmpty(repasswordStr) && !isFieldValid(repasswordStr)) {
                repassword.error = getString(R.string.error_length_in_password)
                focusView = repassword
                cancel = true
            }
        }

        // 服务端验证输入信息
        if (cancel) {
            focusView?.requestFocus()
        } else {
            loadDialog.showLoadingPoint()
            if (isLogin) {
                startLogin(userStr, passwordStr)
            } else {
                startRegister(userStr, passwordStr, repasswordStr)
            }
        }
    }

    /**
     * POST登录验证
     */
    private fun startLogin(username: String, password: String) {
        Api.getDefaultService()
            .login(username, password)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    loadDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "请求失败", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val json = response.body()
                    val jsonObject = JSONObject(json)
                    val errorCode = jsonObject.optInt("errorCode")
                    val errorMsg = jsonObject.optString("errorMsg")
                    loadDialog.dismiss()
                    if (errorCode == 0) {
                        saveUser(json)
                    } else {
                        Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    /**
     * POST注册验证
     */
    private fun startRegister(username: String, password: String, repassword: String) {
        Api.getDefaultService()
            .register(username, password, repassword)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    loadDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "请求失败", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val json = response.body()
                    val jsonObject = JSONObject(json)
                    val errorCode = jsonObject.optInt("errorCode")
                    val errorMsg = jsonObject.optString("errorMsg")
                    loadDialog.dismiss()
                    if (errorCode == 0) {
                        saveUser(json)
                    } else {
                        Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    /**
     * 存储用户信息
     */
    private fun saveUser(response: String?) {
        val jsonObject = JSONObject(response)
        val userModels = arrayListOf<UserModel>()
        val data = jsonObject.optJSONObject("data")
        userModels.add(
            UserModel(
                data.optString("username"),
                data.optInt("type"),
                data.optString("token"),
                data.optString("password"),
                data.optInt("id"),
                data.optString("icon"),
                data.optString("email")
            )
        )
        writeUser(this, userModels)
        setLoginState(this, true)

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    /**
     * 验证输入字符的长度
     */
    private fun isFieldValid(string: String): Boolean {
        return string.length > 4
    }
}