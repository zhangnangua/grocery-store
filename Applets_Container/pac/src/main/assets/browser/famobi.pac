(function () {
    document.addEventListener('click', function (event) {
        var element = event.target;
        var parentAnchor = element.closest('a');
        var imgPath = "";
        var link = "";
        var gameName = "";
        var imgPath = parentAnchor.querySelector('img').src;
        var link = parentAnchor.href;
        gameName = toCamelCase(link);
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