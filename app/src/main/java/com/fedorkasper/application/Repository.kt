package com.fedorkasper.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Calendar

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id:Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun addPost(post: Post)
    fun editById(id: Int, header: String, content: String)
}
interface OnInteractionListener{
    fun onLike(post: Post){}
    fun onEdit(post: Post){}
    fun onRemove(post: Post){}
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
        id = 2,
        header = "Что такое Binding",
        content = "Data Binding — это процесс интеграции представлений в XML-макете с объектами данных.\n" +
                "\n" +
                "Преимущества использования библиотеки Data Binding:\n" +
                "\n" +
                "— Можно сократить количество вызовов findViewById и повысить производительность приложения;\n" +
                "— Помогает избавиться от утечек памяти или исключений нулевого указателя;\n" +
                "— Использует декларативный формат, который является более адаптивным;\n" +
                "— Повышает производительность разработчика за счёт более короткого, простого для понимания и более поддерживаемого кода.\n" +
                "Для настройки проекта на использование Data Binding необходимо:\n" +
                "   1.Объявить библиотеку в файле build.gradle на уровне приложения.\n" +
                "   2.Преобразовать XML-макеты в макеты Data Binding, следуя указанным шагам:\n" +
                "\n" +
                "— Объявить тег <layout>, который будет заключать существующий файл макета на корневом уровне;\n" +
                "— Объявить переменные под тегом <data>, который будет находиться под тегом <layout>;\n" +
                "— Объявить необходимые выражения для привязки данных внутри элементов представления.",
        dateTime = Calendar.getInstance().time,
        isLike = false,
        amountLikes = 1599,
        amountShares = 597
    ),
        Post(
            id = 1,
            header = "Адаптеры в Android",
            content = "Адаптеры в Android упрощают связывание данных с элементом управления. Они используются при работе с виджетами, которые дополняют android.widget.AdapterView: ListView, ExpandableListView, GridView, Spinner, Gallery, а также в активности ListActivity и др.\n" +
                    "\n" +
                    "Примеры готовых адаптеров:\n" +
                    "   ArrayAdapter<T> — предназначен для работы с ListView.\n" +
                    "   ListAdapter — адаптер между ListView и данными.\n" +
                    "   SpinnerAdapter — адаптер для связки данных с элементом Spinner.\n" +
                    "   SimpleAdapter — адаптер, позволяющий заполнить данными список более сложной структуры.\n" +
                    "   Если вам нужен собственный адаптер, в Android есть абстрактный класс BaseAdapter, который можно расширить.",
            dateTime = Calendar.getInstance().time,
            isLike = false,
            amountLikes = 999,
            amountShares = 5478
        ))
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id:Int) {

        posts = posts.map{
            if(it.id != id) it else{
                if (it.isLike)
                    it.amountLikes--
                else
                    it.amountLikes++
                it.copy(isLike = !it.isLike)
            }
        }
        data.value = posts
    }
    override fun shareById(id:Int) {
        posts = posts.map {
            if(it.id != id)
                it
            else
                it.copy(amountShares = it.amountShares + 10)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts

    }

    override fun addPost(post: Post) {
        posts = listOf(
            post.copy(
                id = 0,
                dateTime = Calendar.getInstance().time,
                amountLikes = 0,
                amountShares = 0,
                isLike = false
            )
        )+ posts
        data.value = posts
    }

    override fun editById(id: Int, header: String, content: String) {
        posts = posts.map {
            if(it.id != id)
                it
            else {
                if (it.id == 0 ) it.id = nextId(posts)
                it.copy(header = header, content = content)
            }
        }
        data.value = posts
    }
}

private val empty = Post(
    0,
    "",
    "",
    Calendar.getInstance().time,
    0,
    0,
    false
)
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)
    fun addPost(){
        edited.value?.let {
            repository.addPost(it)
        }
            edited.value = empty
    }
    fun editById(id: Int,header:String,content:String){
        repository.editById(id,header,content)
    }
    fun changeContent(header:String,content:String){
        edited.value?.let {
            if (it.header == header.trim()) return
            edited.value = it.copy(content = content.trim(),header = header.trim())
        }
    }
    fun likeById(id:Int) = repository.likeById(id)
    fun shareById(id:Int) = repository.shareById(id)
    fun removeById(id:Int) = repository.removeById(id)
}
fun nextId(posts:List<Post>):Int{
    var id = 1
    posts.forEach{it1->
        posts.forEach{
            if (it.id==id) id=it.id+1
        }
    }

    return id
}