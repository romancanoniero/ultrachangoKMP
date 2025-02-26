package com.iyr.ultrachango.ui.screens.shoppinglist.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository

import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.database.repositories.StoresRepository
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.ui.capitalizeFirstLetter
import com.iyr.ultrachango.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ShoppingListViewModel(
    private val authRepository: AuthRepository,
    private val shoppingListRepository: ShoppingListRepository,

    private val tiendasRepository: StoresRepository,
    private val userViewModel: UserViewModel,
    private val scaffoldVM: ScaffoldViewModel,


    ) : ViewModel(), KoinComponent {



    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        //  scaffoldVM.showLoader(true)
        viewModelScope.launch(Dispatchers.IO) {
            //    val userId = userViewModel.user.value?.id.toString()
            fetchLists()
        }

    }

    suspend fun fetchLists() {
        scaffoldVM.showLoader(true)

        try {
            shoppingListRepository.fetchLists().collect {

                _state.value = _state.value.copy(list = it)
                scaffoldVM.showLoader(false)
            }
        } catch (e: Exception) {
            scaffoldVM.showLoader(false)

            _state.value = _state.value.copy(
                loading = false,
                errorMessage = e.message,
                showErrorMessage = true
            )
        }
    }


    fun onDeleteButtonClicked(shoppingList: ShoppingList) {
        //   state = state.copy(showDeletionConfirmationDialog = Pair(true, shoppingList))

        val paramMap = HashMap<String, Any>()
        paramMap.put("entity", shoppingList)
        _state.value = _state.value.copy(
            showDeletionConfirmationDialog = paramMap
        )

    }

    fun onResetDialogsRequested() {
        _state.value = _state.value.copy(
            showDeletionConfirmationDialog = null,


            )
    }

    fun onCreationRequested(newName: String) {

        //      userViewModel.user.value?.id?.let { userId ->


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userKey = authRepository.getUserKey()!!
                val newShoppingList =
                    ShoppingList(listName = newName.capitalizeFirstLetter(), userId = userKey)
                var entity = shoppingListRepository.saveShoppingList(newShoppingList)
                fetchLists()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    errorMessage = e.message,
                    showErrorMessage = true
                )
            }
        }
//        }
    }


    fun onRenameRequested(shoppingList: ShoppingList, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userKey = userViewModel.user.value?.uid.toString()
            shoppingListRepository.renameList(shoppingList.id, newName)
        }
    }


    fun onDeletionRequested(entity: ShoppingList) {
        val paramMap = HashMap<String, Any>()
        paramMap.put("entity", entity)
        _state.value = _state.value.copy(
            showDeletionConfirmationDialog = paramMap
        )
    }

    fun deleteShoppingList(entity: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingListRepository.removeList(entity)
                onResetDialogsRequested()
                fetchLists()
            } catch (exception: Exception) {
                _state.value = _state.value.copy(
                    showErrorMessage = true,
                    errorMessage = exception.message
                )
            }
        }

    }


    fun closeErrorDialogRequest() {

        _state.value = _state.value.copy(
            showErrorMessage = false
        )
    }

    fun onRefreshRequested() {
        val userId = userViewModel.user.value?.uid.toString()
        try {
            viewModelScope.launch(Dispatchers.IO) {
                fetchLists()
            }
        } catch (exception: Exception) {
            _state.value = _state.value.copy(
                showErrorMessage = true,
                errorMessage = exception.message
            )
        }
    }


    data class UiState(
        val loading: Boolean = false,
        val buttonSaveEnabled: Boolean = false,
        val goBack: Boolean = false,
        val list: List<ShoppingList> = emptyList(),
        val showDeletionConfirmationDialog: HashMap<String, Any>? = null,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false
    )
}


