import 'dart:io';

import 'package:drift/drift.dart';
import 'package:drift/native.dart';
import 'package:flutter/foundation.dart';
import 'package:path_provider/path_provider.dart';
import 'package:sqlite3_flutter_libs/sqlite3_flutter_libs.dart';
import 'package:sqlite3/sqlite3.dart';

part 'database.g.dart';

class DataItems extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get username => text()();
  TextColumn get email => text()();
  IntColumn get age => integer().nullable()();
  BoolColumn get isActive => boolean()();
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

    final file = File("/data/data/com.skynamo.sqlitedbfun/databases/data.db");

    // Also work around limitations on old Android versions
    if (Platform.isAndroid) {
      await applyWorkaroundToOpenSqlite3OnOldAndroidVersions();
    }

    // Make sqlite3 pick a more suitable location for temporary files - the
    // one from the system may be inaccessible due to sandboxing.
    final cachebase = (await getTemporaryDirectory()).path;
    // We can't access /tmp on Android, which sqlite3 would try by default.
    // Explicitly tell it about the correct temporary directory.
    sqlite3.tempDirectory = cachebase;

    // return NativeDatabase.createInBackground(file);

    Database database =
    sqlite3.open(file.path, mode: OpenMode.readWriteCreate);
    return NativeDatabase.opened(database,
        enableMigrations: true,
        closeUnderlyingOnClose: true,
        logStatements: true); //Log the sql statements i
  });
}