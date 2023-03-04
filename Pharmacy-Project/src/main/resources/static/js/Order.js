var op= document.getElementById("name");
var table = document.getElementById("tablee");
var cop= document.getElementById("copy");
var cop2= document.getElementById("copy2");
var f = 0;
const mapa = new Map();
var desc =document.getElementById("desc");

function doSomething()
{
f=1;
}
function doSomething1()
{
    if(f==1)
    {

        var clone = cop.cloneNode(true);
        var clone2 = cop2.cloneNode(true);
        var row = table.insertRow(-1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        cell1.appendChild(clone);
        cell2.appendChild(clone2);
        f=0;
    }

    else
    {
        alert("Chose Medicine First !");
    }
}
function submit()
{

   if(f==1)
   {
       var a = desc.value.valueOf();
   if(a.trim().length > 5 )
   {
       mapa.clear();
       let elements = document.getElementsByName("name");
       let inputi = document.getElementsByName("num");
       for(let i = 1;i < elements.length;i++)
       {
           if(parseInt(inputi[i].value) < 1)
           {
               mapa.clear();
               alert("Number of items cant be less that 1 !");
               break;
           }
           mapa.set(elements[i].value,parseInt(inputi[i].value));
       }
       zahtev();
   }
   else
   {
       alert("Comment Lenght to small !");
   }


   }
   else
   {
       alert("Chose Medicine First !");
   }

}



function zahtev()
{
    var a = desc.value.valueOf();
    const json = JSON.stringify(Object.fromEntries(mapa));
    const FD = new FormData();
    FD.append("mapa",json);
    FD.append("com",a.trim());
    const pera = Object.fromEntries(FD);
    const json1 = JSON.stringify(pera);
    console.log(typeof FD);
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "Medicine/order", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
            } else {
                console.error(xhr.statusText);
            }
        }
    };
    xhr.send(json1);
}