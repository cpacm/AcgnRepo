package com.cpacm.libarch

import org.junit.Test

/**
 *
 * <p>
 *
 * @author cpacm 2018/1/8
 */
class JavaOverWriteTest {

    open class Parent() {
        open fun startApp() {
            print("this is parent")
        }

        fun test(isChild: Boolean) {
            if (isChild) {
                startApp()
            } else {
                this@Parent.startApp()
            }
        }

    }

    class Children : Parent() {
        override fun startApp() {
            print("this is children")
        }
    }

    @Test
    fun overWrite() {
        Children().test(true)
        Children().test(false)
    }
}