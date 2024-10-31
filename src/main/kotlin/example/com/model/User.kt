package example.com.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val name:String,
    val id:Int=0
)

object Users: Table(){
    val id=integer("id").autoIncrement()
    val name=varchar("name",255)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}