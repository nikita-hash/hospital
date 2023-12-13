var personal_src=null;

function changePhoto(inp){
    let file = inp.files[0]
    let avatar = inp.closest('DIV').querySelector('img')
    if (file != null) {
        console.log(inp.files[0])
        let reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = function () {
            avatar.src = reader.result
        }
        personal_src=inp.value;
    }
    else if(personal_src!=null){
        inp.value=personal_src;
        console.log(inp.files[0]);
    }
}