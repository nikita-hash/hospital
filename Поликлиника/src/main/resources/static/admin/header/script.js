var tool_bar=document.getElementById('tool_bar');
var header_content=document.getElementById("header_content");
let  map_tool_block=new Map();
map_tool_block.set(1,header_content.children[1])
    .set(2,header_content.children[2])
    .set(3,header_content.children[3])
    .set(4,header_content.children[4])
    .set(6,header_content.children[5]);


function createChart(){
    const data = {
        labels: ['День 1', 'День 2', 'День 3', 'День 4', 'День 5', 'День 6', 'День 7'],
        datasets: [{
            label: 'Количество записей',
            data: [10, 5, 8, 15, 7, 12, 10],
            borderColor: '#FB297BFF',
            backgroundColor: 'rgba(239,117,161,0.4)',
            tension: 0.1
        }]
    };

// Создание графика
    const ctx = document.getElementById('myChart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontColor:'#6a5ac3'
                    },
                    gridLines:{
                        display:false
                    }
                }],
                xAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontColor:'#6a5ac3'
                    },
                    gridLines:{
                        display:false
                    }
                }]
            },
            legend: {
                display: false
            },
            title: {
                display: true,
                text: 'Продажи по дням недели',
                boxWidth: 0 // Убираем квадрат около названия
            },
            layout: {
                padding: {
                    top: 10,
                    bottom: 10
                }
            },
            elements: {
                line: {
                    tension: 0, // Убираем сглаживание линии
                }
            }
        }
    });
}


function saveStorage(id,el){
    sessionStorage.setItem(id,el);
}

function getStorageItems(){

}

window.addEventListener('load', function() {
    var savedState = Number(sessionStorage.getItem('componentState'));
    console.log(savedState);
    if(savedState!=null){
        tool_bar.querySelector("ul").children[savedState].className="open_tool_item";
        openToolUserMenu(savedState+1,map_tool_block);
    }
    else {
        tool_bar.querySelector("ul").children[0].className="open_tool_item";
        openToolUserMenu(1,map_tool_block);
    }
});

tool_bar.onclick=function (e){
    if(e.target.tagName=="LI" || e.target.tagName=="I" || e.target.tagName=="FONT"){
        var el=e.target.closest("LI");
        tool_bar.getElementsByClassName("open_tool_item")[0].className="";
        var index = Array.prototype.indexOf.call(tool_bar.getElementsByTagName("UL")[0].children, el);
        el.className="open_tool_item";
        saveStorage("componentState",index);
        openToolUserMenu(index+1,map_tool_block);
    }
}

createChart();