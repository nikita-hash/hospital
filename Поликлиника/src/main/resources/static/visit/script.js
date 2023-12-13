function init(){
    if(document.getElementsByClassName("al")[0]!=null){
        document.getElementById('complite').disabled=true;
        document.getElementById('print').classList.remove('disabled');
    }
}

init();

const form = document.getElementById('myForm');

// Добавляем обработчик события отправки формы
form.addEventListener('submit', function(event) {
    document.getElementById('complite').disabled=true;
});