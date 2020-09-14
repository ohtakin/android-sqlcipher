package com.tioh.sqlcipher

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseOpenHelperTest {

    private lateinit var dbHelper: DatabaseOpenHelper

    @Before
    fun setup() {
        dbHelper = DatabaseOpenHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        dbHelper.clearDbAndRecreate()
    }

    @After
    fun tearDown() {
        dbHelper.clearDb()
    }

    @Test
    fun testDbInsertion() {

        val user = User(1, "ti", "oh")
        val user2 = User(2, "ti2", "oh2")

        dbHelper.insertAll(user, user2)

        assertThat(dbHelper.getAllUser(), equalTo(listOf(user, user2)))
    }
}
