
$(document).ready(function() {
    var invocarTestPublico = function() {
        $.ajax({
            dataType: "json",
            url: 'https://localhost:8000/public/test'
        }).done(function(data) {
            $('#respuesta').text('Público Ok: ' + JSON.stringify(data));
        }).fail(function(error) {
            $('#respuesta').text('Público Ko: ' + JSON.stringify(error));
        });
    };

    var invocarTestPrivado = function() {
        $.ajax({
            dataType: "json",
            url: 'https://localhost:8000/private/test'
        }).done(function(data) {
            $('#respuesta').text('Privado Ok: ' + JSON.stringify(data));
        }).fail(function(error) {
            $('#respuesta').text('Privado Ko: ' + JSON.stringify(error));
        });
    };

    var invocarTestAutentificado = function() {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: 'https://localhost:8000/private/test',
            headers: {
                "Authorization": 'Basic ' + Base64.encode('admin' + ":" + 'adminadmin')
            }            
        }).done(function(data) {
            $('#respuesta').text('Privado Ok: ' + JSON.stringify(data));
        }).fail(function(error) {
            $('#respuesta').text('Privado Ko: ' + JSON.stringify(error));
        });
    };

    var invocarObtenerToken = function() {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: 'https://localhost:8000/credentials/token',
            headers: {
                "Authorization": 'Basic ' + Base64.encode('admin' + ":" + 'adminadmin')
            }            
        }).done(function(data) {
            $('#respuesta').text('Privado Ok: ' + JSON.stringify(data));
            sessionStorage.setItem('X-Auth-Token', data.encryptedToken);
        }).fail(function(error) {
            $('#respuesta').text('Privado Ko: ' + JSON.stringify(error));
        });
    };

    var invocarPrivadoConToken = function() {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: 'https://localhost:8000/private/test',
            headers: {
                "X-Auth-Token": sessionStorage.getItem('X-Auth-Token')
            }            
        }).done(function(data) {
            $('#respuesta').text('Privado Ok: ' + JSON.stringify(data));
        }).fail(function(error) {
            $('#respuesta').text('Privado Ko: ' + JSON.stringify(error));
        });
    };

    var invocarAltaPersona = function() {
        var persona = {docIdentificacion : '12345678A', nombre : 'Bob', apellidos : 'Sponja'};
        $.ajax({
            type: 'POST',
            data : JSON.stringify(persona),
            dataType: "JSON",
            contentType: "application/json; charset=utf-8",
            url: 'https://localhost:8000/public/personas',
            headers: {
                "X-Auth-Token": sessionStorage.getItem('X-Auth-Token')
            }            
        }).done(function(data) {
            $('#respuesta').text('Alta Ok: ' + JSON.stringify(data));
        }).fail(function(error) {
            $('#respuesta').text('Alta Ko: ' + JSON.stringify(error));
        });
    };

    $('#cmdPublico').click(function(evt) {
        evt.preventDefault();
        invocarTestPublico(); 
    });
    $('#cmdPrivado').click(function(evt) {
        evt.preventDefault();
        invocarTestPrivado();
    });
    $('#cmdPrivadoAutentificado').click(function(evt) {
        evt.preventDefault();
        invocarTestAutentificado();
    });
    $('#cmdObtenerToken').click(function(evt) {
        evt.preventDefault();
        invocarObtenerToken();
    });
    $('#cmdPrivadoConToken').click(function(evt) {
        evt.preventDefault();
        invocarPrivadoConToken();
    });
    $('#cmdAltaPersona').click(function(evt) {
        evt.preventDefault();
        invocarAltaPersona();
    });
    
});