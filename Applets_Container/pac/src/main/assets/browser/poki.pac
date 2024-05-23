(function () {
    document.addEventListener('click', function (event) {
        var element = event.target;
        var imgPath = "";
        var gameName = "";
        var link = element.href;
        var imgElement = element.querySelector('img');
        if (imgElement) {
            imgPath = imgElement.src;
        }
        const lastSegment = link.split('/').pop();
        const camelCase = lastSegment.split('-').map((word, index) => {
            if (index === 0) {
                return word;
            }
            return word.charAt(0).toUpperCase() + word.slice(1);
        }).join('');
        var data = {
            img: imgPath,
            name: camelCase,
            link: link
        };
        console.log(data);
        prompt(JSON.stringify(data), 'pt');
    });
})();