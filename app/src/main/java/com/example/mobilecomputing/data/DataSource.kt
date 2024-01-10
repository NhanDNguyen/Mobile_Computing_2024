package com.example.mobilecomputing.data

import com.example.mobilecomputing.R

data class Message(
    val author: String,
    val body: String,
    val imageId: Int = -1,
    val contentDescription: String = ""
)

val messages = listOf<Message>(
    Message(
        author = "Dwalin",
        body = "A partially bald and savage who likes to brawl and eat, very good at mining",
        imageId = R.drawable.dwalin,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/dwarf\" title=\"dwarf icons\">Dwarf icons created by Freepik - Flaticon</a>",
    ),

    Message(
        author = "Balin",
        body = "A kind, gentle, wise, and old dwarf who loves peace and is tired of war, he just wants to have his home back.",
        imageId = R.drawable.balin,
        contentDescription = "<a target=\"_blank\" href=\"https://icons8.com/icon/PXWprcrbvtYE/dwarf\">Dwarf</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a>",
    ),

    Message(
        author = "Kili",
        body = "A skillful and intelligent dwarf who is really good at fighting and following orders, brother of Fili",
        imageId = R.drawable.kili,
        contentDescription = "<a target=\"_blank\" href=\"https://icons8.com/icon/UeMo0neF8nUQ/dwarf\">Dwarf</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a>",
    ),

    Message(
        author = "Fili",
        body = "Just like Kili, Fili is also a very skillful and intelligent dwarf, brother of Kili",
        imageId = R.drawable.fili,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/dwarf\" title=\"dwarf icons\">Dwarf icons created by Flat Icons - Flaticon</a>",
    ),

    Message(
        author = "Thorin Oakenshield",
        body = "King of the drawrves, a good king who loves his people, he is constantly fighting and looking for a home for all the dwarves.",
        imageId = R.drawable.thorin,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/king\" title=\"king icons\">King icons created by Freepik - Flaticon</a>",
    ),

    Message(
        author = "Dori",
        body = "A loyal dwarf soldier, who vowed to protect his kingdom and his king, really good at mining",
        imageId = R.drawable.dori,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/viking\" title=\"viking icons\">Viking icons created by Freepik - Flaticon</a>",
    ),

    Message(
        author = "Nori",
        body = "A very funny dwarf, who likes humor and always seeks to entertain others",
        imageId = R.drawable.nori,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/warrior\" title=\"warrior icons\">Warrior icons created by Freepik - Flaticon</a>",
    ),

    Message(
        author = "Ori",
        body = "A very kind and gentle dwarf, perhaps the most kind and gentle dwarf among the 13",
        imageId = R.drawable.ori,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/warrior\" title=\"warrior icons\">Warrior icons created by Roundicons - Flaticon</a>",
    ),

    Message(
        author = "Oin",
        body = "He has big appetite, and always feels hungry",
        imageId = R.drawable.oin,
        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/fat\" title=\"fat icons\">Fat icons created by Freepik - Flaticon</a>",
    ),

)
