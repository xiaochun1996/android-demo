/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jock.check

import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.jock.check.detector.ParseColorDetector
import org.junit.Test

@Suppress("UnstableApiUsage")
class ParseColorDetectorTest {
    @Test
    fun testBasic() {
        lint().files(
            kt(
                """
                    package test.pkg
                    import android.graphics.Color
                    class DemoActivity {
                        fun onCreate() {
                            Color.parseColor("123312")
                        }
                    }
                    """
            ).indented()
        )
            .issues(ParseColorDetector.ISSUE)
            .run()
            .expect(
                """src/test/pkg/DemoActivity.kt:5: Error: Color.parseColor 解析后端下发的值可能导致 crash，请 try catch [ParseColorError]
        Color.parseColor("123312")
              ~~~~~~~~~~
1 errors, 0 warnings
                    """
            )
    }
}
