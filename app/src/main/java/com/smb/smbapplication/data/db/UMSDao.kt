

package com.smb.smbapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smb.smbapplication.data.model.User

/**
 * Interface for database access on Repo related operations.
 */

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

@Dao
abstract class UMSDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(vararg users:User)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
      abstract fun insertUsers( users: List<User>)

      @Query(" SELECT * FROM User")
      abstract fun loadUsers(): LiveData<List<User>>

    /*fun loadOrdered(repoIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(repoIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }*/

}
