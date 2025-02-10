package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.favorites.domain.interactor.FavoriteInteractor
import ru.practicum.android.diploma.vacancy.data.converter.VacancyConverter
import ru.practicum.android.diploma.vacancy.data.network.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.network.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.presentation.VacancyScreenState

class VacancyDetailsRepositoryImpl(
    private val networkClient: RetrofitNetworkClient,
    private val mapper: VacancyConverter,
    private val favoriteInteractor: FavoriteInteractor
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: String, isFromFavoritesScreen: Boolean): Flow<VacancyScreenState> = flow {
        if (isFromFavoritesScreen) {
            favoriteInteractor.getVacancyById(vacancyId).collect { vacancyFavorite ->
                if (vacancyFavorite != null) {
                    val data = mapper.mapFavoriteToVacancy(vacancyFavorite)
                    val response = mapper.mapFavoriteToDetails(vacancyFavorite)

                    val apiResponse = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
                    when (apiResponse.resultCode) {
                        Response.SUCCESS_RESPONSE_CODE -> {
                            emit(VacancyScreenState.ContentState(data, response))
                        }
                        Response.BAD_REQUEST_ERROR_CODE, Response.NOT_FOUND_ERROR_CODE -> {
                            favoriteInteractor.deleteVacancyById(vacancyId)
                            emit(VacancyScreenState.EmptyState)
                        }
                        Response.NO_INTERNET_ERROR_CODE -> {
                            emit(VacancyScreenState.ContentState(data, response))
                        }
                        else -> {
                            emit(VacancyScreenState.ServerError)
                        }
                    }
                }
            }
        } else {
            val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
            when (response.resultCode) {
                Response.SUCCESS_RESPONSE_CODE -> {
                    val result = response as VacancyDetailsResponse
                    if (result.id.isEmpty()) {
                        emit(VacancyScreenState.EmptyState)
                    } else {
                        val data = mapper.map(response)
                        emit(VacancyScreenState.ContentState(data, response))
                    }
                }
                Response.BAD_REQUEST_ERROR_CODE, Response.NOT_FOUND_ERROR_CODE -> {
                    emit(VacancyScreenState.EmptyState)
                }
                Response.NO_INTERNET_ERROR_CODE -> {
                    emit(VacancyScreenState.ConnectionError)
                }
                else -> {
                    emit(VacancyScreenState.ServerError)
                }
            }
        }
    }.flowOn(Dispatchers.IO)

}
