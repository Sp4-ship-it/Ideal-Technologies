package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.IdealRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Ideal App", appName)
    }

    @Test
    fun `repository provides services and finds repair ticket`() {
        val repo = IdealRepository()
        assertTrue(repo.services.value.isNotEmpty())
        assertTrue(repo.hardwareItems.value.isNotEmpty())

        val ticket = repo.findTicket("IT-8492")
        assertNotNull(ticket)
        assertEquals("HP Spectre x360 14\" (2022)", ticket?.deviceModel)
    }
}
