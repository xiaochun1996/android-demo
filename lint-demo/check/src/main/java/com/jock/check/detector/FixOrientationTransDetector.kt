package com.jock.check.detector

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import org.jetbrains.annotations.NotNull
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.*


/**
 * Description: 检测资源文件
 * Author: lxc
 * Date: 2022/2/24 13:13
 */
@Suppress("UnstableApiUsage")
class FixOrientationTransDetector : Detector(), XmlScanner {

    override fun getApplicableElements(): Collection<String> {
        return listOf(SdkConstants.TAG_ACTIVITY, SdkConstants.TAG_STYLE)
    }

    companion object {

        private val IMPLEMENTATION: Implementation = Implementation(
            FixOrientationTransDetector::class.java, EnumSet.of(
                Scope.MANIFEST,
                Scope.ALL_RESOURCE_FILES
            )
        )

        val ISSUE = Issue.create(
            id = "FixOrientationTransError",
            briefDescription = "不要在 AndroidManifest.xml 文件里同时设置方向和透明主题",
            explanation = "Activity 同时设置方向和透明主题在 Android 8.0 手机会 Crash",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = IMPLEMENTATION
        )
    }

    private val mThemeMap: MutableMap<ElementEntity, String> = HashMap()

    override fun visitElement(@NotNull context: XmlContext, @NotNull element: Element) {
        when (element.tagName) {
            SdkConstants.TAG_ACTIVITY -> if (isFixedOrientation(element)) {
                val theme: String = element.getAttributeNS(
                    SdkConstants.ANDROID_URI,
                    SdkConstants.ATTR_THEME
                )
                if ("@style/Theme.AppTheme.Transparent" == theme) {
                    reportError(context, element)
                } else {
                    // 将主题设置暂存起来
                    mThemeMap[ElementEntity(context, element)] =
                        theme.substring(theme.indexOf('/') + 1)
                }
            }
            SdkConstants.TAG_STYLE -> {
                val styleName: String = element.getAttribute(SdkConstants.ATTR_NAME)
                mThemeMap.forEach { (elementEntity: ElementEntity, theme: String) ->
                    if (theme == styleName) {
                        if (isTranslucentOrFloating(element)) {
                            reportError(
                                elementEntity.context,
                                elementEntity.element
                            )
                        } else if (element.hasAttribute(SdkConstants.ATTR_PARENT)) {
                            // 替换成父主题
                            mThemeMap[elementEntity] =
                                element.getAttribute(SdkConstants.ATTR_PARENT)
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun isFixedOrientation(element: Element): Boolean {
        return when (element.getAttributeNS(SdkConstants.ANDROID_URI, "screenOrientation")) {
            "landscape", "sensorLandscape", "reverseLandscape", "userLandscape", "portrait", "sensorPortrait", "reversePortrait", "userPortrait", "locked" -> true
            else -> false
        }
    }

    private fun isTranslucentOrFloating(element: Element): Boolean {
        var child: Node = element.firstChild
        while (child != null) {
            if (child is Element
                && SdkConstants.TAG_ITEM == child.tagName && child.getFirstChild() != null && SdkConstants.VALUE_TRUE == child.getFirstChild()
                    .nodeValue
            ) {
                when (child.getAttribute(SdkConstants.ATTR_NAME)) {
                    "android:windowIsTranslucent", "android:windowSwipeToDismiss", "android:windowIsFloating" -> return true
                    else -> {
                    }
                }
            }
            child = child.nextSibling
        }
        return "Theme.AppTheme.Transparent" == element.getAttribute(SdkConstants.ATTR_PARENT)
    }

    private fun reportError(context: XmlContext, element: Element) {
        context.report(
            ISSUE,
            element,
            context.getLocation(element),
            "请不要在 AndroidManifest.xml 文件里同时设置方向和透明主题"
        )
    }

    private class ElementEntity(val context: XmlContext, element: Element) {
        private val mElement: Element = element
        val element: Element
            get() = mElement

    }
}

