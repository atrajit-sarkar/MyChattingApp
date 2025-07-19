package com.example.mychattingapp.ChatsData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

data class ChannelsData(
    var channelname: String,
    var timeStamps: String = "12:00",
    var lastMessage: String = "This is $channelname",
    var messageCounter: Int = 0,
    var channelicon: @Composable () -> Unit
)

fun loadChannels(): List<ChannelsData> {
    return listOf(
        ChannelsData(
            channelname = "Tech Updates",
            timeStamps = "10:30",
            lastMessage = "Latest trends in tech!",
            messageCounter = 5,
            channelicon = { FaIcon(
                faIcon = FaIcons.Desktop,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Daily News",
            timeStamps = "08:00",
            lastMessage = "Breaking: New updates on...",
            messageCounter = 12,
            channelicon = { FaIcon(
                faIcon = FaIcons.Newspaper,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Music Lovers",
            timeStamps = "09:15",
            lastMessage = "Top hits this week are...",
            messageCounter = 7,
            channelicon = { FaIcon(
                faIcon = FaIcons.Music,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Sports Updates",
            timeStamps = "07:50",
            lastMessage = "Final score is...",
            messageCounter = 3,
            channelicon = { FaIcon(
                faIcon = FaIcons.FootballBall,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Coding Hub",
            timeStamps = "11:20",
            lastMessage = "Learn Kotlin today!",
            messageCounter = 8,
            channelicon = { FaIcon(
                faIcon = FaIcons.Code,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Gaming Arena",
            timeStamps = "15:45",
            lastMessage = "New DLC released for...",
            messageCounter = 15,
            channelicon = { FaIcon(
                faIcon = FaIcons.Gamepad,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Travel Diaries",
            timeStamps = "12:00",
            lastMessage = "Explore Bali with us!",
            messageCounter = 4,
            channelicon = { FaIcon(
                faIcon = FaIcons.Plane,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Foodies United",
            timeStamps = "13:30",
            lastMessage = "Today's recipe: Pasta",
            messageCounter = 2,
            channelicon = { FaIcon(
                faIcon = FaIcons.FontAwesomeFlag,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Movie Buffs",
            timeStamps = "20:10",
            lastMessage = "Top movies to watch...",
            messageCounter = 9,
            channelicon = { FaIcon(
                faIcon = FaIcons.Video,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Fitness Freaks",
            timeStamps = "06:00",
            lastMessage = "Morning workout tips",
            messageCounter = 6,
            channelicon = { FaIcon(
                faIcon = FaIcons.Dumbbell,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Book Club",
            timeStamps = "18:45",
            lastMessage = "Read this week's book!",
            messageCounter = 3,
            channelicon = { FaIcon(
                faIcon = FaIcons.Book,
                tint = MaterialTheme.colorScheme.onBackground
            )}
        ),
        ChannelsData(
            channelname = "Science Talks",
            timeStamps = "14:25",
            lastMessage = "Amazing facts about...",
            messageCounter = 5,
            channelicon = { FaIcon(
                faIcon = FaIcons.Lock,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Art Enthusiasts",
            timeStamps = "16:30",
            lastMessage = "New exhibition in town!",
            messageCounter = 1,
            channelicon = { Icon(Icons.Default.Info, contentDescription = null) }
        ),
        ChannelsData(
            channelname = "Fashion Trends",
            timeStamps = "21:00",
            lastMessage = "Top looks of the season",
            messageCounter = 11,
            channelicon = { FaIcon(
                faIcon = FaIcons.ShippingFast,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Startup Ideas",
            timeStamps = "09:50",
            lastMessage = "Pitch your ideas here",
            messageCounter = 0,
            channelicon = { FaIcon(
                faIcon = FaIcons.Lightbulb,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "History Buffs",
            timeStamps = "10:10",
            lastMessage = "Explore ancient stories",
            messageCounter = 8,
            channelicon = { FaIcon(
                faIcon = FaIcons.BalanceScale,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Pets Paradise",
            timeStamps = "08:40",
            lastMessage = "Adorable pets photos!",
            messageCounter = 3,
            channelicon = { FaIcon(
                faIcon = FaIcons.Dog,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Health Talks",
            timeStamps = "05:30",
            lastMessage = "Tips for a healthy life",
            messageCounter = 7,
            channelicon = { FaIcon(
                faIcon = FaIcons.DoorClosed,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Tech Jobs",
            timeStamps = "19:15",
            lastMessage = "Hiring for developers!",
            messageCounter = 4,
            channelicon = { FaIcon(
                faIcon = FaIcons.NetworkWired,
                tint = MaterialTheme.colorScheme.onBackground
            ) }
        ),
        ChannelsData(
            channelname = "Photography Club",
            timeStamps = "22:10",
            lastMessage = "Top clicks of the day",
            messageCounter = 10,
            channelicon = {
                FaIcon(
                    faIcon = FaIcons.Camera,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )
    )
}
