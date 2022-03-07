package com.jock.lint_demo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Description: Lint 测试 Activity
 * Author: lxc
 * Date: 2022/2/28 11:06
 */
class LintTestActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 颜色报错
//        Color.parseColor("1234565")
//        // lint 提示
//        val s = "lint"
//
//        Log.d("Test", "This is a test log")
    }
}