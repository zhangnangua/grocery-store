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
                var divElement = parentAnchor.querySelector('.name');
                if (divElement) {
                    gameName = divElement.textContent || divElement.innerText;
                }
            } else if (element.tagName.toLowerCase() === 'div') {
                gameName = toCamelCase(link);
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
        if (data.link != "" && data.name != "") {
            event.preventDefault();
        }
        console.log(data);
        prompt(JSON.stringify(data), 'pt');
    },true);
})();

function toCamelCase(url) {
    let path = url.split('?')[0];
    if (path.endsWith('/')) {
      path = path.slice(0, -1);
    }
    const lastSegment = path.substring(path.lastIndexOf('/') + 1);
    return lastSegment
      .split('-')
      .map((word, index) => {
        return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
      })
      .join('');
  }