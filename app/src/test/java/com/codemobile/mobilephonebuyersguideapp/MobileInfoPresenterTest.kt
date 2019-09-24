package com.codemobile.mobilephonebuyersguideapp

import com.codemobile.mobilephonebuyersguideapp.interfaces.MobileInfoActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import com.codemobile.mobilephonebuyersguideapp.presenter.MobileInfoActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import com.nhaarman.mockitokotlin2.*
import mockit.Deencapsulation
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
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
    lateinit var testView: MobileInfoActivityPresenterInterface

    @Mock
    lateinit var service: MobileApiService

    @InjectMocks
    lateinit var presenter: MobileInfoActivityPresenter

    private fun getMockMobile(
        id: Int = 1,
        price: Double = 100.0,
        rating: Double = 3.0,
        name: String = "any Name",
        favourite: Boolean = false
    ) =
        Mobile("any brand", "any description", id, name, price, rating, "any Url", favourite)

    private fun getMockImage() = MobileImage(1, 1, "https://www.any.jpg")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val mockMobile = getMockMobile()
        Deencapsulation.setField(presenter, "mMobile", mockMobile)
    }

    @Test
    fun testGetMobileImage() {
        //given
        whenever(service.getMobileImage(anyInt())).thenReturn(mock())

        //when
        presenter.loadMobileImages()

        //then
        verify(service).getMobileImage(anyInt())
    }

    @Test
    fun testGetMobileListOnFailure() {
        //given
        val call = mock<Call<List<MobileImage>>>()
        whenever(service.getMobileImage(anyInt())).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<MobileImage>>>(0).onFailure(mock(), mock())
        }

        //when
        presenter.loadMobileImages()

        //then
        verify(testView).showErrorMsg(anyString())
    }

    @Test
    fun testGetMobileListApiResponseBodyNull() {
        //given
        val call = mock<Call<List<MobileImage>>>()
        whenever(service.getMobileImage(anyInt())).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<MobileImage>>>(0).onResponse(mock(), Response.success(null))
        }

        //when
        presenter.loadMobileImages()

        //then
        verifyZeroInteractions(testView)
    }

    @Test
    fun testGetMobileListApiResponseBodyEmptyList() {
        //given
        val call = mock<Call<List<MobileImage>>>()
        whenever(service.getMobileImage(anyInt())).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<MobileImage>>>(0).onResponse(mock(), Response.success(listOf()))
        }

        //when
        presenter.loadMobileImages()

        //then
        verifyZeroInteractions(testView)
    }

    @Test
    fun testGetMobileListApiResponseBodyNoMatchIdWithFacouriteList() {
        //given
        val mockMobileImage = getMockImage()
        val response = listOf(mockMobileImage)
        val call = mock<Call<List<MobileImage>>>()
        whenever(service.getMobileImage(anyInt())).thenReturn(call)
        whenever(call.enqueue(any())).thenAnswer {
            it.getArgument<Callback<List<MobileImage>>>(0).onResponse(mock(), Response.success(response))
        }

        //when
        presenter.loadMobileImages()

        //then
        verify(testView).setViewPagerAdapter(response)
    }

}
