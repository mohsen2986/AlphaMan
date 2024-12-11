package example.com.repository;

import example.com.model.User
import example.com.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository {

  private val users = mutableListOf<User>()

//  fun findById(id: UUID): User? =
//    users.firstOrNull { it.id == id }

  suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
    Users.select { Users.name eq username }
      .singleOrNull()
      ?.let {
        toUsers(it)
      }
  }

  fun save(user: User): Boolean =
    users.add(user)

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