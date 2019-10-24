function process(jsonString) {
    "use strict";
    let list = document.getElementById("ordstat");
    console.log("Hello");

    const string = '[{"PizzaName":"Schinken","Status":"bestellt"},' +
        '{"PizzaName":"Margherita","Status":"bestellt"}]';

    while(list.firstChild){
        list.removeChild(list.firstChild);
    }
    let head = document.createElement('h1');
    head.appendChild( document.createTextNode("Lieferstatus"));
    list.appendChild(head);
    if(jsonString) {

        let jsonData = JSON.parse(jsonString);

        for (let i = 0; i < jsonData.length; i++) {
            let pname = jsonData[i].PizzaName;
            let stat = jsonData[i].Status;
            let status = document.createElement('div');
            let s = pname + ": " + stat;
            status.appendChild(document.createTextNode(s));
            status.className = "Status"
            list.appendChild(status);
        }
    }

}


// request als globale Variable anlegen (haesslich, aber bequem)
var request = new XMLHttpRequest();

function requestData() { // fordert die Daten asynchron an
    request.open("GET", "KundenStatus.php"); // URL für HTTP-GET
    request.onreadystatechange = processData; //Callback-Handler zuordnen
    request.send(null); // Request abschicken
}

function processData() {
    if(request.readyState == 4) { // Uebertragung = DONE
        if (request.status == 200) {   // HTTP-Status = OK
            if(request.responseText != null) {
                console.log(request.responseText);
                process(request.responseText);// Daten verarbeiten
            }
            else console.error ("Dokument ist leer");
        }
        else console.error ("Uebertragung fehlgeschlagen");
    } else ;          // Uebertragung laeuft noch
}

window.setInterval(requestData, 2000);