var tool_bar_user=document.getElementById('tool_users_type_button');
var users_block=document.getElementById('users_block');
var modalDelUser=new bootstrap.Modal(document.getElementById('deleteUserModalLabel'));
var modalAddDoctor=new bootstrap.Modal(document.getElementById("addDoctorModal"));
var mapModal= new Map;
mapModal.set("addDoctorModal",modalAddDoctor)
    .set("deleteUserModalLabel",modalDelUser);

var map=new Map();
map.set(1,users_block.children[1])
    .set(2,users_block.children[2])
    .set(3,users_block.children[3]);

function openToolUserMenu(id,map1){
    map1.forEach(el => {
        el.classList.add("close_block");
    })
    map1.get(id).classList.remove("close_block");
}

window.addEventListener('load', function() {
    var savedState = Number(sessionStorage.getItem('tool_users_state'));
    if(savedState!=null){
        tool_bar_user.children[savedState].className="open";
        openToolUserMenu(savedState+1,map);
    }
    else {
        tool_bar_user.children[0].className="open";
        openToolUserMenu(1,map);
    }
});

tool_bar_user.onclick=function (e){
        var but = e.target.closest("BUTTON");
        if(but!=null){
            tool_bar_user.getElementsByClassName("open")[0].className="";
            but.className="open";
            var index=Array.prototype.indexOf.call(tool_bar_user.children, but)
            openToolUserMenu(index+1,map);
            saveStorage("tool_users_state",index);
        }
}


const filterButton = document.getElementById('filter_patient');
const filterContainer = document.getElementById('filterContainer');

console.log(filterButton)

filterButton.addEventListener('click', () => {
    filterContainer.classList.toggle('show');
});

filterContainer.addEventListener("click", function(event) {
    event.stopPropagation();
});

document.addEventListener('click', (event) => {
    if (!filterButton.contains(event.target)) {
        filterContainer.classList.remove('show');
    }
});

function showConfirmationModal(element) {
    var userId = element.getAttribute("data-id");
    var confirmationModal = document.getElementById("deleteUserModalLabel");
    modalDelUser.show();
    confirmationModal.querySelector('.btn-danger').setAttribute("onclick", "deleteUser(" + userId + ")");
}

function closeModal(name){
    console.log(name);
    console.log(mapModal.get(name));
    mapModal.get(name).hide();
}

function deleteUser(userId) {
    console.log(userId);
    closeModal();

    $.ajax({
        url: "/admin/delete",
        type: "DELETE",
        data: {
            id_patient: userId,
        },
        success: function(response) {
            console.log(response);
            location.reload();
        },
        error: function(error) {
            console.log(error)
        }
    });

}

