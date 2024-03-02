package com.fedorkasper.application

import java.util.Calendar

fun getPosts():List<Post>{
    return listOf(
        Post(
            id = 2,
            R.drawable.img_avatar_smr,
            author = "АСМР",
            content = "АСМР (автономная сенсорная меридиональная реакция) — это, по данным Кембриджского словаря, " +
                    "приятное покалывание, которое люди чувствуют в ответ на звуковые и визуальные стимулы (шепот, " +
                    "движения кисти, жевание, скрежет, постукивания и т.д.). Это ощущение еще может сопровождаться чувством " +
                    "эйфории и расслаблением. В последние годы в сети Интернет поклонников этого веяния становится все больше.",
            dateTime = Calendar.getInstance().time,
            amountLikes = randomNumb(),
            amountShares = randomNumb(),
            amountViews = randomNumb(),
            url = "https://www.youtube.com/watch?v=YUohm2CrVfY",
            isLike = false,
        ),
        Post(
            id = 1,
            R.mipmap.ic_launcher,
            author = "Адаптеры в Android",
            content = "Адаптеры в Android упрощают связывание данных с элементом управления. Они используются при работе с виджетами, которые дополняют android.widget.AdapterView: ListView, ExpandableListView, GridView, Spinner, Gallery, а также в активности ListActivity и др.\n" +
                    "\n" +
                    "Примеры готовых адаптеров:\n" +
                    "   ArrayAdapter<T> — предназначен для работы с ListView.\n" +
                    "   ListAdapter — адаптер между ListView и данными.\n" +
                    "   SpinnerAdapter — адаптер для связки данных с элементом Spinner.\n" +
                    "   SimpleAdapter — адаптер, позволяющий заполнить данными список более сложной структуры.\n" +
                    "   Если вам нужен собственный адаптер, в Android есть абстрактный класс BaseAdapter, который можно расширить.",
            dateTime = Calendar.getInstance().time,
            amountLikes = randomNumb(),
            amountShares = randomNumb(),
            amountViews = randomNumb(),
            url = "",
            isLike = false,

            ))

}
fun getEmptyPost():Post{
    return Post(
        0,
        R.drawable.img_avatar,
        "",
        "",
        dateTime = Calendar.getInstance().time,
        amountViews = 0,
        amountLikes = 0,
        amountShares = 0,
        url = "",
        isLike = false,
    )
}
