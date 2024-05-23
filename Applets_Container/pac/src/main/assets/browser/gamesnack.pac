(function () {
    document.addEventListener('click', function (event) {
        var element = event.target;
        var parentAnchor = element.closest('a');
        var imgPath = "";
        var gameName = "";
        var link = "";
        if (parentAnchor) {
            link = parentAnchor.href;
            if (element.tagName.toLowerCase() === 'img') {
                imgPath = element.src;
                var divElement = parentAnchor.querySelector('div');
                if (divElement) {
                    gameName = divElement.textContent || divElement.innerText;
                }
            } else if (element.tagName.toLowerCase() === 'div') {
                gameName = element.textContent || element.innerText;
                var imgElement = parentAnchor.querySelector('img');
                if (imgElement) {
                    imgPath = imgElement.src;
                }
            }
        }
        var data = {
            img: imgPath,
            name: gameName,
            link: link
        };
        console.log(element);
        prompt(JSON.stringify(data), 'pt');
    });
})();