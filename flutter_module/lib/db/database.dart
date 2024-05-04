import 'dart:io';

import 'package:drift/drift.dart';
import 'package:drift/native.dart';
import 'package:flutter/foundation.dart';
import 'package:sqlite3_flutter_libs/sqlite3_flutter_libs.dart';
import 'package:sqlite3/sqlite3.dart';

part 'database.g.dart';

class DataItems extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get username => text()();
  TextColumn get email => text()();
  IntColumn get age => integer().nullable()();
  BoolColumn get isActive => boolean().nullable()();

  @override
  String get tableName => 'dataitem';

}

@DriftDatabase(tables: [DataItems])
class AppDatabase extends _$AppDatabase {
  AppDatabase() : super(_openConnection());

  @override
  MigrationStrategy get migration => MigrationStrategy(
    onCreate: (m) async {
      if (kDebugMode) {
        print("Dont create anything");
      }
    },
    onUpgrade: (m, from, to) async {
      if (kDebugMode) {
        print("Dont upgrade anything");
      }
    },
  );
  @override

  @override
  int get schemaVersion => 1;
}

LazyDatabase _openConnection() {
  // the LazyDatabase util lets us find the right location for the file async.
  return LazyDatabase(() async {

    // Warning hardcoded paths
    final dbFile = File("/data/data/com.skynamo.sqlitedbfun/databases/data.db");
    final tempFolder = File("/storage/emulated/0/Android/data/com.skynamo.sqlitedbfun/files");

    // Also work around limitations on old Android versions
    if (Platform.isAndroid) {
      await applyWorkaroundToOpenSqlite3OnOldAndroidVersions();
    }

    // Make sqlite3 pick a more suitable location for temporary files - the
    // one from the system may be inaccessible due to sandboxing.
    final cachePath = tempFolder.path;
    // We can't access /tmp on Android, which sqlite3 would try by default.
    // Explicitly tell it about the correct temporary directory.
    sqlite3.tempDirectory = cachePath;

    // Both open modes:
    // OpenMode.readWriteCreate and OpenMode.readOnly cause SQLite db issues
    Database database =
    sqlite3.open(dbFile.path, mode: OpenMode.readWriteCreate);
    return NativeDatabase.opened(database,
        enableMigrations: false,
        closeUnderlyingOnClose: true,
        logStatements: true); //Log the sql statements i
  });
}