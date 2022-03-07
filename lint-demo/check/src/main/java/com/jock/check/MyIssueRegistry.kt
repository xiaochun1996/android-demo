package com.jock.check

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.jock.check.detector.AndroidLogDetector
import com.jock.check.detector.FixOrientationTransDetector
import com.jock.check.detector.SampleCodeDetector
import com.jock.check.detector.ParseColorDetector

/**
 * Description: 注册检查表
 * Author: lxc
 * Date: 2022/2/24 13:05
 */
@Suppress("UnstableApiUsage")
class MyIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(ParseColorDetector.ISSUE, SampleCodeDetector.ISSUE,
            FixOrientationTransDetector.ISSUE,AndroidLogDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = 8 // works with Studio 4.1 or later; see com.android.tools.lint.detector.api.Api / ApiKt

    override val vendor: Vendor = Vendor(
        vendorName = "Android Open Source Project",
        feedbackUrl = "https://github.com/googlesamples/android-custom-lint-rules/issues",
        contact = "https://github.com/googlesamples/android-custom-lint-rules"
    )
}