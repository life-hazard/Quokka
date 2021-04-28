package com.app.quokka

import com.app.quokka.logged.AddTaskActivity
import com.app.quokka.tasks.FullUserTaskActivity
import kotlinx.coroutines.handleCoroutineException
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun dateToMap_isCorrect() {
        assertEquals(
            mapOf("day" to 10, "month" to 11, "year" to 2021), AddTaskActivity.dateToTimeMap("10.11.2021")
        )

        assertEquals(
            mapOf("day" to 1, "month" to 11, "year" to 2021), AddTaskActivity.dateToTimeMap("01.11.2021")
        )

        assertEquals(
            mapOf("day" to 10, "month" to 1, "year" to 2021), AddTaskActivity.dateToTimeMap("10.01.2021")
        )

        assertEquals(
            mapOf("day" to 1, "month" to 1, "year" to 2021), AddTaskActivity.dateToTimeMap("01.01.2021")
        )
    }

    @Test
    fun mapToDate_isCorrect() {
        assertEquals("01.02.3333", AddTaskActivity.mapToDate(1, 2, 3333))
    }

    @Test
    fun date_isValid() {
        assertEquals(
            true,
            FullUserTaskActivity.isDateValid(
                mapOf("day" to 10, "month" to 11, "year" to 2021),
                mapOf("day" to 10, "month" to 11, "year" to 2021)
            )
        )

        assertEquals(
            true,
            FullUserTaskActivity.isDateValid(
                mapOf("day" to 1, "month" to 11, "year" to 2021),
                mapOf("day" to 10, "month" to 11, "year" to 2021)
            )
        )

        assertEquals(
            true,
            FullUserTaskActivity.isDateValid(
                mapOf("day" to 10, "month" to 9, "year" to 2021),
                mapOf("day" to 10, "month" to 11, "year" to 2021)
            )
        )

        assertEquals(
            false,
            FullUserTaskActivity.isDateValid(
                mapOf("day" to 10, "month" to 11, "year" to 2021),
                mapOf("day" to 10, "month" to 11, "year" to 2000)
            )
        )
    }

}
