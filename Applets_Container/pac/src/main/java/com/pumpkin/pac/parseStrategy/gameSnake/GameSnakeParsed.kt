package com.pumpkin.pac.parseStrategy.gameSnake

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.pumpkin.pac.parseStrategy.InjectJsParsed

class GameSnakeParsed() : InjectJsParsed() {

    constructor(parcel: Parcel) : this()

    override fun checkIllegal(url: Uri, path: String): Boolean {
        return !path.contains("game")
    }

    override fun js(url: Uri, path: String): String = js(path)

    private fun js(path: String): String {
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
            var div = links[i].querySelector('div');
            if (img) {
                var imgSrc = img.getAttribute('src');
                var divText = getDivText(div);
                foundData = {
                    image: imgSrc,
                    name: divText
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

    companion object CREATOR : Parcelable.Creator<GameSnakeParsed> {
        override fun createFromParcel(parcel: Parcel): GameSnakeParsed {
            return GameSnakeParsed(parcel)
        }

        override fun newArray(size: Int): Array<GameSnakeParsed?> {
            return arrayOfNulls(size)
        }
    }
}