# Pink it Safe

A toolkit to secure your Android applications in a simple and effective way.

-------
<p align="center">
    <a href="#installation">Installation</a> &bull;
    <a href="#safe-storage">Safe Storage</a> &bull;
    <a href="#biometrics">Biometrics</a>
</p>

-------

## Installation

``` groovy
dependencies {
    implementation 'dev.pinkroom:pinkitsafe:<latest_version>'
}
```

## Safe Storage

The safe storage tool uses the `EncryptedSharedPreferences` under the wood to save your data in an
encrypted file using key-value pairs.

To start using it create a `SafeStorage` instance:

````kotlin
val safeStorage = SafeStorage(context)
````

If you want the storage to be protected by user authentication, like biometrics, use:

````kotlin
val safeStorage = SafeStorage(context, authenticationRequired = true)
````

To save something on it:

````kotlin
safeStorage.save("secret-key", "Go away Mitnick!")
````

To get the value for a given key:

````kotlin
val secret = safeStorage.get<String>("secret-key")
````

Finally, to clear all the key-values stored so far:

````kotlin
safeStorage.clear()
````

## Biometrics

If you protected your `SafeStorage` with user authentication, or if you simply want to control a
flow in your App using biometrics, you can create an instance of `Biometrics`:

````kotlin
val biometrics = Biometrics(context)
````

**Note:** There are 3 ways to instantiate the `Biometrics` class. Using a `Context`, a `Fragment` or
a `FragmentActivity`. If you pass a context, make sure your activity extends the `AppCompatActivity`
or `FragmentActivity`.

Then, ask the user to authenticate:

````kotlin
biometrics.authenticate("Authenticate") {
    // User was authenticated with success
}
````

The `Biometrics` class is a simple wrapper of the `BiometricPrompt` provided in `androidx`. You can
validate the other parameters received by the `authenticate` function in order to customise the
prompt or handle errors.

## License

    Copyright 2023 Pink Room, Lda

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.