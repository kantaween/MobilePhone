package com.codemobile.mobilephonebuyersguideapp

import com.codemobile.mobilephonebuyersguideapp.interfaces.MainActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import com.nhaarman.mockitokotlin2.*
import mockit.Deencapsulation
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
class MobileInfoPresenterTest {

    @Mock
    lateinit var testView: MainActivityPresenterInterface

    @Mock
    lateinit var service: MobileApiService

    @InjectMocks
    lateinit var presenter: MainActivityPresenter

    private fun getMockMobile(id:Int = 1, price:Double = 100.0, rating: Double = 3.0, name: String = "any Name", favourite: Boolean = false) =
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


}