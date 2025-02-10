package com.iyr.ultrachango.ui.screens.shoppinglist.members

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.elements.CustomSwitch
import com.iyr.ultrachango.utils.ui.elements.ItemListTextHeader
import com.iyr.ultrachango.utils.ui.elements.ItemListTextSubHeader
import com.iyr.ultrachango.utils.ui.elements.UserPictureRegular
import com.iyr.ultrachango.utils.ui.elements.MySearchTexField
import com.iyr.ultrachango.utils.ui.elements.buttonShapeBig
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShoppingMembersSelectionScreen(
    listId: Long ,
    appNavController: NavHostController,
    scaffoldVM: ScaffoldViewModel,
    vm: ShoppingMembersSelectionViewModel = koinViewModel(),
    // userViewModel: UserViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        vm.setListId(listId)
    }


    val onInvitationClick: ((String) -> Unit) = { extras ->
        if (extras.isValidMobileNumber()) {
            println("Invitar a $extras")

        } else if (extras.isEmail()) {
            println("Invitar a $extras")
        }
    }

    val onStatusChange: ((String, Boolean) -> Unit) = { userId, status ->
        println("Cambio de estado $status")

        when (status) {
            true -> vm.addMemberToList(listId, userId)
            false -> vm.removeMemberFromList(listId, userId)
        }

    }

        scaffoldVM.setTitle("Miembros!!!")

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)
        ) {

            val state = vm.state


            var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(screenOuterPadding)

            ) {

                MySearchTexField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeHolderText = "Nick, correo o telefono"
                )



                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f)

                ) {
                    state.records.forEach { member ->
                        item {
                            MemberItem(member, onStatusChange)
                        }

                    }
                }


                Button(modifier = Modifier.fillMaxWidth().background(Color.Black, buttonShapeBig),

                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Black, contentColor = Color.White
                    ),

                    onClick = {
                        // Handle button click
                    }, content = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            text = "Invite Members"
                        )
                    })
            }
        }
    }


@Composable
fun MemberItem(
    member: ShoppingListMember,
    onStatusChange: ((String, Boolean) -> Unit)? = null,
    onClick: () -> Unit? = {},
) {

    var included by remember { mutableStateOf(member.connectionStatus.equals("MEMBER")) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable {
            /*
            member.user.extras?.let {
                onInvitationClick?.invoke(it)
            }

             */
        },
        verticalAlignment = Alignment.CenterVertically,


        ) {


        /*
             when (member.user.recordType) {
                 RecordType.APP_USER -> {
                     ItemListImageBoxRounded(
                         modifier = Modifier.size(60.dp),
                         imageModel = getProfileImageURL(member.user.id),
                         contentDesription = member.user.nick ?: "",
                         onClick = onClick,

                     )
                 }

                 RecordType.INVITE_SUGGESTION -> {
                     if ((member.user.extras ?: "").isValidMobileNumber()) {
                         ItemListImageBoxRounded(
                             modifier = Modifier.size(60.dp),
                             imageVector = Icons.Outlined.Phone,
                             innerPadding = PaddingValues(8.dp),
                             contentDesription = member.user.extras ?: ""
                         )
                     } else
                         if ((member.user.extras ?: "").isEmail()) {
                             ItemListImageBoxRounded(
                                 modifier = Modifier.size(60.dp),
                                 innerPadding = PaddingValues(8.dp),
                                 imageVector = Icons.Outlined.Email,
                                 contentDesription = member.user.extras ?: ""
                             )
                         }
                 }
             }
        */

        getProfileImageURL(member.userId,"").let {

            UserPictureRegular(
                modifier = Modifier.size(60.dp),
                imageModel = it!!,
                contentDesription = "",
                onClick = {
                    triggerHapticFeedback()
                    onClick()
                },
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {

            ItemListTextHeader(text = member.user?.nick.toString())
            //   Text(text = member.connectionStatus.toString(), style = Style)
            if (member.isAdmin) {
                //Text("Admin")
                ItemListTextSubHeader(text = "Admin")
            }
        }

        if (member.isAdmin) {
            Text("Admin")
        } else {
            CustomSwitch(
                checked = included, enabled = true, onCheckedChange = { status ->
                    included = status
                    onStatusChange?.invoke(member.userId,status)

                }, modifier = Modifier.padding(8.dp)
            )
        }
    }
}

