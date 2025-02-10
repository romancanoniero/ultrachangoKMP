package com.iyr.ultrachango.ui.screens.member

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.database.repositories.FamilyMembersRepository
import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MembersScreenViewModel(
    private val authRepository: AuthRepositoryImpl ,
    private val familyMembersRepository: FamilyMembersRepository,
) : ViewModel(), KoinComponent {



    var state by mutableStateOf(UiState())
        private set


    init {
        getList()
    }

    fun getList() {
        state = state.copy(
            loading = true,
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userKey: String = authRepository.getUserKey()
                val call = familyMembersRepository.getList(userKey).collect {

                    state = state.copy(
                        loading = false,
                        records = it
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    loading = false,
                    showErrorMessage = true,
                    errorMessage = e.message.toString()
                )
            }

        }
    }

    /*
        val buttonSaveEnabled = MutableStateFlow<Boolean>(false)
        val data = MutableStateFlow<ShoppingListComplete?>(shoppingList)
    */


    fun resetGoBack() {
        state = state.copy(goBack = false)
    }

    fun onProductAdded(product: Product, userKey: String) {

    }


    fun closeErrorDialogRequest() {

        state = state.copy(
            showErrorMessage = false
        )
    }


    data class UiState(
        val loading: Boolean = false,
        val goBack: Boolean = false,
        val records: List<FamilyMember> = emptyList(),
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null
    )


}


