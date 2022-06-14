class NoteService: CrudService<Note> {

    val elementsList = mutableListOf<Note>()
    private val comment = CommentService()
    private var nextID = 1

    override fun add(element: Note): Note {
        val newNote = element.copy(id = nextID++)
        elementsList.add(newNote)
        return elementsList.last()
    }

    override fun delete(id: Int): Boolean {
        var change = false
        for ((index, value) in elementsList.withIndex()) {
            if (id == value.id && !value.isDeleted) {
                elementsList[index] = value.copy(isDeleted = true)
                if (comment.elementsList.isNotEmpty()) {
                    val commentIsDelete = comment.elementsList.get(index)
                    comment.elementsList[index] = commentIsDelete.copy(isDeleted = true)
                }
                change = true
                break
            }
        }
        return change
    }

    override fun edit(element: Note): Boolean {
        var change = false
        for ((index, value) in elementsList.withIndex()) {
            if (element.id == value.id && !value.isDeleted) {
                elementsList[index] = value.copy(
                    title = element.title,
                    text = element.text
                )
                change = true
                break
            }
        }
        return change
    }

    override fun getByID(id: Int): Note {
        val listByID = mutableListOf<Note>()
        for (note in elementsList) {
            if (id == note.id && !note.isDeleted)
                listByID.add(note)
        }
        return if (listByID.isNotEmpty()) listByID.last() else throw CommentExeption("По данному ID записей не найдено")
    }

    override fun restore(id: Int): Boolean {
        throw CommentExeption("Восстановление записей невозможно")
    }

}