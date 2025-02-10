package com.iyr.ultrachango.ui.screens.member

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
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
import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.RecordType
import com.iyr.ultrachango.data.models.UserAsFamilyMember
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.elements.UserPictureRegular
import com.iyr.ultrachango.utils.ui.elements.MySearchTexField
import com.iyr.ultrachango.utils.ui.elements.buttonShapeBig
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MembersScreen(
    listId: Int? = null,
    appNavController: NavHostController,
    scaffoldVM: ScaffoldViewModel,
    vm: MembersScreenViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {

    val onInvitationClick: ((String) -> Unit) = { extras ->
        if (extras.isValidMobileNumber()) {
            println("Invitar a $extras")

        } else if (extras.isEmail()) {
            println("Invitar a $extras")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    )
    {

        val state = vm.state

        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier.fillMaxSize()
                .weight(1f)
                .padding(screenOuterPadding)

        ) {

            MySearchTexField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { searchQuery = it},
                placeHolderText = "Nick, correo o telefono"
            )



            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .weight(1f)
            ) {

                val filteredMembers = filterMembers(state.records, searchQuery.text)

                filteredMembers.forEach { member ->
                    item {
                        MemberItem(member,
                            onInvitationClick )
                    }

                }
            }




            Button(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Black, buttonShapeBig),

                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),

                onClick = {
                    // Handle button click
                },
                content = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        text = "Invite Members"
                    )
                }
            )
        }


    }
}

fun filterMembers(records: List<FamilyMember>, text: String): List<FamilyMember> {
    val results: ArrayList<FamilyMember> = ArrayList<FamilyMember>(
        records.filter {
            it.user.nick.contains(text, ignoreCase = true)
        }
    )
    if (text.isEmail() || text.isValidMobileNumber()) {
        val inviteMember = records.find { it.user.recordType == RecordType.INVITE_SUGGESTION }
        if (inviteMember == null) {
            val newMember = FamilyMember(
                user = UserAsFamilyMember(
                    id = "invite_${text.hashCode()}",
                    nick = "Invitar a $text",
                    recordType = RecordType.INVITE_SUGGESTION,
                    extras = text
                ),
                connectionStatus = "",
                userId = "",
                memberId = "",
            )
            results.add(newMember)
        } else {
            inviteMember.user.nick = "Invitar a $text"
        }
    }
    return results
}

@Composable
fun MemberItem(
    member: FamilyMember,
    onInvitationClick: ((String) -> Unit)? = null,
    onClick: () -> Unit? = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                member.user.extras?.let {
                    onInvitationClick?.invoke(it)
                }
            },
        verticalAlignment = Alignment.CenterVertically,


        ) {
        when (member.user.recordType) {
            RecordType.APP_USER -> {

                getProfileImageURL(member.user.id, member.user.fileName ).let {
                    UserPictureRegular(
                        modifier = Modifier.size(60.dp),
                        imageModel = it!!,
                        contentDesription = member.user.nick,
                        onClick = onClick,

                        )
                }

            }

            RecordType.INVITE_SUGGESTION -> {
                if ((member.user.extras ?: "").isValidMobileNumber()) {
                    UserPictureRegular(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Outlined.Phone,
                        innerPadding = PaddingValues(8.dp),
                        contentDesription = member.user.extras ?: ""
                    )
                } else
                    if ((member.user.extras ?: "").isEmail()) {
                        UserPictureRegular(
                            modifier = Modifier.size(60.dp),
                            innerPadding = PaddingValues(8.dp),
                            imageVector = Icons.Outlined.Email,
                            contentDesription = member.user.extras ?: ""
                        )
                    }
            }
        }
        /*
               ItemListImageBoxRounded(
                   modifier = Modifier.size(60.dp),
                   imageModel = getProfileImageURL(member.user.id),
                   contentDesription = member.user.nick ?: ""
               )
       */
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = member.user.nick, style = MaterialTheme.typography.body1)
            Text(text = member.connectionStatus, style = MaterialTheme.typography.body2)
        }
    }
}

