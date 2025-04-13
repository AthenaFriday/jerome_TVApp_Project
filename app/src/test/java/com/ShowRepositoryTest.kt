package com

import com.android.tvapp.data.Show
import com.android.tvapp.repository.ShowRepository
import com.android.tvapp.ui.TvMazeApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ShowRepositoryTest {

    private lateinit var api: TvMazeApi
    private lateinit var repository: ShowRepository

    @Before
    fun setup() {
        api = mockk()
        repository = ShowRepository(api)
    }

    @Test
    fun `returns cached result on second call`() = runTest {
        val show = Show("Test Show", "2020-01-01", null)
        coEvery { api.searchShow("test") } returns show

        val first = repository.searchShow("test")
        val second = repository.searchShow("test")

        assertTrue(first.isSuccess)
        assertTrue(second.isSuccess)
        coVerify(exactly = 1) { api.searchShow("test") }
    }
}
