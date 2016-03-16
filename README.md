# SettingsManager
A library that interfaces with SharedPreferences and makes it easy to store data across devices including Android Wear syncing

I've found that, for saving simple data, the SharedPreferences class is great. This library wraps SharedPreferences to easily 
integrate that with Android Wear through automatic syncing, Google Drive, and Google Play Games.

## Gradle
You can get the vanilla SettingsManager through Gradle:

    compile 'com.github.fleker:settingsmanager:1.2.2'

## SettingsManager

### Constructor
`new SettingsManager(Context)`

### Methods
`setInt(int resId, int value)` or `setInt(String key, int value)`

You can set a value by passing the resource id of your key or the key directly, along with the value you want saved.

`getInt(int resId)` or `getInt(String key)`

You can get an integer value by passing the resource id of your key or the key directly.

These methods also work for `Boolean`, `String`, and `Long`.

### Google Play Games - Cloud Saves
Cloud saves require you to write and read byte arrays. It's easy to turn your SharedPreferences into a JSONObject and then to a byte 
array by calling:

`writeSnapshotToBytes()` 

And you can read them by calling:

`readSnapshotAsBytes(byte[] array)`

You should read the documentation on cloud saves to learn more about their implementation. This library just makes it easy to store 
and read data. 

### Android Wear
To use Android Wear, use the `WearSettingsManager` class by including the following library **on your phone**:

    compile 'com.github.fleker:settingsmanager-wear:1.2.2'
    
Your watch will require a slightly separate version of the library.

    compile 'com.github.fleker:settingsmanager-wearable:1.2.2'

The `WearSettingsManager` is an extension of the `SettingsManager` that, when a value is changed, syncs all of the data. This works
both ways, if data is changed on the phone or on the watch. By including the library, the necessary listeners will be added to the project on each device.

You can manually reset data to another device by calling:

`pushData()`

# Storing Arrays 
If you want to easily store an array in `SharedPreferences` you should consider using the `CommaArray` class. Included in the library, it will store an array of data as a `String` separated by commas. This could be a great way to store numbers. 

## Initialization
To get a `CommaArray` pass a `String`, preferably one from your `SettingsManager`:

    CommaArray mCommaArray = new CommaArray(sm.getString(R.string.key));
    
## Saving
To save this data, you can update the value in `SettingsManager`

    sm.setString(R.string.key, mCommaArray.toString());
    
## Methods
| Name | Return | Description |
| :--- | :---:  | ---:        |
| `size()` | int | Returns the number of items |
| `get(int pos)` | String | Returns the item at the given position as a String. To get a number you will need to parse this value. |
| `last()` | String | Gets the item at the end of the array |
| `set(int pos, String val)` | void | Changes the value of a particular item in the array at a given index |
| `add(String val)` | CommaArray | Appends an item to the end of the array and then returns itself | 
| `remove(String val)` | void | Removes the first item matching the value |
| `contains(String val)` | boolean | Returns true if one item in the array matches a given value |
| `toString()` | String | Returns the array as a string, joined by commas |
| `sum()` | long | Returns the summation of all items in the array (assuming they are all integers) |
| `getIterator()` | Iterator<String> | Returns an iterator for this array |
| `truncate(int newSize)` | void | Removes items from the head of the array until it is at its new size |
| `getRange(int start)` | CommaArray | Returns an array with items starting at a given position |
| `getRange(int start, int end)` | CommaArray | Returns an array with items between two positions |
| `average()` | long | Returns the average value of all items in the array (assuming they are all integers) |
| `averageDelta()` | long | For incrementing values like timestamps, returns the average change between subsequent items (assuming they are all integers) |
