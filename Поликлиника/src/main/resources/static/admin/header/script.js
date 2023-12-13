var tool_bar=document.getElementById('tool_bar');
var header_content=document.getElementById("header_content");
let  map_tool_block=new Map();
map_tool_block.set(1,header_content.children[1])
    .set(2,header_content.children[2])
    .set(3,header_content.children[3])
    .set(4,header_content.children[4])
    .set(5,header_content.children[5])
    .set(6,header_content.children[6])
    .set(7,header_content.children[7]);


function createChart(){
    const ctx = document.getElementById('myChart').getContext('2d');
    const masDay=["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]
    var  user;
    axios.get('/admin/getChart',{
    })
        .then((response) => {
            console.log(response);
            user=response.data;
            const data = {
                labels: ['Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота', 'Воскресенье'],
                datasets: [{
                    label: 'Количество записей',
                    data: [user[0].count,
                        user[1].count,
                        user[2].count,
                        user[3].count,
                        user[4].count,
                        user[5].count,
                        user[6].count],
                    borderColor: '#FB297BFF',
                    backgroundColor: 'rgba(239,117,161,0.4)',
                    tension: 0.1
                }]
            };

// Создание графика
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
        })
        .catch((error) => {
            console.log(error);
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
    if((e.target.tagName=="LI" || e.target.tagName=="I" || e.target.tagName=="FONT" || e.target.tagName=="DIV" )&& e.target.className!="tool_bar"){
        var el=e.target.closest("LI");
        tool_bar.getElementsByClassName("open_tool_item")[0].className="";
        var index = Array.prototype.indexOf.call(tool_bar.getElementsByTagName("UL")[0].children, el);
        el.className="open_tool_item";
        saveStorage("componentState",index);
        openToolUserMenu(index+1,map_tool_block);
    }
}


createChart();