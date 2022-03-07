package com.jock.check.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.impl.source.tree.java.MethodElement
import org.jetbrains.annotations.NotNull
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UTryExpression
import org.jetbrains.uast.getParentOfType
import org.jetbrains.uast.kotlin.KotlinUFile
import java.util.*


/**
 * Description: 分析 Color
 * Author: lxc
 * Date: 2022/2/24 13:25
 */
@Suppress("UnstableApiUsage")
class ParseColorDetector : Detector(), Detector.UastScanner {

    override fun getApplicableMethodNames(): List<String> {
        return Collections.singletonList("parseColor")
    }

    override fun visitMethodCall(
        @NotNull context: JavaContext, @NotNull node: UCallExpression,
        @NotNull method: PsiMethod
    ) {
        // 不是 android.graphics.Color 类的方法，直接返回
        if (!context.evaluator.isMemberInClass(method, "android.graphics.Color")) {
            return
        }
        // 参数写死的比如 "#FFFFFF" 这种，简单判断如果是 # 号开头，直接返回
        if (isConstColor(node)) {
            return
        }
        // 已经做了 try catch 处理，直接返回
        if (isWrappedByTryCatch(node, context)) {
            return
        }
        reportError(context, node)
    }

    private fun isConstColor(node: UCallExpression): Boolean {
        return node.valueArguments[0].evaluate().toString().startsWith("#")
    }

    private fun isWrappedByTryCatch(node: UCallExpression, context: JavaContext): Boolean {
        if (context.uastFile is KotlinUFile) {
            // fixed:
            return node.uastParent?.getParentOfType(UTryExpression::class.java) != null
        }
        var parent = node.sourcePsi!!.parent
        while (parent != null && parent !is MethodElement) {
            if (parent is PsiTryStatement) {
                return true
            }
            parent = parent.parent
        }
        return false
    }

    private fun reportError(context: JavaContext, node: UCallExpression) {
        context.report(
            ISSUE,
            node,
            context.getCallLocation(node, includeReceiver = false, includeArguments = false),
            "Color.parseColor 解析后端下发的值可能导致 crash，请 try catch"
        )
    }

    companion object {
        private val IMPLEMENTATION: Implementation =
            Implementation(ParseColorDetector::class.java, Scope.JAVA_FILE_SCOPE)
        val ISSUE = Issue.create(
            id = "ParseColorError",
            briefDescription = "`Color.parseColor` 解析可能 crash",
            explanation = "后端下发的色值可能无法解析，导致 crash",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = IMPLEMENTATION
        )
            .setAndroidSpecific(true)
    }
}