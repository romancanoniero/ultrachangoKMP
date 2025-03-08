package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.stringResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.gender_female
import ultrachango2.composeapp.generated.resources.gender_male
import ultrachango2.composeapp.generated.resources.gender_other

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    value: Genders,
    onGenderSelected: (Genders) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf<Int>(value.ordinal) }

    val maleGender = stringResource(Res.string.gender_male)
    val femaleGender = stringResource(Res.string.gender_female)
    val otherGender = stringResource(Res.string.gender_other)


    var gendersByText = rememberSaveable {
        mutableListOf(
            maleGender, femaleGender, otherGender
        )
    }


    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = stringResource(resource = Genders.values()[gender].descriptionResId),
            onValueChange = { gender = 1 },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            readOnly = true
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf(
                stringResource(Res.string.gender_male),
                stringResource(Res.string.gender_female),
                stringResource(Res.string.gender_other)
            ).forEach { selectionOption ->
                DropdownMenuItem(text = { Text(selectionOption) }, onClick = {
                    triggerHapticFeedback()
                    when (selectionOption) {
                        (gendersByText.get(0)) -> {
                            gender = Genders.MALE.ordinal
                        }

                        (gendersByText.get(1)) -> {
                            gender = Genders.FEMALE.ordinal
                        }

                        (gendersByText.get(2)) -> {
                            gender = Genders.OTHER.ordinal
                        }
                    }
                    onGenderSelected(Genders.values().get(gender))
                    expanded = false
                })
            }
        }
    }

}
