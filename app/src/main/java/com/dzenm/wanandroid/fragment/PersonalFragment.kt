package com.dzenm.wanandroid.fragment

import android.content.Intent
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.activities.CollectActivity
import com.dzenm.wanandroid.activities.LoginActivity
import com.dzenm.wanandroid.base.BaseFragment
import com.dzenm.wanandroid.util.deleteLoginFile
import com.dzenm.wanandroid.util.readUser
import com.dzenm.wanandroid.util.setLoginState
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_personal

    override fun lazyPrepareFetchData() {
        val id_card = rootView.findViewById<TextView>(R.id.id_card)
        val username = rootView.findViewById<TextView>(R.id.username)
        val email = rootView.findViewById<TextView>(R.id.email)

        val id_layout = rootView.findViewById<LinearLayout>(R.id.id_layout)
        val username_layout = rootView.findViewById<LinearLayout>(R.id.username_layout)
        val email_layout = rootView.findViewById<LinearLayout>(R.id.email_layout)
        val collect = rootView.findViewById<TextView>(R.id.collect)
        val about = rootView.findViewById<TextView>(R.id.about)
        val logout = rootView.findViewById<TextView>(R.id.logout)

        // 点击事件
        id_layout.setOnClickListener { }
        username_layout.setOnClickListener { }
        email_layout.setOnClickListener { }
        collect.setOnClickListener {
            val intent = Intent(activity, CollectActivity::class.java)
            startActivity(intent)
        }
        about.setOnClickListener { }

        // 点击进行注销
        logout.setOnClickListener {
            com.dzenm.wanandroid.api.Api.getDefaultService().logout().enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    clearLocalLogoutData(response)
                }
            })
        }

        // 读取缓存数据
        val userModels = readUser(context!!)

        val id = userModels?.get(0)?.id.toString()
        val user = userModels?.get(0)?.username

        if (!TextUtils.isEmpty(id) || !"0".equals(id)) {
            id_card.setText(id)
        }
        if (!TextUtils.isEmpty(user)) {
            username.setText(user)
        }
    }

    /**
     * 清楚本地数据
     */
    fun clearLocalLogoutData(response: Response<String>) {
        val json = response.body()
        val jsonObject = JSONObject(json)
        val errorCode = jsonObject.optInt("errorCode")
        val errorMsg = jsonObject.optString("errorMsg")
        if (errorCode == 0) {                                           // 判断服务端是否注销
            val isDelete = deleteLoginFile()                            // 删除登录记录文件
            if (isDelete) {
                activity?.let { it1 -> setLoginState(it1, false) } // 删除登录状态信息
                startLoginActivity()
            }
        } else {
            Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 打开登录页面
     */
    fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }
}
