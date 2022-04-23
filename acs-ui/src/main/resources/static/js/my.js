//-- Self-invoking validator function for forms.
(function () {
    'use strict';
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms)
        .forEach((form) => {
            form.addEventListener('submit',
                function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                        event.stopImmediatePropagation();
                        console.log("validation failed");
                    }
                    form.classList.add('was-validated');
                }, false)
        })
})();

//---

function formToEncodedURL(path, form) {
    return path + '?' + new URLSearchParams(Object.fromEntries(new FormData(form).entries()))
}

function formToJson(form) {
    return JSON.stringify(Object.fromEntries(new FormData(form).entries()))
}

function fetchFragment(url, targetFragment) {
    fetch(url)
        .then(response => response.text())
        .then(data => document.getElementById(targetFragment).innerHTML = data)
}

//---

// const updateBtn = document.getElementById("updateBtn");
// const myStorage = window.sessionStorage;

// if (myStorage.getItem("isConnected") == null) {
//     connect();
//     myStorage.setItem("isConnected", "true")
// }
//
// function connect() {
//     console.log("Connecting to Socket");
//     let stomp;
//     stomp = webstomp.over(new SockJS('/socket-ap'));
//     stomp.connect({},
//         function (frame) {
//             console.log('Client connected');
//             stomp.subscribe('/live-update/personnel',
//                 function (response) {
//                     showUpdate(response);
//                 });
//         });
// }
//
// function showUpdate(response) {
//     const input = response.body;
//     console.log("Client received: " + input);
//     updateBtn.style.display = "block";
// }

// var table = document.getElementById('exampleModal');
// var button = document.getElementById('exampleModal');
//
// $(function () {
//     $button.click(function () {
//         alert('getSelections: ' +  $("#table").bootstrapTable('getSelections'));
//     });
// });

// let deleteModal = document.getElementById('deleteModal')
// let x = '[[@{${#request.requestURI}}]]'
//
// deleteModal.addEventListener('show.bs.modal',
//     function (event) {
//         // Button that triggered the modal
//         let button = event.relatedTarget
//         let recipient = button.getAttribute('id')
//
//         deleteModal.querySelector('.modal-title').textContent = 'Personnel ' + recipient
//         deleteModal.querySelector('.modal-confirm')
//             .addEventListener('submit',
//                 function () {
//                     fetch('[[@{${#request.requestURI}}]]' + '?idx=' + recipient)
//                         .then(data => console.log(data));
//                 })
//     })
