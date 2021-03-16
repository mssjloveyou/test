var interval=0;
function autoStart(){
    if(interval == 0){
        $("#autoValue").html("Auto Stop");
        interval=setInterval(loadData,60000);
    }else{
        $("#autoValue").html("Auto Start");
        clearInterval(interval);
    }
}
function openDetailPage(code){
    window.open("https://xueqiu.com/S/"+code,"_blank");
}
function loadData(){
    $("#loading").css("display","");
    loadSZSHCount();
    var date = $("#inputDate").val();
    $.ajax({
        url:"/stock/etf/"+date,
        success:function(data){
             $("#loading").css("display","none");
            $("#leftTable").empty();
            $("#rightTable").empty();
            var tmpCount = 0 ;
            for(var obj in data.top){
                tmpCount++;
                var str = "<tr>";
                   str+="<td onclick='javascript:openDetailPage(\""+data.top[obj].code+"\")'>";
                   str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.top[obj].code+"</h5>";
                   str+="    <span class='text-muted font-13'>"+data.top[obj].name+"</span>";
                   str+="</td>";
                   str+="<td>";
                   str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.top[obj].fluctuate+"</h5>";
                   str+="    <span class='text-muted font-13'>Flucture</span>";
                   str+="</td>";
                   str+="<td>";
                   str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.top[obj].currentPrice+"</h5>";
                   str+="    <span class='text-muted font-13'>Price</span>";
                   str+="</td>";
                    $("#leftTable").append(str);
            }
            for(var obj in data.today){
                    tmpCount++;
                    var str = "<tr>";
                       str+="<td onclick='javascript:openDetailPage(\""+data.today[obj].code+"\")'>";
                       str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.today[obj].code+"</h5>";
                       str+="    <span class='text-muted font-13'>"+data.today[obj].name+"</span>";
                       str+="</td>";
                       str+="<td>";
                       str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.today[obj].todayFluctuate+"</h5>";
                       str+="    <span class='text-muted font-13'>Flucture</span>";
                       str+="</td>";
                       str+="<td>";
                       str+="    <h5 class='font-14 my-1 font-weight-normal'>"+data.today[obj].currentPrice+"</h5>";
                       str+="    <span class='text-muted font-13'>Price</span>";
                       str+="</td>";
                       $("#rightTable").append(str);
                }

        }
    });
}
function loadSZSHCount(){
    $.ajax({
        url:"/stock/shszcount",
        success:function(data){
            for(var tmp in data){
                var str  = data[tmp].name+" " + data[tmp].index + " "+(data[tmp].count/10000)+"亿";
                $("#"+data[tmp].code).html(str);
            }
        }
    })
}
function init(){
    $.ajax({
        url:"/stock/calculateDate",
        success:function(data){
            $("#inputDate").val(data);
        }
    }).done(function(){
        loadData();
    });
}
$(function(){
init();
});