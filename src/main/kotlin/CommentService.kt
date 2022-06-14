class CommentService : CrudService<Comment> {

    val elementsList = mutableListOf<Comment>()
    private val note = NoteService()

    override fun add(element: Comment): Comment {
        for ((index, value) in note.elementsList.withIndex()) {
            when {
                value.id == element.id && !value.isDeleted && !element.isDeleted -> {
                    elementsList.add(element)
                    break
                }
                index < note.elementsList.size - 1 -> continue
                else -> break
            }
        }
        return if (elementsList.isNotEmpty()) elementsList.last() else throw CommentExeption("Невозможно добавить комментарий к заметке")
    }

    override fun delete(id: Int): Boolean {
        var change = false
        for ((index, value) in elementsList.withIndex()) {
            when {
                id != value.id && (index < elementsList.size - 1) -> continue
                id == value.id && !value.isDeleted -> {
                    elementsList[index] = value.copy(isDeleted = true)
                    change = true
                    break
                }
                else -> throw CommentExeption("Этот комментарий уже удален, удаление невозможно")
            }
        }
        return change
    }

    override fun edit(element: Comment): Boolean {
        var change = false
        for ((index, value) in elementsList.withIndex()) {
            when {
                element.id != value.id && (index < elementsList.size - 1) -> continue
                element.id == value.id && !value.isDeleted -> {
                    elementsList[index] = element.copy(message = element.message)
                    change = true
                    break
                }
                else -> throw CommentExeption("Этот комментарий уже удален,редактирование невозможно")
            }
        }
        return change
    }

    override fun getByID(id: Int): Comment {
        val listByID = mutableListOf<Comment>()
        for (comment in elementsList) {
            if (id == comment.id && !comment.isDeleted)
                listByID.add(comment)
        }
        return if (listByID.isNotEmpty()) listByID.last() else throw CommentExeption("По данному ID комментариев не найдено")
    }

    override fun restore(id: Int): Boolean {
        var change = false
        for ((index, value) in elementsList.withIndex()) {
            when {
                id != value.id && (index < elementsList.size - 1) -> continue
                id == value.id && value.isDeleted -> {
                    elementsList[index] = value.copy(isDeleted = false)
                    change = true
                    break
                }
                else -> throw CommentExeption("Невозможно восстановить неудаленный комментарий")
            }
        }
        return change
    }
}