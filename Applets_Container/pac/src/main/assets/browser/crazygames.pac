(function () {
    document.addEventListener('click', function (event) {
        var element = event.target;
        var parentAnchor = element.closest('a');
        var imgPath = "";
        var link = "";
        var gameName = "";
        var imgPath = parentAnchor.querySelector('img').src;
        var link = parentAnchor.href;
        const lastSegment = link.split('/').pop();
        const camelCase = lastSegment.split('-').map((word, index) => {
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