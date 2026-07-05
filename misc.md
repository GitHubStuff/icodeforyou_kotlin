Proto DataStore Setup for Person Preferences
PDF‑Ready Document
1. person.proto
Code

syntax = "proto3";

option java_package = "com.example.datastore";
option java_multiple_files = true;

message Person {
  string name = 1;
  int32 age = 2;
  int32 gender = 3;
}

2. Gradle Dependencies
Code

implementation "androidx.datastore:datastore:1.1.1"
implementation "com.google.protobuf:protobuf-javalite:3.25.1"

Proto Generation Block
Code

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {}
            }
        }
    }
}

3. PersonSerializer.kt
kotlin

import androidx.datastore.core.Serializer
import com.example.datastore.Person
import java.io.InputStream
import java.io.OutputStream

object PersonSerializer : Serializer<Person> {

    override val defaultValue: Person = Person.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Person =
        Person.parseFrom(input)

    override suspend fun writeTo(t: Person, output: OutputStream) =
        t.writeTo(output)
}

4. DataStore Instance
kotlin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.datastore.Person

val Context.personDataStore: DataStore<Person> by dataStore(
    fileName = "person.pb",
    serializer = PersonSerializer
)

5. PersonRepository.kt
kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.datastore.Person

class PersonRepository(private val context: Context) {

    val personFlow: Flow<Person> =
        context.personDataStore.data.map { it }

    suspend fun savePerson(name: String, age: Int, gender: Int) {
        context.personDataStore.updateData { current ->
            current.toBuilder()
                .setName(name)
                .setAge(age)
                .setGender(gender)
                .build()
        }
    }

    suspend fun clearPerson() {
        context.personDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}

6. Usage Example
kotlin

val repo = PersonRepository(context)

viewModelScope.launch {
    repo.savePerson("Alice", 30, 1)
}

repo.personFlow.collect { person ->
    println(person)
}

7. Notes for Production

    Proto DataStore is strongly typed and version‑safe

    Use toBuilder() for partial updates

    Use clear() to reset the file

    Supports migrations from SharedPreferences<>