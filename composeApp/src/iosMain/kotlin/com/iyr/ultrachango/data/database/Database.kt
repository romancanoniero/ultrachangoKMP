package com.iyr.ultrachango.data.database


private const val DATABASE_NAME = "ultrachango.db"

/*
fun getDatabaseBuilder(): RoomDatabase.Builder<UltraChangoDatabase> {
    val dbFilePath = documentDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<UltraChangoDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

 */