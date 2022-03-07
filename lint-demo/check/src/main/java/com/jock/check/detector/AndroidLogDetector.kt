package com.jock.check.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

/**
 * 直接调用 Log 警告
 */
@Suppress("UnstableApiUsage")
class AndroidLogDetector : Detector(), SourceCodeScanner {
    override fun getApplicableMethodNames(): List<String> {
        return listOf("wtf", "v", "d", "i", "w", "e")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, context.getLocation(node), "Don't use Android log directly")
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "AndroidLogDeprecated",
            briefDescription = "Please don't use android log directly, use Timber instead",
            explanation = "Please don't use android log directly, use Timber instead",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(AndroidLogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}