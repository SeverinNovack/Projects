var pricelist = new Map();

function deleteAllSelected() {
    "use strict";
    let dltall = document.getElementsByName("dltallbtn");
    let itemList = document.getElementById("order");
    for (let i = itemList.options.length - 1; i >= 0; i--) {
        itemList.remove(i);
        document.getElementById("totalprice").innerText = 0;
    }
}

function deleteSelected() {
    "use strict";
    let dlt = document.getElementsByName("dltbtn");
    let itemList = document.getElementById("order");
    for (let i = itemList.options.length; i >= 0; i--) {
        alterTotalPrice(itemList.options[itemList.selectedIndex].text, false);
        itemList.options[itemList.selectedIndex] = null;
    }
}

function btnActivity() {
    "use strict";
    let ord = document.getElementById("ordbtn");
    let inp = document.getElementById("adrinput");
    let itemList = document.getElementById("order");
    if (inp.value.length == 0 || itemList.length == 0) {
        ord.disabled = true;
    } else {
        ord.disabled = false;
    }
}

function alterTotalPrice(price, state) {
    "use strict";
    let p = document.getElementById("totalprice").innerText;
    if (state == true) {
        var newp = parseFloat(p) + parseFloat(price);
    } else {
        let val = pricelist.get(price);
        var newp = parseFloat(p) - parseFloat(val);
    }
    document.getElementById("totalprice").innerText = newp.toFixed(2);
}

function addToCart(id, pnr, preis) {
    "use strict";
    alterTotalPrice(preis, true);
    let itemList = document.getElementById("order");
    let myoption = document.createElement('option');
    myoption.value = pnr; //pnr
    myoption.text = id.innerText; //id.innerText
    myoption.selected = true;
    itemList.add(myoption);
    pricelist.set(id.innerText, preis);
}

function makeEverythingSelected() {
    "use strict";
    let itemList = document.getElementById("order");
    for (let i = 0; i < itemList.length; i++) {
        itemList[i].selected = true;
    }
}