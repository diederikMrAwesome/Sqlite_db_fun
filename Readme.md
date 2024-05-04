### Standalone app for reproducing SQLite db corruption

## Overview of structure

This is an Android app that has flutter embedded in one of the fragments.  The Android app 
generates some data in the background once the user has pressed the "Generate items" button.  
It also updates some rows in a transaction using [ORMLite](https://github.com/j256/ormlite-android).
The Android app uses standard Android APIs to access the SQLite database file.

If the flutter view is launched, by tapping on the dashboard fragment, the flutter view sometimes
fails to read the database.  Flutter (drift) just does a simple select to get all the items.

Both Android and Flutter read from the same db file. Android is only process that should have write
access.  

To speed up stepping on this bug, the app launches with a db that contains about 94 000 rows.

## Steps to reproduce the reported corruption

When the app opens, tap the "Generate items" button.  Then switch to the next tab over ("Dashboard").  If the
reads on the flutter side succeeded, you should see a normal flutter view on the top half of the screen, showing 
the count of items. Otherwise it will show the error from the db read.

It happens about 90% of the time for me that some error occurs on the flutter read side if the above steps are followed.

## Failures

Once the database reaches about 50 000 rows the inserts, I suspect get slow enough to race with
the selects from the flutter process.  If the SQLite db file is opened with `readWriteCreate`
then the flutter query sometimes gets the error

```
SqliteException(11): while selecting from statement, database disk image is malformed, database disk image is malformed (code 11)
  Causing statement: SELECT * FROM "dataitem";
```

If the file is opened with `readOnly` flutter gets the error

```
SqliteException(776): while selecting from statement, attempt to write a readonly database, attempt to write a readonly database (code 776)
  Causing statement: SELECT * FROM "dataitem";
```

### Reproduced environment

I am able to reliably reproduce this on an Android emulator on Android API level 32 if the inserts are happening concurrently with the select statement from flutter.

## Build setup

I am using Android studio to build and run both the Android and Flutter apps.  I am currently running Flutter version 3.19.6. 
Android build tools 35-rc3.

After the Android and Flutter SDK have been installed, cd into the `flutter_module` folder.  
Then run `flutter build aar --no-profile --no-release` to build the flutter part.  
Then run Android build and run on Android project as per normal.

## Some more backstory

We saw a very similar failure in the wild with our [app](https://skynamo.com/).  The failure was just a bit more sever:
The main app database gets corrupted and the Android side doesn't read it at all anymore.