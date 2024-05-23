(function () {
    document.addEventListener('click', function (event) {
        var element = event.target;
        var parentAnchor = element.closest('.header');
        var imgPath = "";
        var link = "";
        var gameName = "";
        var imgPath = parentAnchor.querySelector('img').src;
        var link = parentAnchor.querySelector('a').href;
        const lastSegment = link.split('/').pop();
        const camelCase = lastSegment.split('-').map((word, index) => {
            if (index === 0) {
                return word;
            }
            return word.charAt(0).toUpperCase() + word.slice(1);
        }).join('');
        gameName = camelCase;

        var data = {
            img: imgPath,
            name: gameName,
            link: link
        };
        if (data.link != "" && data.name != "") {
            event.preventDefault();
        }
        console.log(data);
        prompt(JSON.stringify(data), 'pt');
    });
})();