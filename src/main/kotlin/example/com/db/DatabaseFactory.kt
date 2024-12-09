package example.com.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import example.com.model.Users
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert

object DatabaseFactory {

    fun init(){
        Database.connect(hikari())
        transaction {
            create(Users)
            Users.insert {
                it[name] = "Ishrat Khan"
            }
            Users.insert {
                it[name] = "Suhaib Roomy"
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()


//        config.jdbcUrl = "jdbc:mysql://localhost:3306/alpha_man"
//        config.username = "root"
//        config.password = ""

        config.jdbcUrl = "jdbc:mysql://avnadmin:AVNS_iYuyvX01L8U5k-mykrK@mysql-2fd66379-mohsenaliakbari-84ab.d.aivencloud.com:22428/defaultdb?ssl-mode=REQUIRED"
//        config.username = "root"
//        config.password = ""
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); //alternative is Class.forName("com.mysql.cj.jdbc.Driver")

        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}