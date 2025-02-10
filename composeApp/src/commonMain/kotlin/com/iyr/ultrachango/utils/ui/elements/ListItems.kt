package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.ui.theme.cardViewColors
import com.iyr.ultrachango.ui.theme.textIntense
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sin_imagen



@Composable
fun ItemListContainer(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(Color.White)
        ,
        contentAlignment
    ) {
       Box(modifier = Modifier
           .padding(vertical = 14.dp, horizontal = 10.dp)
           )
       {
           content()
       }
    }
}

@Composable
fun ItemListTextHeader(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = modifier.padding(bottom = 5.dp), text = text, style = TextStyle(
            fontFamily = SFProMediumFontFamily(),
            fontSize = textSize16,
            fontWeight = FontWeight.ExtraBold,
            color = textIntense
        )
    )
}


@Composable
fun ItemListTextSubHeader(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = modifier, text = text, style = TextStyle(
            fontFamily = SFProMediumFontFamily(),
            fontSize = textSize14,
            fontWeight = FontWeight.Medium,
            color = textIntense
        )
    )
}

@Composable
fun ItemListTextRegular(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = modifier, text = text, style = TextStyle(
            fontFamily = SFProMediumFontFamily(),
            fontSize = textSize12,
            fontWeight = FontWeight.Normal,
            color = textIntense
        )
    )
}


@Composable
fun ItemListImageBox(
    modifier: Modifier = Modifier,
    imageModel: String,
    contentDesription: String
) {

    Card(
        modifier =
        Modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .width(80.dp)
            .height(80.dp),
//        colors = cardViewColors
    )
    {
        AsyncImage(
            model = imageModel,
            placeholder = painterResource(Res.drawable.sin_imagen),
            contentDescription = contentDesription,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    //    onImageClicked(product)

                }
        )
    }
}

enum class UserPictureInfoMode
{
    PLAIN,
    BADGE_QTY,
    BADGE_HAND
}

@Composable
fun UserPictureRegular(
    modifier: Modifier = Modifier
        .width(80.dp)
        .height(80.dp),
    imageModel: String,
    contentDesription: String,
    mode : UserPictureInfoMode = UserPictureInfoMode.PLAIN,
    onClick: () -> Unit? = {},

    ) {

    Box(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .width(80.dp)
            .height(80.dp)
            .clip(CircleShape)
            .shadow(4.dp, CircleShape)

    ) {
        AsyncImage(
            model = imageModel,
            placeholder = painterResource(Res.drawable.sin_imagen),
            contentDescription = contentDesription,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape).background(Color.White)
                .clickable {
                    onClick()
                }
        )
    }
}


@Composable
fun UserPictureRegular(
    modifier: Modifier = Modifier
        .width(80.dp)
        .height(80.dp),
    imageVector: ImageVector,
    contentDesription: String,
    innerPadding : PaddingValues = PaddingValues(0.dp),

    ) {

    Card(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .width(80.dp)
            .height(80.dp)
            .clip(CircleShape)
            .shadow(4.dp, CircleShape)
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = contentDesription,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    //    onImageClicked(product)

                }
                .padding(innerPadding)
        )

    }
}

