function showGreeting(tennews) {
    $.each(tennews, function(i, onenews){
        var tbody = $('#latestnews');
        var tr = $('<tr></tr>');
        var tdTitle = $('<td></td>');
        var aTitle = $('<a></a>');
        var tdTag = $('<td></td>');
        aTitle.text(onenews.title);
        aTitle.attr("href", onenews.sourceUrl).attr("target", "_blank");
        tdTag.text(onenews.tag);

        tdTitle.append(aTitle);
        tr.append(tdTitle);
        tr.append(tdTag);
        tbody.append(tr);
    });

    for (var i=0; i<message.length; i++){
        $("#greetings").append("<tr><td>" +
        "<a href='"+message[i].sourceUrl+"'>"+message[i].title+"</a>"
         + "</td></tr>");
    }
}

$(document).ready(function(){
    function addLatestNews(tennews){

    }

    $.getJSON("/thirtynews",
        function(data){
            showGreeting(data);
        }
    );
});