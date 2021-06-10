pma-assignment-7
=====

[![build](https://github.com/MikuAuahDark/pma-assignment-7/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/MikuAuahDark/pma-assignment-7/actions/workflows/build.yml)

Final task for Mobile Programming subject using The Movie DB API.

Notes
-----

For security reasons, 3 files has been omitted from the repository:

* app/src/main/assets/apikey.txt

* app/src/main/assets/apikey-decrypted.txt

* app/src/main/id/co/npad93/pm/t7/ApiTokenDecrypter.java

`apikey-decrypted.txt` is your v4 moviedb bearer token.

`apikey.txt` is `apikey-decrypted.txt` but encrypted with [HonokaMiku](https://github.com/MikuAuahDark/HonokaMiku) JP version 3.

Only use one of them. If you have `ApiTokenDecrypter.java`, put your API key as `apikey.txt`. Otherwise, `apikey-decrypted.txt`.

Search
-----

Typing on search does NOT immediately start the search API call to prevent the app get rate limited by TMDB. Instead, it's delayed by 500ms.
Press the search button manually in your keyboard to bypass the 500ms delay.
