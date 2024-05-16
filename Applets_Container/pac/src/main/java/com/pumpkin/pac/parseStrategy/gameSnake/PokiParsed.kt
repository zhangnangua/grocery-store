package com.pumpkin.pac.parseStrategy.gameSnake

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.pumpkin.pac.parseStrategy.InjectJsParsed

class PokiParsed() : InjectJsParsed() {

    constructor(parcel: Parcel) : this()

    override fun checkIllegal(url: Uri, path: String): Boolean {
        return !path.contains("g")
    }

    override fun js(url: Uri, path: String): String = js(path)

    private fun js(path: String): String {
        val name = path.substring(path.lastIndexOf("/"))
        return """
            (function() {
    var foundData = null;
    var links = document.getElementsByTagName('a');
    function getDivText(divElement) {
        if (divElement) {
            if (divElement.textContent.trim().length > 0) {
                return divElement.textContent.trim();
            }
            var childDiv = divElement.querySelector('div');
            return getDivText(childDiv);
        }
        return '';
    }
    for (var i = 0; i < links.length; i++) {
        if (links[i].getAttribute('href').includes('$path')) {
            var img = links[i].querySelector('img');
            if (img) {
                var imgSrc = img.getAttribute('src');
                foundData = {
                    image: imgSrc,
                    name: $name
                };
                break;
            }
        }
    }
    if (foundData) {
        return foundData;
    }
    return "";
    })();
    """.trimIndent()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokiParsed> {
        override fun createFromParcel(parcel: Parcel): PokiParsed {
            return PokiParsed(parcel)
        }

        override fun newArray(size: Int): Array<PokiParsed?> {
            return arrayOfNulls(size)
        }
    }
}