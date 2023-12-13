var tool_bar=document.getElementById('tool_bar');
var header_content=document.getElementById("header_content");
let  map_tool_block=new Map();
console.log(header_content.childNodes)
map_tool_block.set(1,header_content.children[1])
    .set(2,header_content.children[2])
    .set(3,header_content.children[3])
    .set(5,header_content.children[4])
    .set(6,header_content.children[5])


function saveStorage(id,el){
    sessionStorage.setItem(id,el);
}

function openToolUserMenu(id,map1){
    console.log(map1.get(id));
    map1.forEach(el => {
        el.classList.add("close_block");
    })
    map1.get(id).classList.remove("close_block");
}

function getStorageItems(){

}

window.addEventListener('load', function() {
    var savedState = Number(sessionStorage.getItem('componentState'));
    console.log(savedState);
    if(savedState!=null){
        console.log("hello")
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


