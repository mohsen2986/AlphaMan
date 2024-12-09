package example.com.service

import example.com.model.User
import example.com.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserService {

    suspend fun getAllUsers() :List<User> = newSuspendedTransaction{
        Users.selectAll().map { toUsers(it) }
    }

    private fun toUsers(row: ResultRow) : User {
        return User(
            id = row[Users.id],
            name = row[Users.name],
        )
        }
}