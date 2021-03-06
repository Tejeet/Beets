package com.tejeet.beets.repository


import com.nero.mint.data.remote.RetrofitGenerator
import com.tejeet.beets.data.constants.GetAllUserResponseDTO
import com.tejeet.beets.data.modelDTO.FirebaseTokenUpdateResponseDTO
import com.tejeet.beets.data.modelDTO.LikeStoryResponseDTO
import com.tejeet.beets.data.modelDTO.StoriesData
import com.tejeet.beets.data.modelDTO.UserSignupDTO
import com.tejeet.beets.data.modelDTO.upload.StoryUploadResponseDTO
import com.tejeet.beets.data.network.ApiService
import com.tejeet.beets.ui.discover.data.apiClient
import com.tejeet.beets.ui.discover.data.modelClass.DataItem
import com.tejeet.beets.utils.Constants.API_KEY
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@ActivityRetainedScoped
class DataRepository @Inject constructor(private val apiService: ApiService) {

    var ApiClient = RetrofitGenerator.getRetrofitInstance().create(apiClient::class.java)
    val searchKey: String = "4wpLe98F1bo8C45KqdeO8BvP6RlWrtd2"
    val limit: Int = 100
    val language: String = "en"
    val offset: Int = 0
    val rating: String = "g"


    suspend fun getStoriesData(): MutableList<StoriesData> {

        val dataList = apiService.getStory("OK", API_KEY)
        return dataList.body()?.storiesData as MutableList<StoriesData>
    }


    suspend fun getGifs(name: String): MutableList<DataItem?> {

        val data = ApiClient.Search(searchKey, name, limit, offset, rating, language)
        return data.data as MutableList<DataItem?>

    }

    suspend fun uploadStoryVideo(
        userId: String,
        userEmail: String, musicName: String,
        hashTag: String, storyDesc: String, rawFile: File
    ): StoryUploadResponseDTO {

        val filePart = MultipartBody.Part.createFormData(
            "storyVideo",
            rawFile.name, rawFile.asRequestBody()
        )


        val dataList: StoryUploadResponseDTO = apiService.uploadStoryVideo(
            "OK", userId,
            userEmail, API_KEY, musicName, hashTag, storyDesc, filePart
        )

        return dataList
    }

    suspend fun updateFirebaseToken(
        email: String,
        firebaseToekn: String
    ): FirebaseTokenUpdateResponseDTO? {

        val response = apiService.updateFirebaseToken("OK", API_KEY, email, firebaseToekn)
        return response.body()

    }

    suspend fun updateSignUPAccount(
        displayName: String,
        userName : String,
        email : String,
        userProfile : String,
        firebaseToken : String
    ): UserSignupDTO? {

        val response = apiService.signupUser("OK", API_KEY, email, displayName, userName , userProfile, firebaseToken )
        return response.body()

    }


    suspend fun likeStoryPost(
        storyId: String,
        likeCount : String,
        userID : String
    ): LikeStoryResponseDTO? {

        val response = apiService.likeStory("OK", API_KEY, storyId,likeCount, userID)
        return response.body()
    }

    suspend fun getAllUsers(
        userID : String
        ): GetAllUserResponseDTO? {

        val response = apiService.getAllUsers("OK", API_KEY, userID)
        return response.body()
    }


}
