package com.codemobile.mobilephonebuyersguideapp

import com.codemobile.mobilephonebuyersguideapp.interfaces.MainActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter.Companion.ALERTDIALOG_SORT_HIGH2LOW
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter.Companion.ALERTDIALOG_SORT_LOW2HIGH
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter.Companion.ALERTDIALOG_SORT_RATING
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import com.nhaarman.mockitokotlin2.*
import mockit.Deencapsulation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MobileActivityPresenterTest {

    @Mock
    lateinit var testView: MainActivityPresenterInterface

    @Mock
    lateinit var service: MobileApiService

    @InjectMocks
    lateinit var presenter: MainActivityPresenter

    private fun getMockMobile(
        id: Int = 1,
        price: Double = 100.0,
        rating: Double = 3.0,
        name: String = "any Name",
        favourite: Boolean = false
    ) =
        Mobile("any brand", "any description", id, name, price, rating, "any Url", favourite)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetMobileList() {
        //given
        whenever(service.getMobileList()).thenReturn(mock())

        //when
        presenter.loadNewData()

        //then
        verify(service).getMobileList()
    }

    @Test
    fun testGetMobileListOnFailure() {
        //given
        val call = mock<Call<List<Mobile>>>()
        whenever(service.getMobileList()).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<Mobile>>>(0).onFailure(mock(), mock())
        }

        //when
        presenter.loadNewData()

        //then
        verify(testView).showErrorMessage(anyString())
        // verifyNoMoreInteractions(testView)
    }

    @Test
    fun testGetMobileListApiResponseBodyNull() {
        //given
        val call = mock<Call<List<Mobile>>>()
        whenever(service.getMobileList()).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<Mobile>>>(0).onResponse(mock(), Response.success(null))
        }

        //when
        presenter.loadNewData()

        //then
        verifyZeroInteractions(testView)
    }

    @Test
    fun testGetMobileListApiResponseBodyEmptyList() {
        //given
        val mockMobile1 = getMockMobile()
        val mockFavouriteList = listOf(mockMobile1)
        val call = mock<Call<List<Mobile>>>()
        whenever(service.getMobileList()).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<Mobile>>>(0).onResponse(mock(), Response.success(listOf()))
        }
        Deencapsulation.setField(presenter, "mFavouriteList", mockFavouriteList)

        //when
        presenter.loadNewData()

        //then
        verifyZeroInteractions(testView)
    }

    @Test
    fun testGetMobileListApiResponseBodyNoMatchIdWithFacouriteList() {
        //given
        val mockMobile1 = getMockMobile(id = 1)
        val mockMobile2 = getMockMobile(id = 2)
        val mockFavouriteList = listOf(mockMobile1)
        val responseData = listOf(mockMobile2)
        val call = mock<Call<List<Mobile>>>()
        whenever(service.getMobileList()).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<Mobile>>>(0).onResponse(mock(), Response.success(responseData))
        }
        Deencapsulation.setField(presenter, "mFavouriteList", mockFavouriteList)

        //when
        presenter.loadNewData()

        //then
        verify(testView).updateData(responseData, mockFavouriteList)
    }

    @Test
    fun testGetMobileListApiResponseBodyHaveMatchIdWithFavouriteList() {
        //given
        val mockMobile1 = getMockMobile(id = 1, favourite = true)
        val mockMobile2 = getMockMobile(id = 1, favourite = false)
        val mockFavouriteList = listOf(mockMobile1)
        val responseData = listOf(mockMobile2)
        val call = mock<Call<List<Mobile>>>()
        whenever(service.getMobileList()).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<Mobile>>>(0).onResponse(mock(), Response.success(responseData))
        }
        Deencapsulation.setField(presenter, "mFavouriteList", mockFavouriteList)

        //when
        presenter.loadNewData()

        //then
        verify(testView).updateData(mockFavouriteList, mockFavouriteList)
    }

    @Test
    fun testSortPriceLowToHigh() {
        //given
        val mockMobile1 = getMockMobile(price = 100.00)
        val mockMobile2 = getMockMobile(price = 150.00)
        val mockMobile3 = getMockMobile(price = 200.00)
        val expectData = listOf(mockMobile1, mockMobile2, mockMobile3)
        val inputData = listOf(mockMobile2, mockMobile3, mockMobile1)
        val outputMobileData: List<Mobile>
        val outputFavouriteData: List<Mobile>
        Deencapsulation.setField(presenter, "mMobileList", inputData)
        Deencapsulation.setField(presenter, "mFavouriteList", inputData)

        //when
        presenter.handleSort(ALERTDIALOG_SORT_LOW2HIGH)

        //then
        outputMobileData = Deencapsulation.getField(presenter, "mMobileList")
        outputFavouriteData = Deencapsulation.getField(presenter, "mFavouriteList")
        assertEquals(expectData, outputMobileData)
        assertEquals(expectData, outputFavouriteData)
    }

    @Test
    fun testSortPriceHighToLow() {
        //given
        val mockMobile1 = getMockMobile(price = 100.00)
        val mockMobile2 = getMockMobile(price = 150.00)
        val mockMobile3 = getMockMobile(price = 200.00)
        val expectData = listOf(mockMobile3, mockMobile2, mockMobile1)
        val inputData = listOf(mockMobile2, mockMobile3, mockMobile1)
        val outputMobileData: List<Mobile>
        val outputFavouriteData: List<Mobile>
        Deencapsulation.setField(presenter, "mMobileList", inputData)
        Deencapsulation.setField(presenter, "mFavouriteList", inputData)

        //when
        presenter.handleSort(ALERTDIALOG_SORT_HIGH2LOW)

        //then
        outputMobileData = Deencapsulation.getField(presenter, "mMobileList")
        outputFavouriteData = Deencapsulation.getField(presenter, "mFavouriteList")
        assertEquals(expectData, outputMobileData)
        assertEquals(expectData, outputFavouriteData)
    }

    @Test
    fun testSortRating() {
        //given
        val mockMobile1 = getMockMobile(rating = 1.0)
        val mockMobile2 = getMockMobile(rating = 3.0)
        val mockMobile3 = getMockMobile(rating = 5.0)
        val expectData = listOf(mockMobile3, mockMobile2, mockMobile1)
        val inputData = listOf(mockMobile2, mockMobile3, mockMobile1)
        val outputMobileData: List<Mobile>
        val outputFavouriteData: List<Mobile>
        Deencapsulation.setField(presenter, "mMobileList", inputData)
        Deencapsulation.setField(presenter, "mFavouriteList", inputData)

        //when
        presenter.handleSort(ALERTDIALOG_SORT_RATING)

        //then
        outputMobileData = Deencapsulation.getField(presenter, "mMobileList")
        outputFavouriteData = Deencapsulation.getField(presenter, "mFavouriteList")
        assertEquals(expectData, outputMobileData)
        assertEquals(expectData, outputFavouriteData)
    }

    @Test
    fun testAddFavourite() {
        //given
        val mockMobile = getMockMobile(id = 1, favourite = false)
        val expectMobile = getMockMobile(id = 1, favourite = true)
        val expectMobileData = listOf(expectMobile)
        val expectFavouriteData: List<Mobile> = listOf(expectMobile)
        val inputMobileData = listOf(mockMobile)
        val inputFavouriteData: List<Mobile> = listOf()
        val outputMobileData: List<Mobile>
        val outputFavouriteData: List<Mobile>
        Deencapsulation.setField(presenter, "mMobileList", inputMobileData)
        Deencapsulation.setField(presenter, "mFavouriteList", inputFavouriteData)

        //when
        presenter.setFavourite(mockMobile)

        //then
        outputMobileData = Deencapsulation.getField(presenter, "mMobileList")
        outputFavouriteData = Deencapsulation.getField(presenter, "mFavouriteList")
        assertEquals(expectMobileData, outputMobileData)
        assertEquals(expectFavouriteData, outputFavouriteData)
    }

    @Test
    fun testRemoveFavourite() {
        //given
        val mockMobile = getMockMobile(id = 1, favourite = true)
        val expectMobile = getMockMobile(id = 1, favourite = false)
        val expectMobileData = listOf(expectMobile)
        val expectFavouriteData: List<Mobile> = listOf()
        val inputMobileData = listOf(mockMobile)
        val inputFavouriteData: List<Mobile> = listOf(mockMobile)
        val outputMobileData: List<Mobile>
        val outputFavouriteData: List<Mobile>
        Deencapsulation.setField(presenter, "mMobileList", inputMobileData)
        Deencapsulation.setField(presenter, "mFavouriteList", inputFavouriteData)

        //when
        presenter.setFavourite(mockMobile)

        //then
        outputMobileData = Deencapsulation.getField(presenter, "mMobileList")
        outputFavouriteData = Deencapsulation.getField(presenter, "mFavouriteList")
        assertEquals(expectMobileData, outputMobileData)
        assertEquals(expectFavouriteData, outputFavouriteData)
    }

}
