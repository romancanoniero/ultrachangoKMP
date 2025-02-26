package com.iyr.ultrachango.ui.screens.shoppinglist.members

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class ShoppingMembersSelectionViewModel(
    private val userViewModel: UserViewModel,
    private val shoppingListRepository: ShoppingListRepository,
) : ViewModel(), KoinComponent {

    private var listId: Long? = null
    private val me: User = userViewModel.user.value!!
    var state by mutableStateOf(UiState())
        private set

    fun setListId(listId: Long) {
        this.listId = listId
        fetchData(this.listId!!)
    }


    fun fetchData(listId: Long) {

        state = state.copy(
            loading = true,
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userKey: String = userViewModel.user.value?.uid.toString()
                //        val call = familyMembersRepository.getList(userKey).collect() {

                val call = shoppingListRepository.getMembers(listId, userKey).collect {
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
        fun getList() {
            state = state.copy(
                loading = true,
            )

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val userKey: String = userViewModel.user.value?.id.toString()
                    //        val call = familyMembersRepository.getList(userKey).collect() {

                    val call = shoppingListRepository.getMembers(74, userKey).collect() {

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
    */
    /*
        val buttonSaveEnabled = MutableStateFlow<Boolean>(false)
        val data = MutableStateFlow<ShoppingListComplete?>(shoppingList)
    */


    fun resetGoBack() {
        state = state.copy(goBack = false)
    }

    fun closeErrorDialogRequest() {

        state = state.copy(
            showErrorMessage = false
        )
    }

    fun addMemberToList(listId: Long, userId: String) {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingListRepository.addMember(listId, userId)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        showErrorMessage = true, errorMessage = e.message
                    )

                }
            }

        }
    }

    fun removeMemberFromList(listId: Long, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingListRepository.removeMember(listId, userId)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        showErrorMessage = true, errorMessage = e.message
                    )
                }
            }
        }
    }


    data class UiState(
        val loading: Boolean = false,
        val goBack: Boolean = false,
        val records: List<ShoppingListMember> = emptyList(),
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null
    )


}


