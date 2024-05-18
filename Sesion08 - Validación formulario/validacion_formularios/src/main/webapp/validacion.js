function validarFormulario() {
    var nombre = document.getElementById('nombre').value;
    var email = document.getElementById('email').value;

    if (nombre === "" || email === "") {
        alert("Se debe rellenar todos los campos.");
        return false;
    }
    return true;
}
